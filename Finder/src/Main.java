import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.applet.Applet;

/**
 * Created by Kuba on 2016-12-26.
 */
public class Main extends Application{
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage=primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("FinderWindow.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("Finder");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        primaryStage.show();
    }
    public static void main(String[] args) {
        // Prints "Hello, World" to the terminal window.
        //System.out.println("Hello, World");
        launch(args);
        //Finder finder = new Finder("C:\\Users\\Piotrek\\Desktop\\heheMD5.dat");
    }


}
