package viewmodel;

import dao.DbConnectivityClass;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Person;
import service.MyLogger;
import javafx.print.PrinterJob;


import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class DB_GUI_Controller implements Initializable {

    @FXML
    TextField first_name, last_name, department, email, imageURL;
    @FXML
    ImageView img_view;
    @FXML
    MenuBar menuBar;
    @FXML
    private TableView<Person> tv;
    @FXML
    private TableColumn<Person, Integer> tv_id;
    @FXML
    private TableColumn<Person, String> tv_fn, tv_ln, tv_department, tv_major, tv_email;
    private final DbConnectivityClass cnUtil = new DbConnectivityClass();
    private final ObservableList<Person> data = cnUtil.getData();

    @FXML private Button editButton;

    @FXML private Button deleteButton;

    @FXML private Button addBtn;

    @FXML private ComboBox<Major> majorComboBox;

    @FXML private Label statusLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            tv_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            tv_fn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            tv_ln.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            tv_department.setCellValueFactory(new PropertyValueFactory<>("department"));
            tv_major.setCellValueFactory(new PropertyValueFactory<>("major"));
            tv_email.setCellValueFactory(new PropertyValueFactory<>("email"));
            tv.setItems(data);

            editButton.setDisable(true); // Disable the Edit button
            deleteButton.setDisable(true); // Disable the Delete button
            addBtn.setDisable(true); // Disable the Add Button

            // Key Listener to disable/enable button
            tv.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
                boolean disable = (newSelection == null);
                editButton.setDisable(disable);
                deleteButton.setDisable(disable);
            });

            // 'Add' Listeners
            first_name.textProperty().addListener((obs, oldSelection, newSelection)-> validateForm());
            last_name.textProperty().addListener((obs, oldText, newText) -> validateForm());
            department.textProperty().addListener((obs, oldText, newText) -> validateForm());
            email.textProperty().addListener((obs, oldText, newText) -> validateForm());
            imageURL.textProperty().addListener((obs, oldText, newText) -> validateForm());

            majorComboBox.setItems(FXCollections.observableArrayList(Major.values()));
            majorComboBox.getSelectionModel().selectFirst();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void addNewRecord() {

            Person p = new Person(first_name.getText(), last_name.getText(), department.getText(),
                    majorComboBox.getValue().toString(), email.getText(), imageURL.getText());
            cnUtil.insertUser(p);
            cnUtil.retrieveId(p);
            p.setId(cnUtil.retrieveId(p));
            data.add(p);
            clearForm();
            statusLabel.setText("Record Added!");

    }

    @FXML
    protected void clearForm() {
        first_name.setText("");
        last_name.setText("");
        department.setText("");
        majorComboBox.getSelectionModel().clearSelection();
        email.setText("");
        imageURL.setText("");

        statusLabel.setText("Form Cleared!");
    }

    @FXML
    protected void logOut(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Log Out");
        alert.setHeaderText("Are you sure you want to log out?");
        alert.setContentText("You will be returned to the login screen.");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
                Scene scene = new Scene(root, 1100, 750);
                scene.getStylesheets().add(getClass().getResource("/css/lightTheme.css").toExternalForm());
                Stage window = (Stage) menuBar.getScene().getWindow();
                window.setScene(scene);
                window.show();
                statusLabel.setText("Logged out successfully.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            statusLabel.setText("Logout canceled.");
        }
    }


    @FXML
    protected void closeApplication() {
        System.exit(0);
    }

    @FXML
    protected void displayAbout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/about.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root, 600, 400);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void editRecord() {
        Person p = tv.getSelectionModel().getSelectedItem();
        int index = data.indexOf(p);
        Person p2 = new Person(index + 1, first_name.getText(), last_name.getText(), department.getText(),
                majorComboBox.getValue().toString(), email.getText(),  imageURL.getText());
        cnUtil.editUser(p.getId(), p2);
        data.remove(p);
        data.add(index, p2);
        tv.getSelectionModel().select(index);

        statusLabel.setText("Record Edited!");
    }

    @FXML
    protected void deleteRecord() {
        Person p = tv.getSelectionModel().getSelectedItem();

        if (p != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete this record?");
            alert.setContentText(p.getFirstName() + " " + p.getLastName() + " (" + p.getEmail() + ")");

            // Show and wait for user response
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // User confirmed deletion
                int index = data.indexOf(p);
                cnUtil.deleteRecord(p);
                data.remove(index);
                tv.getSelectionModel().clearSelection();
                statusLabel.setText("Record Deleted!");
            } else {
                // User canceled deletion
                statusLabel.setText("Deletion Canceled.");
            }
        }
    }


    @FXML
    protected void showImage() {
        File file = (new FileChooser()).showOpenDialog(img_view.getScene().getWindow());
        if (file != null) {
            img_view.setImage(new Image(file.toURI().toString()));
        }
    }

    @FXML
    protected void addRecord() {
        showSomeone();
    }

    @FXML
    protected void selectedItemTV(MouseEvent mouseEvent) {
        Person p = tv.getSelectionModel().getSelectedItem();
        first_name.setText(p.getFirstName());
        last_name.setText(p.getLastName());
        department.setText(p.getDepartment());
        majorComboBox.setValue(Major.valueOf(p.getMajor()));
        email.setText(p.getEmail());
        imageURL.setText(p.getImageURL());
    }

    public void lightTheme(ActionEvent actionEvent) {
        try {
            Scene scene = menuBar.getScene();
            Stage stage = (Stage) scene.getWindow();
            stage.getScene().getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/css/lightTheme.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
            System.out.println("light " + scene.getStylesheets());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void darkTheme(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            Scene scene = stage.getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/css/darkTheme.css").toExternalForm());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSomeone() {
        Dialog<Results> dialog = new Dialog<>();
        dialog.setTitle("New User");
        dialog.setHeaderText("Please specify…");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField textField1 = new TextField("Name");
        TextField textField2 = new TextField("Last Name");
        TextField textField3 = new TextField("Email ");
        ObservableList<Major> options =
                FXCollections.observableArrayList(Major.values());
        ComboBox<Major> comboBox = new ComboBox<>(options);
        comboBox.getSelectionModel().selectFirst();
        dialogPane.setContent(new VBox(8, textField1, textField2,textField3, comboBox));
        Platform.runLater(textField1::requestFocus);
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                return new Results(textField1.getText(),
                        textField2.getText(), comboBox.getValue());
            }
            return null;
        });
        Optional<Results> optionalResult = dialog.showAndWait();
        optionalResult.ifPresent((Results results) -> {
            MyLogger.makeLog(
                    results.fname + " " + results.lname + " " + results.major);
        });
    }


    private static class Results {

        String fname;
        String lname;
        Major major;

        public Results(String name, String date, Major venue) {
            this.fname = name;
            this.lname = date;
            this.major = venue;
        }
    }

    private void validateForm(){
        boolean valid = true;

        String namePattern = "^[A-Za-z]{2,25}$";
        String departmentPattern = "^[A-Za-z]{2,30}$";
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        if(!first_name.getText().matches(namePattern)){
            valid = false;
        }
        if(!last_name.getText().matches(namePattern)){
            valid = false;
        }
        if(!department.getText().matches(departmentPattern)){
            valid = false;
        }
        if(majorComboBox.getSelectionModel().isEmpty()){
            valid = false;
        }
        if(!email.getText().matches(emailPattern)){
            valid = false;
        }
        if(!imageURL.getText().trim().isEmpty()) {
            String urlPattern = "^(https?|file):.*$";
            if(!imageURL.getText().matches(urlPattern)){
                valid = false;
            }
        }

        addBtn.setDisable(!valid);

    }

    @FXML
    private void importCSV(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(menuBar.getScene().getWindow());

        if(file != null){
            try(BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null){
                    String[] fields = line.split(",");
                    if(fields.length == 6){
                       Person p = new Person(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5]);
                        cnUtil.insertUser(p);
                        p.setId(cnUtil.retrieveId(p));
                        data.add(p);
                    }
                }

                statusLabel.setText("Successfully imported CSV");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void exportCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(menuBar.getScene().getWindow());

        if (file != null) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                for (Person p : data) {
                    bw.write(p.getFirstName() + "," +
                            p.getLastName() + "," +
                            p.getDepartment() + "," +
                            p.getMajor() + "," +
                            p.getEmail() + "," +
                            p.getImageURL());
                    bw.newLine();
                }
                // ✨ AFTER exporting:
                clearForm(); // Clear all fields
                tv.getSelectionModel().clearSelection(); // Deselect in TableView
                statusLabel.setText("Data Exported — Ready for New Entry!");
            } catch (IOException e) {
                e.printStackTrace();
                statusLabel.setText("Error exporting CSV.");
            }
        }
    }

    @FXML
    private void exportPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Major Report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(menuBar.getScene().getWindow());

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("Student Enrollment Report by Major\n\n");

                int csCount = 0;
                int cpisCount = 0;
                int businessCount = 0;
                int englishCount = 0;
                int mathCount = 0;
                int biologyCount = 0;
                int chemistryCount = 0;

                for (Person p : data) {
                    String major = p.getMajor();
                    if (major.equalsIgnoreCase("CS")) {
                        csCount++;
                    } else if (major.equalsIgnoreCase("CPIS")) {
                        cpisCount++;
                    } else if (major.equalsIgnoreCase("Business")) {
                        businessCount++;
                    } else if (major.equalsIgnoreCase("English")) {
                        englishCount++;
                    } else if (major.equalsIgnoreCase("Math")) {
                        mathCount++;
                    } else if (major.equalsIgnoreCase("Biology")) {
                        biologyCount++;
                    } else if (major.equalsIgnoreCase("Chemistry")) {
                        chemistryCount++;
                    }
                }

                writer.write("CSC: " + csCount + " students\n");
                writer.write("CPIS: " + cpisCount + " students\n");
                writer.write("Business: " + businessCount + " students\n");
                writer.write("English: " + englishCount + " students\n");
                writer.write("Math: " + mathCount + " students\n");
                writer.write("Biology: " + biologyCount + " students\n");
                writer.write("Chemistry: " + chemistryCount + " students\n");

                statusLabel.setText("Major report exported successfully!");
            } catch (IOException e) {
                e.printStackTrace();
                statusLabel.setText("Failed to export major report.");
            }
        }
    }






}