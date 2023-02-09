package src;



import java.io.IOException;
import java.text.ParseException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException, ParseException {

        Scene scene = new Scene(new Controller(), 750, 500);

        primaryStage.setTitle("Stages View");
        primaryStage.setScene(scene);
       // primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) throws IOException, ParseException {

        launch(args);
    }

}
