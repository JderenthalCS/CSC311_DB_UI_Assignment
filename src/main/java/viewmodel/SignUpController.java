package viewmodel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.prefs.Preferences;


public class SignUpController {

    @FXML TextField usernameTextField;
    @FXML PasswordField passwordField;
    @FXML private GridPane rootpane;

    public void createNewAccount(ActionEvent actionEvent) {
        String username = usernameTextField.getText();
        String password = passwordField.getText();

        if(username.isEmpty() || password.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Username and Password must not be empty");
            alert.showAndWait();
            return;
        }

        Preferences prefs = Preferences.userRoot().node("login");
        prefs.put("USERNAME", username);
        prefs.put("PASSWORD", password);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Account Created!");
        alert.showAndWait();

        goBack(actionEvent);  //Re-direct
    }

    public void goBack(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Scene scene = new Scene(root, 1100, 750);
            scene.getStylesheets().add(getClass().getResource("/css/lightTheme.css").toExternalForm());
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
