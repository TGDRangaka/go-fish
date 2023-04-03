package lk.ijse.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowFormController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private JFXButton btnCrewManage;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Node node = FXMLLoader.load(getClass().getResource("/view/dashboard_form.fxml"));
        root.getChildren().setAll(node);
    }

    @FXML
    void btnDashboardOnAction(ActionEvent event) throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("/view/dashboard_form.fxml"));
        root.getChildren().setAll(node);
    }

    @FXML
    void btnCrewManageOnAction(ActionEvent event) throws IOException {
       Node node = FXMLLoader.load(getClass().getResource("/view/crew_manage_form.fxml"));
       root.getChildren().setAll(node);
    }

    @FXML
    void btnCatchManageOnAction(ActionEvent event) throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("/view/catch_manage_form.fxml"));
        root.getChildren().setAll(node);
    }
}
