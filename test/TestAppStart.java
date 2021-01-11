import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;

import org.junit.Test;
import org.xml.sax.SAXException;
import sample.Main;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

public class TestAppStart {

    @Test
    public void testAppStart() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                new JFXPanel(); // Запускает платформу JavaFx
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            new Main().start(new Stage()); // Запуск приложения
                            System.out.println("TestAppStart выполнен");
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
            }
        });
        thread.start(); // Запускает поток
        Thread.sleep(1000); // Задерживает поток на некоторое время для проведения теста
    }
}