package at.fhv.td.gui;

import at.fhv.td.Main;
import at.fhv.td.rmi.interfaces.IClientSession;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class LoginViewGuiController implements Initializable {

    private SimpleBooleanProperty valid = new SimpleBooleanProperty(true);
    private SimpleBooleanProperty messageVisible = new SimpleBooleanProperty(false);

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button login;

    @FXML
    private Label message;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        login.disableProperty().bind(valid);
        message.visibleProperty().bind(messageVisible);
    }

    @FXML
    public void checkAccept() {
        valid.set(!(!username.getText().equals("") && !password.getText().equals("")));
    }

    @FXML
    public void loginButtonClicked() throws RemoteException {
        messageVisible.set(false);
        IClientSession session = Main.getSessionFactory().login(username.getText(), password.getText(), false);

        if (session != null) {
            Main.setSession(session);
            Main.setUserName(username.getText());
            UI.changeScene("/fxml_files/listEvents.fxml");
        }

        messageVisible.set(true);
    }
}
