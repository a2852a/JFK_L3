package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class mainWindowController implements Initializable {

    @FXML
    private ListView mainWindowList;

    @FXML
    private Text mainWindowResult;

    @FXML
    private Button mainWindowButtonRun;

    @FXML
    private TextField mainWindowPar1;

    @FXML
    private TextField mainWindowPar2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainWindowButtonRun.setOnAction(this::runSelected);
        setupListView();
    }


    private void runSelected(ActionEvent actionEvent) {
        String par1 = mainWindowPar1.getText();
        String par2 = mainWindowPar2.getText();

        Object[] args = new Object[]{par1, par2};
        Object selectedValue = mainWindowList.getSelectionModel().getSelectedItem();

        String result = null;
        try {
            result = ModuleLoader.getInstance().invokeMethod(selectedValue, args);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (result == null) mainWindowResult.setText("ERROR");
            else mainWindowResult.setText(result);
        }

    }


    private void setupListView() {
        ObservableList<String> items = FXCollections.observableArrayList(
                ModuleLoader.getInstance().getLoadedMethodNames()
        );
        mainWindowList.setItems(items);
        mainWindowList.getSelectionModel().clearSelection();

        mainWindowList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                mainWindowPar1.clear();
                mainWindowPar2.clear();
                int parCount = ModuleLoader.getInstance().getMethodMap().get(newValue).getParameterCount();
                switch (parCount) {
                    case 0: {
                        inputControl(false, false);
                        break;
                    }
                    case 1: {
                        inputControl(true, false);
                        break;
                    }
                    case 2: {
                        inputControl(true, true);
                        break;
                    }
                }
            }
        });
    }

    private void inputControl(boolean var1, boolean var2) {
        if (var1) mainWindowPar1.setDisable(false);
        else mainWindowPar1.setDisable(true);

        if (var2) mainWindowPar2.setDisable(false);
        else mainWindowPar2.setDisable(true);

    }


}
