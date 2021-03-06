package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class loadWindowController implements Initializable {


    @FXML
    private Pane loadWindowPane;

    @FXML
    private Button loadWindowButtonOpen;

    @FXML
    private Button loadWindowButtonLoad;

    @FXML
    private Text loadWindowPath;

    public loadWindowController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadWindowButtonOpen.setOnAction(this::openFolderExplorer);
        loadWindowButtonLoad.setOnAction(this::loadFolder);
    }


    private void openFolderExplorer(ActionEvent actionEvent) {
        ModuleLoader loaderInstance = ModuleLoader.getInstance();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(Paths.get("").toAbsolutePath().toFile());

        Stage stage = (Stage) loadWindowPane.getScene().getWindow();
        loaderInstance.setDirectory(directoryChooser.showDialog(stage));

        if (loaderInstance.getDirectory() != null) {
            String path = loaderInstance.getDirectory().getAbsolutePath();
            if (path.length() >= 37) {
                path = path.substring(0, 36).concat("...");
            }
            loadWindowPath.setText(path);
            loadWindowButtonLoad.setDisable(false);
        }
    }


    private void loadFolder(ActionEvent actionEvent) {
        try{
        ModuleLoader loaderInstance = ModuleLoader.getInstance();
        if (loaderInstance.getDirectory() != null)
            if (loaderInstance.loadClasses()) {
                    loadMainScene();
                }
             else {
                 setNoFilesFound();
            }
    }catch(Exception|NoClassDefFoundError e){
            setErrorLoading();
    }
    }

    private void loadMainScene() throws Exception {
        Stage stage = (Stage) loadWindowPane.getScene().getWindow();
        Parent parent = FXMLLoader.load(getClass().getResource("view/main_window.fxml"));
        stage.setScene(new Scene(parent, 335, 585));
        stage.setResizable(false);
    }


    private void setErrorLoading() {
        loadWindowPath.setText("Error - is path correct?");
        loadWindowButtonLoad.setDisable(false);
    }

    private void setNoFilesFound(){
        loadWindowPath.setText("Error - No files where found");
        loadWindowButtonLoad.setDisable(false);
    }

}
