package sample;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private Button rb;
    
    @FXML
    private void rbAction(){
        rb.setOnAction(e-> setStage(new RBVisualize()));
    }

    private void setStage(RBVisualize menu){
        Stage menuStage = new Stage();
        menu.start(menuStage);
        menuStage.show();
    }
}

