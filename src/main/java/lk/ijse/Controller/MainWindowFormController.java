package lk.ijse.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowFormController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private Label lblTitle;

    @FXML
    private JFXButton btnCrewManage;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Node node = FXMLLoader.load(getClass().getResource("/view/dashboard_form.fxml"));
        root.getChildren().setAll(node);

//        btnCrewManage.fire();
//        btnCatchManageOnAction(new ActionEvent());
//        btnFishManageOnAction(new ActionEvent());
//        btnViewAnalysticOnAction(new ActionEvent());
//        btnSendMailOnAction(new ActionEvent());
//        btnDashboardOnAction(new ActionEvent());

        String[] ar = {
                "crew_registration_form.fxml",
                "crew_manage_form.fxml",
                "catch_manage_form.fxml",
                "catch_record_form.fxml",
                "fish_manage_form.fxml"
        };

        for(String s : ar) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/" + s));
            Scene scene = new Scene(fxmlLoader.load());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.close();
        }


    }

    @FXML
    void btnDashboardOnAction(ActionEvent event) throws IOException {
        lblTitle.setText("Dashboard");
        Node node = FXMLLoader.load(getClass().getResource("/view/dashboard_form.fxml"));
        root.getChildren().setAll(node);
    }

    @FXML
    void btnCrewManageOnAction(ActionEvent event) throws IOException {
        lblTitle.setText("Crew Manage");
       Node node = FXMLLoader.load(getClass().getResource("/view/crew_manage_form.fxml"));
       root.getChildren().setAll(node);
    }

    @FXML
    void btnCatchManageOnAction(ActionEvent event) throws IOException {
        lblTitle.setText("Catch Manage");
        Node node = FXMLLoader.load(getClass().getResource("/view/catch_manage_form.fxml"));
        root.getChildren().setAll(node);
    }

    @FXML
    void btnFishManageOnAction(ActionEvent event) throws IOException {
        lblTitle.setText("Fish Manage");
        Node node = FXMLLoader.load(getClass().getResource("/view/fish_manage_form.fxml"));
        root.getChildren().setAll(node);
    }


    @FXML
    void btnViewAnalysticOnAction(ActionEvent event) {
        lblTitle.setText("View Analystics");

    }

    @FXML
    void btnSendMailOnAction(ActionEvent event) throws IOException {
        lblTitle.setText("Send Mails");
        Node node = FXMLLoader.load(getClass().getResource("/view/send_mails_form.fxml"));
        root.getChildren().setAll(node);
    }


    @FXML
    void btnLogOutOnAction(ActionEvent event) {
        lblTitle.setText("asdf");

    }
    public void setLblTitle(String title){
        lblTitle.setText(title);
    }
}
