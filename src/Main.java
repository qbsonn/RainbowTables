/**
 * Created by Kuba on 2016-12-03.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application{


    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
       this.primaryStage=primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));



        Scene scene = new Scene(root);

        primaryStage.setTitle("Generator tęczowych tablic");
       primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        primaryStage.show();
    }







    public static void main ( String args []) {

        launch(args);

    }
}
