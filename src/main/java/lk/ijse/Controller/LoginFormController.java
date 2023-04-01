package lk.ijse.Controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import lk.ijse.Launcher;
import lk.ijse.Model.AdminModel;
import lk.ijse.util.CrudUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController {

    @FXML
    private JFXTextField txtPassword;

    @FXML
    private JFXTextField txtUserName;

    @FXML
    void btnLoginOnAction(ActionEvent event) {
        String username = txtUserName.getText();
        String password = txtPassword.getText();

        try {

            boolean isUserVerified = AdminModel.userVerify(username, password);

            if (isUserVerified){
                Launcher.stage.close();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/main_window_form.fxml"));
                Scene scene = new Scene(fxmlLoader.load());

                Stage primaryStage = new Stage();
                primaryStage.setTitle("Go Fish");
                primaryStage.setScene(scene);
                primaryStage.setResizable(false);
                primaryStage.centerOnScreen();
                primaryStage.show();
            }else{
                new Alert(Alert.AlertType.WARNING, "wrong username and password!!!").show();
            }

        } catch (SQLException | IOException e) {
            new Alert(Alert.AlertType.ERROR, "Ops something went wrong!!!");
        }
    }

}
