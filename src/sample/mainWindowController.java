package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ResourceBundle;

public class mainWindowController implements Initializable {

    @FXML
    private ListView mainWindowList;

    @FXML
    private GridPane mainWindowGrid;

    @FXML
    private Text mainWindowResult;

    @FXML
    private Button mainWindowButtonRun;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //mainWindowButtonRun.setOnAction(this::runSelected);
    }


    private void runSelected(ActionEvent actionEvent){

    }



}
