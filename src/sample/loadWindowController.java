package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class loadWindowController implements Initializable {

    //Load window

    @FXML
    private Pane loadWindowPane;

    @FXML
    private Button loadWindowButtonOpen;

    @FXML
    private Button loadWindowButtonLoad;

    @FXML
    private Text loadWindowPath;



    public loadWindowController(){}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadWindowButtonOpen.setOnAction(this::openFileExploer);
    }


    private void openFileExploer(ActionEvent actionEvent){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage = (Stage) loadWindowPane.getScene().getWindow();
        File directory = directoryChooser.showDialog(stage);
        if (directory != null) {
            String path = directory.getAbsolutePath();
            if(path.length() >= 37){path = path.substring(0,36).concat("...");}
            loadWindowPath.setText(path);
            loadWindowButtonLoad.setDisable(false);
        }
    }

    public void setErrorLoading(){
        loadWindowPath.setText("Error - is path correct?");;
    }

    public void setLoadWindowButtonOpen(Button loadWindowButtonOpen) {
        this.loadWindowButtonOpen = loadWindowButtonOpen;
    }

    public Text getLoadWindowPath() {
        return loadWindowPath;
    }

    public void setLoadWindowPath(Text loadWindowPath) {
        this.loadWindowPath = loadWindowPath;
    }
}
