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
import lk.ijse.Model.AdminModel;
import lombok.SneakyThrows;

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

    private Stage mainStage;

    @SneakyThrows
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadFieldsOnActions();

        loadMainWindow();
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
                Stage stage = (Stage) txtUserName.getScene().getWindow();
                stage.close();

                mainStage.show();
            }else {
                new Alert(Alert.AlertType.WARNING, "User name or password is wrong!!").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Oops... Something went wrong!!!").show();
        }
    }

}
