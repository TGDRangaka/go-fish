package lk.ijse.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.DB.DBConnection;
import lombok.SneakyThrows;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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

        String[] ar = {
                "dashboard_form.fxml",
                "crew_registration_form.fxml",
                "crew_manage_form.fxml",
                "catch_manage_form.fxml",
                "catch_record_form.fxml",
                "fish_manage_form.fxml"
//                "send_mails_form.fxml"
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
    void btnViewAnalysticOnAction(ActionEvent event) throws SQLException, IOException {
        lblTitle.setText("View Analystics");
        Node node = FXMLLoader.load(getClass().getResource("/view/view_analystic_form.fxml"));
        root.getChildren().setAll(node);

//        Connection con = DBConnection.getInstance().getConnection();
//        try {
//
//            InputStream input = new FileInputStream(new File("F:/Github/go-fish/src/main/resources/reports/test.jrxml"));
//            JasperDesign jasperDesign = JRXmlLoader.load(input);
//            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
//
//            Map<String, Object> d = new HashMap();
//            d.put("adminId", "A002");
//            d.put("id", "test test test");
//            JasperPrint fillReport = JasperFillManager.fillReport(jasperReport, d, con);
//            JasperViewer.viewReport(fillReport, false);
//
//
//        } catch (JRException | FileNotFoundException e) {
//            e.printStackTrace();
//        }

    }

    @FXML
    void btnSendMailOnAction(ActionEvent event) throws IOException {
        lblTitle.setText("Send Mails");
        Node node = FXMLLoader.load(getClass().getResource("/view/send_mails_form.fxml"));
        root.getChildren().setAll(node);
    }


    @FXML
    void btnLogOutOnAction(ActionEvent event) throws IOException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure want to Log Out?", yes, no).showAndWait();

        if(result.orElse(no) == yes) {
            Stage mainStage = (Stage) btnCrewManage.getScene().getWindow();
            mainStage.close();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/login_form.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }

    }
}
