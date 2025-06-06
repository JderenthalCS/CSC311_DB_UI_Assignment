package viewmodel;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.prefs.Preferences;


public class LoginController {


    @FXML
    private GridPane rootpane;

    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordField;
    public void initialize() {
        rootpane.setBackground(new Background(
                        createImage("https://edencoding.com/wp-content/uploads/2021/03/layer_06_1920x1080.png"),
                        null,
                        null,
                        null,
                        null,
                        null
                )
        );


        rootpane.setOpacity(0);
        FadeTransition fadeOut2 = new FadeTransition(Duration.seconds(10), rootpane);
        fadeOut2.setFromValue(0);
        fadeOut2.setToValue(1);
        fadeOut2.play();
    }
    private static BackgroundImage createImage(String url) {
        return new BackgroundImage(
                new Image(url),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                new BackgroundPosition(Side.LEFT, 0, true, Side.BOTTOM, 0, true),
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true));
    }
    @FXML
    public void login(ActionEvent actionEvent) {
        String username = usernameTextField.getText();
        String password = passwordField.getText();

        Preferences prefs = Preferences.userRoot().node("login");
        String storedUsername = prefs.get("USERNAME", "");
        String storedPassword = prefs.get("PASSWORD", "");

        if (username.equals(storedUsername) && password.equals(storedPassword)) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/view/db_interface_gui.fxml"));
                Scene scene = new Scene(root, 990, 602);
                scene.getStylesheets().add(getClass().getResource("/css/lightTheme.css").toExternalForm());
                Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Incorrect username or password!");
            alert.showAndWait();
        }
    }
    @FXML
    private void signUp(ActionEvent event) {
        try {
            // Debug resource paths
            System.out.println("Working directory: " + System.getProperty("user.dir"));
            System.out.println("Looking for CSS at: /css/signup.css");
            URL cssUrl = getClass().getResource("/css/signup.css");
            System.out.println("CSS URL: " + (cssUrl != null ? cssUrl.toString() : "NOT FOUND"));

            // Rest of your code...
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/signUp.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
                System.out.println("Added CSS to scene");
            } else {
                System.out.println("Could not add CSS - resource not found");
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
