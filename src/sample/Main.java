package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
            primaryStage.setTitle("VisualizeRBT");
            primaryStage.setResizable(false);
            primaryStage.setScene(new Scene(root, 600, 450));
        }
        catch (NullPointerException e){
            e.getMessage();
            System.out.println(e);
        }
        primaryStage.show();

    }



    public static void main(String[] args) {
        launch(args);
    }
}
