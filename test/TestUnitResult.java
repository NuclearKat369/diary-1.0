import javafx.application.Platform;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestUnitResult {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestCreateXml.class, TestAppStart.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("Тесты успешны: " + result.wasSuccessful());
        Platform.exit(); // Закрывает приложение после теста, чтобы не делать этого вручную
    }
}