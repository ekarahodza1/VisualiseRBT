package sample;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;

public class RBVisualize extends Application {
    private static ArrayList<Integer> nodes = new ArrayList<>();
    @Override
    public void start(Stage primaryStage){
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        BorderPane pane = new BorderPane();
        RBPane view = new RBPane(tree);
        setPane(pane, view, tree);
        setStage(pane, primaryStage, "VisualizeRBT");
    }

    public void setStage(BorderPane pane, Stage primaryStage, String title){
        Scene scene = new Scene(pane, 900,700);
        String css = this.getClass().getResource("style.css").toExternalForm();
        scene.getStylesheets().add(css);
        primaryStage.setTitle(title);
        primaryStage.setResizable(false);
        //primaryStage.getIcons().add(new Image("file:data/tree.png"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void setPane(BorderPane pane, RBPane view, RedBlackTree<Integer> tree){
        pane.setCenter(view);
        TextField textField = new TextField();
        textField.setPrefColumnCount(3);
        textField.setAlignment(Pos.BASELINE_RIGHT);
        Button insert = new Button("Dodaj");
        Button delete = new Button("Izbriši");
        Button search = new Button("Pretraga");
        addFunctionalities(textField, insert, delete, search, tree, view);
        HBox hBox = new HBox(5);
        hBox.getStyleClass().add("hbox");
        hBox.getChildren().addAll(new Label("Unesi vrijednost"), textField, insert, delete, search);
        hBox.setAlignment(Pos.BASELINE_CENTER);
        pane.setBottom(hBox);
    }

    public void addFunctionalities(TextField textField, Button insert, Button delete, Button search, RedBlackTree<Integer> tree, RBPane view){
        insert.setOnAction(e->{
            if(textField.getText().length() == 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Nije unesena vrijednost!", ButtonType.OK);
                alert.getDialogPane().setMinHeight(80);
                alert.show();
            }
            else {
                int key = Integer.parseInt(textField.getText());
                nodes.add(key);
                if (tree.search(key)) {
                    view.displayTree();
                    view.setStatus(key + " se već nalazi u stablu!");
                } else {
                    tree.insert(key);
                    view.displayTree();
                    view.setStatus(key + " je dodano!");
                }
                textField.clear();
            }
        });

        delete.setOnAction(e->{
            int key = Integer.parseInt(textField.getText());
            if(!tree.search(key)){
                view.displayTree();
                view.setStatus(key +" se ne nalazi u stablu!");
            }
            else{
                tree.delete(key);
                view.displayTree();
                view.setStatus(key+" je obrisano!");
            }
            textField.clear();
        });

        search.setOnAction(e-> {
            int key = Integer.parseInt(textField.getText());
            if(!tree.search(key)){
                view.displayTree();
                view.setStatus(key +" se ne nalazi u stablu!");
            }
            else {
                tree.search(key);
                view.displaySearchTree();
                view.setStatus("Put pretrage je: " + Arrays.toString(tree.getSearchArray().toArray()));
                textField.clear();
            }
        });
    }
}

