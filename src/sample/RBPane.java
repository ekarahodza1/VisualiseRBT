package sample;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class RBPane  extends Pane {
    private RedBlackTree<Integer> tree;
    private double radius = 20;
    private double vGap = 50;


    RBPane(RedBlackTree<Integer> tree){
        this.tree = tree;
        setStatus("Stablo je prazno");
        setBackground(new Background(new BackgroundFill(Color.web("#" + "F1F3F7"), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void setStatus(String msg){
        Text t = new Text(20, 30, msg);
        t.setFont(Font.font ("Palatino Linotype", 20));

        getChildren().add(t);
    }


    //Override
    public void displayTree(){
        this.getChildren().clear();
        if(tree.getRoot() != null){
            displayTree(tree.getRoot(), getWidth() / 2, vGap, getWidth() / 4, false);
        }
    }

    public void displaySearchTree(){
        this.getChildren().clear();
        if(tree.getRoot() != null){
            displayTree(tree.getRoot(), getWidth() / 2, vGap, getWidth() / 4, true);
        }
    }

    private void displayTree(Node<Integer> root, double x, double y, double hGap, boolean search){
        if(root.left != null){
            getChildren().add(new Line(x - hGap, y + vGap, x, y));
            displayTree(root.left, x - hGap, y + vGap, hGap / 2, search);
        }

        if (root.right != null){
            getChildren().add(new Line(x + hGap, y + vGap, x, y));
            displayTree(root.right, x + hGap, y + vGap, hGap / 2, search);
        }

        tree.getRed(root);

        Circle circle = new Circle(x, y, radius);
        circle.setStroke(Color.BLACK);

        if (search && tree.getSearchArray().contains(root.element)){
            circle.setFill(Color.YELLOW);
        }
        else if(tree.getRed(root))
            circle.setFill(Color.RED);
        else circle.setFill(Color.GRAY);
        getChildren().addAll(circle, new Text(x - 4, y + 4, root.element + ""));
    }
}