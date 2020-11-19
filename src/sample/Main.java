package sample;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Main extends Application {

    private final LocalTime firstSlotStart = LocalTime.of(0, 0);
    private final Duration slotLength = Duration.ofMinutes(30);
    private final LocalTime lastSlotStart = LocalTime.of(23, 59);

    private static LocalDateTime cal;
    Dates dates = new Dates();


//    private static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");

    private final List<TimeSlot> timeSlots = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws IOException {



        TabPane tp = new TabPane();
        tp.setLayoutX(10);
        tp.setLayoutY(10);
        tp.setCursor(Cursor.HAND);
        DropShadow effect=new DropShadow();
        effect.setOffsetX(8);
        effect.setOffsetY(8);
        tp.setEffect(effect);
        tp.setStyle("-fx-border-width:4pt;-fx-border-color:lightblue;");
        tp.setPrefSize(300, 300);
        tp.setTooltip(new Tooltip("Панель с вкладками"));
        tp.setSide(Side.TOP);
        tp.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tp.setTabMinHeight(20);
        tp.setTabMinWidth(100);


// Вкладка №1
        Tab tabGrid = new Tab("Расписание");
        Group rootGrid = new Group();
        tabGrid.setContent(rootGrid);

        GridPane calendarView = new GridPane();
        calendarView.setGridLinesVisible(true);

        ObjectProperty<TimeSlot> mouseAnchor = new SimpleObjectProperty<>();

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1) ;
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        for (LocalDate date = startOfWeek; ! date.isAfter(endOfWeek); date = date.plusDays(1)) {
            int slotIndex = 1 ;

            for (LocalDateTime startTime = date.atTime(firstSlotStart);
                 ! startTime.isAfter(date.atTime(lastSlotStart));
                 startTime = startTime.plus(slotLength)) {


                TimeSlot timeSlot = new TimeSlot(startTime, slotLength);
                timeSlots.add(timeSlot);

                registerDragHandlers(timeSlot, mouseAnchor);

                calendarView.add(timeSlot.getView(), timeSlot.getDayOfWeek().getValue(), slotIndex);
                slotIndex++ ;
            }
        }

        //headers:
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("E\nMMM d");

        for (LocalDate date = startOfWeek; ! date.isAfter(endOfWeek); date = date.plusDays(1)) {
            Label label = new Label(date.format(dayFormatter));
            label.setPadding(new Insets(1));
            label.setTextAlignment(TextAlignment.CENTER);
            GridPane.setHalignment(label, HPos.CENTER);
            calendarView.add(label, date.getDayOfWeek().getValue(), 0);
        }

        int slotIndex = 1 ;
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        for (LocalDateTime startTime = today.atTime(firstSlotStart);
             ! startTime.isAfter(today.atTime(lastSlotStart));
             startTime = startTime.plus(slotLength)) {
            Label label = new Label(startTime.format(timeFormatter));
            label.setPadding(new Insets(2));
            GridPane.setHalignment(label, HPos.RIGHT);
            calendarView.add(label, 0, slotIndex);
            slotIndex++ ;
        }
        ScrollPane scroller = new ScrollPane(calendarView);
        rootGrid.getChildren().add(scroller);


// Вкладка №2
        Tab tabCal = new Tab("Календарь");
        Group rootCal = new Group();
        tabCal.setContent(rootCal);

        GridPane t = new GridPane();
        t.setGridLinesVisible(true);

        cal = LocalDateTime.now();
        dates.setYear(cal.getYear());

        String str = "Год: " + dates.getYear() + "\n";
        Label label = new Label(str);
        label.setPadding(new Insets(1));
        label.setTextAlignment(TextAlignment.CENTER);
        GridPane.setHalignment(label, HPos.CENTER);
        int colIndex = 0;
        int rowIndex = 0;

        t.add(label, colIndex, rowIndex);
        rowIndex++;
        int colIndexMonth = 0;
        int rowIndexMonth = 2;
        for (int month = 1; month <= 12; month++) {


//            if (statCal.isLeapYear(statCal.YEAR) && month == 1) {     //проверяет год на високосность
//                days[month] = 29;
//            }

                //month name
                dates.setMonth(cal.withMonth(month).getMonth().getDisplayName(TextStyle.FULL, Locale.UK));
                dates.setMonthNumber(month);
                str = dates.getMonth();
                label = new Label(str);
                label.setPadding(new Insets(1));
                label.setTextAlignment(TextAlignment.CENTER);
                GridPane.setHalignment(label, HPos.LEFT);
            if (month % 3 != 0) {
                t.add(label, colIndexMonth, rowIndexMonth);
                colIndexMonth++;
            }
            else {
                t.add(label, colIndexMonth, rowIndexMonth);
                colIndexMonth = 0;
                rowIndexMonth +=3;
            }

            System.out.println("Row: " + rowIndex + " Column: " + colIndex);
            rowIndex = rowIndexMonth+1;
            if (colIndexMonth != 0) {
                colIndex = colIndexMonth-1;

            }
            else {
                colIndex = colIndexMonth + 2;
                rowIndex = rowIndexMonth-2;
            }

            GridPane dow = new GridPane();
            dow.setGridLinesVisible(true);

            for (int i = 0; i < 7; i++){
                int dowColInd = i;
                Calendar temp = Calendar.getInstance();
                temp.set(Calendar.DAY_OF_WEEK, i+2);
                label = new Label(temp.getDisplayName(temp.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH));
                label.setPadding(new Insets(1));
                label.setTextAlignment(TextAlignment.CENTER);
                if (dowColInd > 4) label.setTextFill(Color.RED);
                GridPane.setHalignment(label, HPos.LEFT);
                dow.getColumnConstraints().add(new ColumnConstraints(30));
                dow.add(label, dowColInd, 0);
            }
            t.add(dow, colIndex, rowIndex);

            rowIndex++;

            System.out.println("Row: " + rowIndex + " Column: " + colIndex);

            System.out.println("_____________________________" + "\n" +
                    " Mon Tue Wed Thu Fri Sat Sun");

            //days of month grid in the main grid
            GridPane datesTable = new GridPane();
            datesTable.setGridLinesVisible(true);

            for (int w = 0; w < 7; w++){
                datesTable.getColumnConstraints().add(new ColumnConstraints(30));
            }

            int colIndexDates;
            int rowIndexDates = 0;
            String[] calendarTable = new String[42];

            for (int n = 0; n < calendarTable.length; n++) {


                n += cal.withMonth(month).withDayOfMonth(1).getDayOfWeek().getValue() - 1;

                dates.setDayOfWeek(cal.withMonth(dates.getMonthNumber()).withDayOfMonth(1).getDayOfWeek().toString());

                colIndexDates = n;

                System.out.println("DoW: " + dates.getDayOfWeek());

                for (int s = 0; s < n; s++) {
                    calendarTable[s] = " ";
                    System.out.printf("%4s", calendarTable[s]);
                }

                YearMonth yearMonth = YearMonth.of(dates.getYear(), dates.getMonthNumber());

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
                        if (colIndexDates > 4) label.setTextFill(Color.RED);
                        //todo set the bg for "today"
                        if (dates.getYear() == LocalDateTime.now().getYear() &
                                dates.getMonth().equalsIgnoreCase(LocalDateTime.now().getMonth().toString()) == true &
                                dates.getDay() == LocalDateTime.now().getDayOfMonth())
                            label.setBackground(new Background
                                    (new BackgroundFill
                                            (Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                        datesTable.add(label, colIndexDates, rowIndexDates);
                        colIndexDates = 0;
                        rowIndexDates++;
                        System.out.printf("%4d", dates.getDay());
                        System.out.println();
                    } else {
                        str = Integer.toString(dates.getDay());
                        label = new Label(str);
                        label.setPadding(new Insets(1));
                        label.setTextAlignment(TextAlignment.CENTER);
                        GridPane.setHalignment(label, HPos.LEFT);
                        if (colIndexDates > 4) label.setTextFill(Color.RED);
                        //todo set the bg for "today"
                        if (dates.getYear() == LocalDateTime.now().getYear() &
                                dates.getMonth().equalsIgnoreCase(LocalDateTime.now().getMonth().toString()) == true &
                                dates.getDay() == LocalDateTime.now().getDayOfMonth())
                            label.setBackground(new Background
                                    (new BackgroundFill
                                            (Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                        datesTable.add(label, colIndexDates, rowIndexDates);
                        colIndexDates++;
                        System.out.printf("%4d", dates.getDay());
                    }
                }
                System.out.println();
                break;
            }

            System.out.println("CHECK YEAR: " + dates.getYear() + "\n" + "CHECK MONTH: " +
                    dates.getMonth() + "\n" + "CHECK DAY: " + dates.getDay() +
                    LocalDateTime.now().getMonth());
            System.out.println("Row: " + rowIndex + " Column: " + colIndex);
            datesTable.setAlignment(Pos.TOP_LEFT);
            t.add(datesTable, colIndex, rowIndex);
        }

        rootCal.getChildren().add(t);

        tp.getTabs().addAll(tabGrid, tabCal);


//        tp.getTabs().addAll(tabGrid);

        Scene scene = new Scene(tp);
//        scene.getStylesheets().add(getClass().getResource("calendar-view.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    // Registers handlers on the time slot to manage selecting a range of slots in the grid.

    private void registerDragHandlers(TimeSlot timeSlot, ObjectProperty<TimeSlot> mouseAnchor) {
        timeSlot.getView().setOnDragDetected(event -> {
            mouseAnchor.set(timeSlot);
            timeSlot.getView().startFullDrag();
            timeSlots.forEach(slot ->
                    slot.setSelected(slot == timeSlot));
        });

        timeSlot.getView().setOnMouseDragEntered(event -> {
            TimeSlot startSlot = mouseAnchor.get();
            timeSlots.forEach(slot ->
                    slot.setSelected(isBetween(slot, startSlot, timeSlot)));
        });

        timeSlot.getView().setOnMouseReleased(event -> mouseAnchor.set(null));
    }

    // Utility method that checks if testSlot is "between" startSlot and endSlot
    // Here "between" means in the visual sense in the grid: i.e. does the time slot
    // lie in the smallest rectangle in the grid containing startSlot and endSlot
    //
    // Note that start slot may be "after" end slot (either in terms of day, or time, or both).

    // The strategy is to test if the day for testSlot is between that for startSlot and endSlot,
    // and to test if the time for testSlot is between that for startSlot and endSlot,
    // and return true if and only if both of those hold.

    // Finally we note that x <= y <= z or z <= y <= x if and only if (y-x)*(z-y) >= 0.

    private boolean isBetween(TimeSlot testSlot, TimeSlot startSlot, TimeSlot endSlot) {

        boolean daysBetween = testSlot.getDayOfWeek().compareTo(startSlot.getDayOfWeek())
                * endSlot.getDayOfWeek().compareTo(testSlot.getDayOfWeek()) >= 0 ;

        boolean timesBetween = testSlot.getTime().compareTo(startSlot.getTime())
                * endSlot.getTime().compareTo(testSlot.getTime()) >= 0 ;

        return daysBetween && timesBetween ;
    }

    // Class representing a time interval, or "Time Slot", along with a view.
    // View is just represented by a region with minimum size, and style class.

    // Has a selected property just to represent selection.

    public static class TimeSlot {

        private final LocalDateTime start ;
        private final Duration duration ;
        private final Region view ;

        private final BooleanProperty selected = new SimpleBooleanProperty();

        public final BooleanProperty selectedProperty() {
            return selected ;
        }

        public final boolean isSelected() {
            return selectedProperty().get();
        }

        public final void setSelected(boolean selected) {
            selectedProperty().set(selected);
        }

        public TimeSlot(LocalDateTime start, Duration duration) {
            this.start = start ;
            this.duration = duration ;

            view = new Region();
            view.setMinSize(80, 20);
            view.getStyleClass().add("time-slot");

//            selectedProperty().addListener((obs, wasSelected, isSelected) ->
//                    view.pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, isSelected));

        }

        public LocalDateTime getStart() {
            return start ;
        }

        public LocalTime getTime() {
            return start.toLocalTime() ;
        }

        public DayOfWeek getDayOfWeek() {
            return start.getDayOfWeek() ;
        }

        public Duration getDuration() {
            return duration ;
        }

        public Node getView() {
            return view;
        }

    }


    public static void main(String[] args) {
        launch(args);
    }

//    public static class Console extends OutputStream {
//
//        private TextArea output;
//
//        public Console(TextArea ta) {
//            this.output = ta;
//        }
//
//        @Override
//        public void write(int i) throws IOException {
//            output.appendText(String.valueOf((char) i));
//        }
//    }


}