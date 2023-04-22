package lk.ijse.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
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
import javafx.scene.shape.Line;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MainWindowFormController implements Initializable {
    @FXML
    private Label lblUserName;
    @FXML
    private AnchorPane root;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTime;

    @FXML
    private JFXButton btnCrewManage;

    @FXML
    private Line lineDashboard;

    @FXML
    private Line lineCrewManage;

    @FXML
    private Line lineCatchManage;

    @FXML
    private Line lineFishManage;

    @FXML
    private Line lineViewAnalystics;

    @FXML
    private Line lineSendMails;

    private Line[] lines = new Line[6];

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Node node = FXMLLoader.load(getClass().getResource("/view/dashboard_form.fxml"));
        root.getChildren().setAll(node);

        new Thread(){
            public void run() {
                Platform.runLater(() -> {
                    try {
                        loadWindows();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }.start();


        lblUserName.setText(LoginFormController.user);
        setTime();

        lines = new Line[]{lineDashboard, lineCrewManage, lineCatchManage, lineFishManage, lineViewAnalystics, lineSendMails};
    }

    private void loadWindows() throws IOException {
        String[] ar = {
                "dashboard_form.fxml",
                "crew_registration_form.fxml",
                "crew_manage_form.fxml",
                "catch_manage_form.fxml",
                "catch_record_form.fxml",
                "fish_manage_form.fxml",
                "view_analystic_form.fxml",
                "send_mails_form.fxml"
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

    private void setTime() {
        new Thread() {
            @Override
            public void run() {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        LocalDate currentDate = LocalDate.now();
                        LocalTime currentTime = LocalTime.now();
                        Platform.runLater(() -> {
                            lblTime.setText(currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                            lblDate.setText(currentDate.format(DateTimeFormatter.ofPattern("yyyy-MMM-dd")));
                        });
                    }
                }, 0, 1000);
            }
        }.start();
    }

    @FXML
    void btnDashboardOnAction(ActionEvent event) throws IOException {
        showSelectedBtn(0);
        lblTitle.setText("Dashboard");
        Node node = FXMLLoader.load(getClass().getResource("/view/dashboard_form.fxml"));
        root.getChildren().setAll(node);
    }

    @FXML
    void btnCrewManageOnAction(ActionEvent event) throws IOException {
        showSelectedBtn(1);
        lblTitle.setText("Crew Manage");
       Node node = FXMLLoader.load(getClass().getResource("/view/crew_manage_form.fxml"));
       root.getChildren().setAll(node);
    }

    @FXML
    void btnCatchManageOnAction(ActionEvent event) throws IOException {
        showSelectedBtn(2);
        lblTitle.setText("Catch Manage");
        Node node = FXMLLoader.load(getClass().getResource("/view/catch_manage_form.fxml"));
        root.getChildren().setAll(node);
    }

    @FXML
    void btnFishManageOnAction(ActionEvent event) throws IOException {
        showSelectedBtn(3);
        lblTitle.setText("Fish Manage");
        Node node = FXMLLoader.load(getClass().getResource("/view/fish_manage_form.fxml"));
        root.getChildren().setAll(node);
    }


    @FXML
    void btnViewAnalysticOnAction(ActionEvent event) throws SQLException, IOException {
        showSelectedBtn(4);
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
        showSelectedBtn(5);
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

    private void showSelectedBtn(int btn){
        for(int i = 0; i < lines.length; i++){
            if(btn == i){
                lines[i].setVisible(true);
            }else{
                lines[i].setVisible(false);
            }

        }
    }
}
