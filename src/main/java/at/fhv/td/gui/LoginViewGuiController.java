package at.fhv.td.gui;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginViewGuiController implements Initializable {

    private SimpleBooleanProperty valid = new SimpleBooleanProperty(true);

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private Button login;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        login.disableProperty().bind(valid);
    }

    @FXML
    public void checkAccept() {
        valid.set(!((!username.getText().equals("")) && (!password.getText().equals(""))));
    }

    @FXML
    public void loginButtonClicked() {
        //TODO: Call method to check username and password
        UI.changeScene("/fxml_files/listEvents.fxml");
    }
}
