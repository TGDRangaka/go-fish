package lk.ijse.Controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.Model.AdminModel;
import lombok.SneakyThrows;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginFormController implements Initializable {
    @FXML
    private JFXTextField txtPasswordValue;

    @FXML
    private ImageView imgEyeOpen;

    @FXML
    private ImageView imgEyeClose;

    @FXML
    private JFXTextField txtUserName;

    @FXML
    private JFXPasswordField txtPassword;

    public static String user = "";

    private Stage mainStage;

    @SneakyThrows
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadFieldsOnActions();

    }

    private void loadMainWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/main_window_form.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Go Fish");
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setScene(scene);
        mainStage = stage;
    }

    private void loadFieldsOnActions() {
        txtUserName.setOnAction((e) -> {
            txtPassword.requestFocus();
        });
        txtPassword.setOnAction((e) -> {
            btnLoginOnAction(e);
        });
        txtPasswordValue.setOnAction((e) -> {
            btnLoginOnAction(e);
        });

        txtPasswordValue.textProperty().bindBidirectional(txtPassword.textProperty());
        imgEyeClose.setOnMouseClicked((e) -> {
            imgEyeClose.setVisible(!imgEyeClose.isVisible());
            imgEyeOpen.setVisible(true);

            txtPassword.setVisible(false);
        });

        imgEyeOpen.setOnMouseClicked((e) -> {
            imgEyeOpen.setVisible(!imgEyeOpen.isVisible());
            imgEyeClose.setVisible(true);

            txtPassword.setVisible(true);
        });
        txtUserName.requestFocus();
    }

    @FXML
    void btnLoginOnAction(ActionEvent event) {
        String username = txtUserName.getText();
        String password = txtPassword.getText();

        try {
            boolean isUserVerified = AdminModel.userVerify(username, password);

            if(isUserVerified){
                user = AdminModel.getName(username, password);
                String title = "Congradulation";
                String message = "You've successfully login to the system.";
                TrayNotification tray = new TrayNotification(title, message, NotificationType.SUCCESS);
                tray.showAndDismiss(new Duration(3000));

                Stage stage = (Stage) txtUserName.getScene().getWindow();
                stage.close();
                loadMainWindow();
                mainStage.show();
            }else {
//                new Alert(Alert.AlertType.WARNING, "User name or password is wrong!!").show();

                String title = "WARNING";
                String message = "User name or password is wrong!!";
                TrayNotification tray = new TrayNotification(title, message, NotificationType.WARNING);
                tray.showAndDismiss(new Duration(3000));
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
//            new Alert(Alert.AlertType.ERROR, "Oops... Something went wrong!!!").show();

            String title = "ERROR";
            String message = "Oops... Something went wrong!!!";
            TrayNotification tray = new TrayNotification(title, message, NotificationType.ERROR);
            tray.showAndDismiss(new Duration(3000));
        }
    }

}
