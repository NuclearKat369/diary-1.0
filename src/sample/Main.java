package sample;

import java.awt.*;
import java.io.File;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import javax.xml.xpath.XPathExpressionException;

import java.io.IOException;
import java.util.List;

public class Main extends Application {

    private final LocalTime firstSlotStart = LocalTime.of(0, 0);
    private final Duration slotLength = Duration.ofMinutes(30);
    private final LocalTime lastSlotStart = LocalTime.of(23, 59);

    private static LocalDateTime cal;
    Dates dates = new Dates();

    private final List<TimeSlot> timeSlots = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws IOException,
            XPathExpressionException, ParserConfigurationException, SAXException {


        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setStyle("-fx-font: 10pt \"Courier New\"; -fx-font-weight: bold");

        TabPane tabPane = new TabPane();

        Button addEvent = new Button("Добавить событие");
        Button updateEvents = new Button("Обновить данные");
        Button removeEvent = new Button ("Удалить событие");

        tabPane.setLayoutX(10);
        tabPane.setLayoutY(10);
        tabPane.setCursor(Cursor.DEFAULT);

        tabPane.setStyle("-fx-border-width:4pt;-fx-border-color:royalblue;");
        tabPane.setPrefSize(300, 300);
        tabPane.setTooltip(new Tooltip("Панель с вкладками"));
        tabPane.setSide(Side.TOP);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setTabMinHeight(20);
        tabPane.setTabMinWidth(100);

// Вкладка №1
        Tab scheduleTab = new Tab("Расписание");
        Group rootGrid = new Group();
        scheduleTab.setContent(rootGrid);

        GridPane scheduleGrid = new GridPane();
        scheduleGrid.setGridLinesVisible(true);
        scheduleGrid.setStyle("-fx-background-color: #bed5ff");

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        // Фомрирование таблицы, где будут отображаться дела
        for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)) {
            int slotIndex = 1;

            for (LocalDateTime startTime = date.atTime(firstSlotStart);
                 !startTime.isAfter(date.atTime(lastSlotStart));
                 startTime = startTime.plus(slotLength)) {

                TimeSlot timeSlot = new TimeSlot(startTime, slotLength, date);

                int year = date.getYear();
                int month = date.getMonthValue();
                int day = date.getDayOfMonth();
                int hour = startTime.getHour();
                int minute = startTime.getMinute();

                String text = CreateXmlFile.searchXml(year, month, day, hour, minute);
                if (text != "") {
                    Label label = new Label(text);
                    scheduleGrid.add(label, timeSlot.getDayOfWeek().getValue(), slotIndex);
                    label.setStyle("-fx-font-size: 9pt; -fx-font-weight: normal");
                }

                timeSlots.add(timeSlot);

                ColumnConstraints columnConstraints = new ColumnConstraints();
                columnConstraints.setMaxWidth(100);
                scheduleGrid.getColumnConstraints().add(columnConstraints);

                scheduleGrid.add(timeSlot.getView(), timeSlot.getDayOfWeek().getValue(), slotIndex);

                slotIndex++;
            }
        }

        // Заполнение заголовков таблицы
        // Заполнение по-горизонтали (дата и день недели)
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("E\nMMM d");

        for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)) {
            Label label = new Label(date.format(dayFormatter));
            label.setPadding(new Insets(1));
            label.setTextAlignment(TextAlignment.CENTER);
            GridPane.setHalignment(label, HPos.CENTER);
            scheduleGrid.add(label, date.getDayOfWeek().getValue(), 0);
        }

        // Заполнение по-вертикали (часы и минуты)
        int slotIndex = 1;
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        for (LocalDateTime startTime = today.atTime(firstSlotStart);
             !startTime.isAfter(today.atTime(lastSlotStart));
             startTime = startTime.plus(slotLength)) {
            Label label = new Label(startTime.format(timeFormatter));
            label.setPadding(new Insets(2));
            GridPane.setHalignment(label, HPos.RIGHT);
            scheduleGrid.add(label, 0, slotIndex);
            slotIndex++;
        }
        ScrollPane scroller = new ScrollPane(scheduleGrid);
        rootGrid.getChildren().add(scroller);
        scroller.setPrefViewportHeight(590);
        scroller.setPrefViewportWidth(700);

        // Вкладка №2
        Tab calendarTab = new Tab("Календарь");
        Group rootCal = new Group();
        calendarTab.setContent(rootCal);

        GridPane calendarGrid = new GridPane();
        calendarGrid.setGridLinesVisible(true);

        // Запрашивает у системы данные, какой сейчас год
        cal = LocalDateTime.now();
        dates.setYear(cal.getYear());
        String str = "Год: " + dates.getYear() + "\n";
        Label label = new Label(str);
        label.setPadding(new Insets(1));
        label.setTextAlignment(TextAlignment.CENTER);
        GridPane.setHalignment(label, HPos.CENTER);
        label.setStyle("-fx-font-weight: bold");

        int colIndex = 0;
        int rowIndex = 0;

        calendarGrid.add(label, colIndex, rowIndex);
        rowIndex++;

        //заполнение полей месяцами
        int colIndexMonth = 0;
        int rowIndexMonth = 2;
        for (int month = 1; month <= 12; month++) {

            //название месяца
            dates.setMonth(cal.withMonth(month).getMonth().getDisplayName(TextStyle.FULL, Locale.UK));
            dates.setMonthNumber(month);
            str = dates.getMonth();
            label = new Label(str);
            label.setPadding(new Insets(1));
            label.setTextAlignment(TextAlignment.CENTER);
            label.setStyle("-fx-font-weight: bold; -fx-background-color: PaleTurquoise");

            GridPane.setHalignment(label, HPos.LEFT);

            //расположение месяцев в календарном виде по 3 в ряд
            if (month % 3 != 0) {
                calendarGrid.add(label, colIndexMonth, rowIndexMonth);
                colIndexMonth++;
            } else {
                calendarGrid.add(label, colIndexMonth, rowIndexMonth);
                colIndexMonth = 0;
                rowIndexMonth += 3;
            }

            rowIndex = rowIndexMonth + 1;
            //расположение месяцев так, чтобы они не накладывались друг на друга
            if (colIndexMonth != 0) {
                colIndex = colIndexMonth - 1;

            } else {
                colIndex = colIndexMonth + 2;
                rowIndex = rowIndexMonth - 2;
            }

            GridPane dow = new GridPane();
            dow.setGridLinesVisible(true);

            //заполнение названий дней недели
            for (int i = 0; i < 7; i++) {
                int dowColInd = i;
                Calendar temp = Calendar.getInstance();
                temp.set(Calendar.DAY_OF_WEEK, i + 2);
                label = new Label(temp.getDisplayName(temp.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH));
                label.setPadding(new Insets(1));
                label.setTextAlignment(TextAlignment.CENTER);
                if (dowColInd > 4) label.setTextFill(Color.RED);
                GridPane.setHalignment(label, HPos.LEFT);
                dow.getColumnConstraints().add(new ColumnConstraints(30));
                dow.add(label, dowColInd, 0);
            }
            calendarGrid.add(dow, colIndex, rowIndex);

            rowIndex++;


            //заполнение дней в месяце
            GridPane datesTable = new GridPane();
            datesTable.setGridLinesVisible(true);

            for (int w = 0; w < 7; w++) {
                datesTable.getColumnConstraints().add(new ColumnConstraints(30));
            }
            int colIndexDates;
            int rowIndexDates = 0;
            String[] calendarTable = new String[42];

            for (int n = 0; n < calendarTable.length; n++) {


                n += cal.withMonth(month).withDayOfMonth(1).getDayOfWeek().getValue() - 1;

                dates.setDayOfWeek(cal.withMonth(dates.getMonthNumber()).withDayOfMonth(1).getDayOfWeek().toString());

                colIndexDates = n;

                for (int s = 0; s < n; s++) {
                    calendarTable[s] = " ";
                }

                YearMonth yearMonth = YearMonth.of(dates.getYear(), dates.getMonthNumber());

                //проверка, является ли день началом недели, если да, то отображение идёт с новой строки
                for (int i = 1; i <= yearMonth.lengthOfMonth(); i++, n++) {
                    dates.setDay(i);
                    dates.setDayOfWeek(cal.withYear(dates.getYear())
                            .withMonth(dates.getMonthNumber())
                            .withDayOfMonth(dates.getDay())
                            .getDayOfWeek()
                            .toString());
                    if ((n + 1) % 7 == 0) {
                        str = Integer.toString(dates.getDay());
                        label = new Label(str);
                        label.setPadding(new Insets(1));
                        label.setTextAlignment(TextAlignment.CENTER);
                        GridPane.setHalignment(label, HPos.LEFT);
                        if (colIndexDates > 4) label.setStyle("-fx-text-fill: red");

                        if (dates.getYear() == LocalDateTime.now().getYear() &
                                dates.getMonth().equalsIgnoreCase(LocalDateTime.now().getMonth().toString()) == true &
                                dates.getDay() == LocalDateTime.now().getDayOfMonth())
                            label.setStyle("-fx-background-color: royalblue");
                        datesTable.add(label, colIndexDates, rowIndexDates);
                        colIndexDates = 0;
                        rowIndexDates++;
//                        System.out.printf("%4d", dates.getDay());
//                        System.out.println();
                    } else {
                        str = Integer.toString(dates.getDay());
                        label = new Label(str);
                        label.setPadding(new Insets(1));
                        label.setTextAlignment(TextAlignment.CENTER);
                        GridPane.setHalignment(label, HPos.LEFT);
                        if (colIndexDates > 4) label.setStyle("-fx-text-fill: red");

                        if (dates.getYear() == LocalDateTime.now().getYear() &
                                dates.getMonth().equalsIgnoreCase(LocalDateTime.now().getMonth().toString()) == true &
                                dates.getDay() == LocalDateTime.now().getDayOfMonth())
                            label.setStyle("-fx-background-color: royalblue");
                        datesTable.add(label, colIndexDates, rowIndexDates);
                        colIndexDates++;
                    }
                }
                break;
            }

            datesTable.setAlignment(Pos.TOP_LEFT);
            calendarGrid.add(datesTable, colIndex, rowIndex);
        }

        rootCal.getChildren().add(calendarGrid);

        //Вкладка №3

        Tab chartTab = new Tab("График занятости");
        Group rootChart = new Group();
        chartTab.setContent(rootChart);

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("День недели");
        yAxis.setLabel("Количество дел");

        //создание графика
        LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);

        lineChart.setTitle("График загруженности по дням недели");

        //создание серии данных
        XYChart.Series chart = new XYChart.Series();
        lineChart.setStyle("-fx-font: 10pt \"Courier New\"; -fx-font-weight: bold");

        //определение количества дел на каждый день недели для заполнения данных по оси Y графика
        int [] dutyCounter = new int[7];
        int i = 0;
        for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)) {

            int year = date.getYear();
            int month = date.getMonthValue();
            int day = date.getDayOfMonth();
            dutyCounter[i] = CreateXmlFile.searchXmlNumberOfEvents(year,month,day);
            i++;
            }

        //присвоение значения контрольным точкам
        chart.getData().add(new XYChart.Data("Пн", dutyCounter[0]));
        chart.getData().add(new XYChart.Data("Вт", dutyCounter[1]));
        chart.getData().add(new XYChart.Data("Ср", dutyCounter[2]));
        chart.getData().add(new XYChart.Data("Чт", dutyCounter[3]));
        chart.getData().add(new XYChart.Data("Пт", dutyCounter[4]));
        chart.getData().add(new XYChart.Data("Сб", dutyCounter[5]));
        chart.getData().add(new XYChart.Data("Вс", dutyCounter[6]));

        lineChart.getData().add(chart);
        rootChart.getChildren().add(lineChart);

        //Меню
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-font: 10pt \"Courier New\"; -fx-font-weight: bold");
        Menu menuFile = new Menu("Файл");
        Menu menuHelp = new Menu("Справка");
        menuBar.getMenus().addAll(menuFile, menuHelp);

        MenuItem menuItemQuit = new MenuItem("Выход");

        menuItemQuit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Platform.exit();
            }
        });
        menuFile.getItems().add(menuItemQuit);

        MenuItem menuItemHelp = new MenuItem("Открыть справку");

        menuItemHelp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                File file = new File("Help.txt");
                try {
                    Desktop.getDesktop().open(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        menuHelp.getItems().add(menuItemHelp);

        menuItemQuit.setAccelerator(KeyCombination.keyCombination("CTRL+Q"));
        menuItemHelp.setAccelerator(KeyCombination.keyCombination("CTRL+H"));

        tabPane.getTabs().addAll(scheduleTab, calendarTab, chartTab);

        addEvent.setPrefWidth(150);
        updateEvents.setPrefWidth(150);
        removeEvent.setPrefWidth(150);
        AnchorPane.setTopAnchor(tabPane, 40.0);
        AnchorPane.setLeftAnchor(tabPane, 10.0);
        AnchorPane.setRightAnchor(tabPane, 10.0);
        AnchorPane.setBottomAnchor(tabPane, 10.0);
        AnchorPane.setTopAnchor(addEvent, 5.0);
        AnchorPane.setRightAnchor(addEvent, 10.0);
        AnchorPane.setTopAnchor(updateEvents, 5.0);
        AnchorPane.setRightAnchor(updateEvents, 170.0);
        AnchorPane.setTopAnchor(removeEvent, 5.0);
        AnchorPane.setRightAnchor(removeEvent, 330.0);

        anchorPane.getChildren().addAll(tabPane, addEvent, removeEvent, updateEvents, menuBar);

        addEvent.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                Stage addEventStage = new Stage();
                VBox popupAdd = new VBox();
                Button submit = new Button("Сохранить");
                Button cancel = new Button("Отмена");
                popupAdd.setAlignment(Pos.CENTER);

                GridPane timeSet = new GridPane();
                timeSet.setMaxHeight(300);
                timeSet.setMaxWidth(400);
                timeSet.setAlignment(Pos.CENTER);
                DatePicker pickDate = new DatePicker();
                pickDate.setDayCellFactory(p -> new DateCell(){
                    public void updateItem(LocalDate date, boolean empty){
                        super.updateItem(date, empty);
                        LocalDate today = LocalDate.now();
                        setDisable(empty || date.compareTo(today)<0);
                    }
                });

                Spinner<Integer> pickHour = new Spinner();
                pickHour.setEditable(false);
                SpinnerValueFactory<Integer> hourFactory =
                        new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, LocalTime.now().getHour());
                pickHour.setValueFactory(hourFactory);


                Spinner<Integer> pickMinutes = new Spinner();
                pickMinutes.setEditable(false);
                SpinnerValueFactory<Integer> minutesFactory =
                        new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, LocalTime.now().getMinute());
                pickMinutes.setValueFactory(minutesFactory);

                Label inputText = new Label("Введите текст:");
                Label chooseDate = new Label("Выберите дату и время: ");
                Label chooseHour = new Label("Часы");
                Label chooseMinutes = new Label("Минуты");

                GridPane.setRowIndex(chooseDate, 1);
                GridPane.setRowIndex(pickDate, 2);

                GridPane.setRowIndex(chooseHour, 3);
                GridPane.setRowIndex(pickHour, 4);

                GridPane.setRowIndex(chooseMinutes, 5);
                GridPane.setRowIndex(pickMinutes, 6);

                TextArea ta = new TextArea();
                ta.setMaxWidth(400);
                ta.setEditable(true);

                submit.disableProperty().bind(Bindings.isEmpty(ta.textProperty())
                        .or(Bindings.isEmpty(pickDate.getProperties()))
                        .or(Bindings.isEmpty(pickHour.getProperties()))
                        .or(Bindings.isEmpty(pickMinutes.getProperties())));

                submit.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {

                        try {
                            LocalDate localDate = pickDate.getValue();
                            int year = localDate.getYear();
                            int month = localDate.getMonthValue();
                            int day = localDate.getDayOfMonth();
                            int hour = pickHour.getValue();
                            int minute = pickMinutes.getValue();
                            String note = ta.getText();
                            CreateXmlFile.addElement(year, month, day, hour, minute, note);
                            addEventStage.close();
                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        } catch (TransformerException e) {
                            e.printStackTrace();
                        } catch (SAXException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                cancel.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        addEventStage.close();
                    }
                });

                timeSet.getChildren().addAll(pickDate, pickHour, pickMinutes,
                        ta, chooseMinutes, chooseHour, chooseDate);
                popupAdd.getChildren().addAll(inputText, timeSet, submit, cancel);
                Scene addScene = new Scene(popupAdd, 500, 500);
                addEventStage.setScene(addScene);
                addEventStage.setTitle("Новое событие");
                addEventStage.setResizable(false);
                addEventStage.show();
            }
        });

        removeEvent.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                Stage removeEventStage = new Stage();
                VBox popupRemove = new VBox();
                popupRemove.setBackground(new Background(new BackgroundImage(
                        new Image("cancel.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                        BackgroundSize.DEFAULT)));
                Button submit = new Button("Сохранить");
                Button cancel = new Button("Отмена");
                submit.setPrefWidth(150);
                cancel.setPrefWidth(150);

                submit.setStyle("-fx-font: 10pt \"Courier New\"; -fx-font-weight: bold; -fx-background-color: #98ccff");
                cancel.setStyle("-fx-font: 10pt \"Courier New\"; -fx-font-weight: bold");
                popupRemove.setAlignment(Pos.CENTER);

                GridPane timeSet = new GridPane();
                timeSet.setMaxHeight(300);
                timeSet.setMaxWidth(400);
                timeSet.setAlignment(Pos.CENTER);
                DatePicker pickDate = new DatePicker();

                Spinner<Integer> pickHour = new Spinner();
                pickHour.setEditable(false);
                SpinnerValueFactory<Integer> hourFactory =
                        new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, LocalTime.now().getHour());
                pickHour.setValueFactory(hourFactory);

                Spinner<Integer> pickMinutes = new Spinner();
                pickMinutes.setEditable(false);
                SpinnerValueFactory<Integer> minutesFactory =
                        new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, LocalTime.now().getMinute());
                pickMinutes.setValueFactory(minutesFactory);

                pickDate.setStyle("-fx-font: 10pt \"Courier New\"; -fx-font-weight: bold");
                pickHour.setStyle("-fx-font: 10pt \"Courier New\"; -fx-font-weight: bold");
                pickMinutes.setStyle("-fx-font: 10pt \"Courier New\"; -fx-font-weight: bold");

                Label inputText = new Label("Выберите дату и время,\nкоторые необходимо освободить\n\n");
                inputText.textAlignmentProperty().setValue(TextAlignment.CENTER);
                inputText.setStyle("-fx-font: 12pt \"Courier New\"; -fx-font-weight: bold");
                Label chooseDate = new Label("Выберите дату и время: ");
                chooseDate.setStyle("-fx-font: 10pt \"Courier New\"; -fx-font-weight: bold");
                Label chooseHour = new Label("Часы");
                chooseHour.setStyle("-fx-font: 10pt \"Courier New\"; -fx-font-weight: bold");
                Label chooseMinutes = new Label("Минуты");
                chooseMinutes.setStyle("-fx-font: 10pt \"Courier New\"; -fx-font-weight: bold");

                GridPane.setRowIndex(chooseDate, 1);
                GridPane.setRowIndex(pickDate, 2);

                GridPane.setRowIndex(chooseHour, 3);
                GridPane.setRowIndex(pickHour, 4);

                GridPane.setRowIndex(chooseMinutes, 5);
                GridPane.setRowIndex(pickMinutes, 6);

                submit.disableProperty().bind(Bindings.isEmpty(pickDate.getProperties())
                        .or(Bindings.isEmpty(pickHour.getProperties()))
                        .or(Bindings.isEmpty(pickMinutes.getProperties())));

                submit.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {

                        try {
                            LocalDate localDate = pickDate.getValue();
                            int year = localDate.getYear();
                            int month = localDate.getMonthValue();
                            int day = localDate.getDayOfMonth();
                            int hour = pickHour.getValue();
                            int minute = pickMinutes.getValue();

                            CreateXmlFile.removeElement(year, month, day, hour, minute);
                            removeEventStage.close();
                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        } catch (TransformerException e) {
                            e.printStackTrace();
                        } catch (SAXException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (XPathExpressionException e) {
                            e.printStackTrace();
                        }
                    }
                });

                cancel.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        removeEventStage.close();
                    }
                });

                timeSet.getChildren().addAll(pickDate, pickHour, pickMinutes,
                        chooseMinutes, chooseHour, chooseDate);
                popupRemove.getChildren().addAll(inputText, timeSet, submit, cancel);
                Scene addScene = new Scene(popupRemove, 400, 300);
                removeEventStage.setScene(addScene);
                removeEventStage.setTitle("Удалить событие");
                removeEventStage.setResizable(false);
                removeEventStage.show();
            }
        });

        updateEvents.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    start(primaryStage);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XPathExpressionException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
            }
        });
        Scene scene = new Scene(anchorPane, 700, 700);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Ежедневник");
        primaryStage.getIcons().add(new Image("logo.png"));
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    // Вспомогательный класс для определения временного промежутка при создании вкладки расписание
    public static class TimeSlot {

        private final LocalDateTime start;
        private final Duration duration;
        private final Region view;
        private final LocalDate date;

        public TimeSlot(LocalDateTime start, Duration duration, LocalDate date) {
            this.start = start;
            this.duration = duration;
            this.date = date;

            view = new Region();
            view.setMinSize(80, 20);

        }


        public LocalDateTime getStart() {
            return start;
        }

        public LocalTime getTime() {
            return start.toLocalTime();
        }

        public DayOfWeek getDayOfWeek() {
            return start.getDayOfWeek();
        }

        public Duration getDuration() {
            return duration;
        }

        public Node getView() {
            return view;
        }

    }


    public static void main(String[] args) {
        CreateXmlFile.create();

        launch(args);

    }
}