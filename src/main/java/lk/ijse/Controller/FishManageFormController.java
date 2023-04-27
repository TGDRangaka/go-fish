package lk.ijse.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import lk.ijse.DB.DBConnection;
import lk.ijse.Model.*;
import lk.ijse.dto.Catch;
import lk.ijse.dto.Fish;
import lk.ijse.dto.Mail;
import lk.ijse.dto.tm.FishTM;
import lk.ijse.util.CrudUtil;
import lk.ijse.util.SendMail;
import lombok.SneakyThrows;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class FishManageFormController implements Initializable {

    @FXML
    private TableView<FishTM> tableFish;

    @FXML
    private TableColumn<?, ?> colFishId;

    @FXML
    private TableColumn<?, ?> colFishType;

    @FXML
    private TableColumn<?, ?> colUnitWeight;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private TableColumn<?, ?> colAction;
    @FXML
    private Label lblFishType;

    @FXML
    private Label lblUnitWeight;

    @FXML
    private Label lblUnitPrice;

    @FXML
    private Label lblFishId;

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXTextField txtFishType;

    @FXML
    private JFXTextField txtUnitWeight;

    @FXML
    private JFXTextField txtUnitPrice;

    private ObservableList<FishTM> fishList = FXCollections.observableArrayList();

    private FishTM selectedFish = null;
    private boolean isPriceAlreadySended = false;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCellValueFactory();
        loadFishTable();
        loadNewFishId();
        setTexetFieldsOnAction();

    }

    private void setTexetFieldsOnAction() {
        txtFishType.setOnAction((e) -> {
            txtUnitWeight.requestFocus();
        });
        txtFishType.setOnKeyReleased((e) -> {
            if(!txtFishType.getText().matches("^[A-Za-z\\s'-]+$")){
                lblFishType.setText("Invalid Input!");
            }else{
                lblFishType.setText(null);
            }
        });

        txtUnitWeight.setOnAction((e) -> {
            txtUnitPrice.requestFocus();
        });
        txtUnitWeight.setOnKeyReleased((e) -> {
            if(!txtUnitWeight.getText().matches("^\\d+(\\.\\d+)?$")){
                lblUnitWeight.setText("Invalid Input!");
            }else{
                lblUnitWeight.setText(null);
            }
        });

        txtUnitPrice.setOnAction((e) -> {
            btnAdd.fire();
        });
        txtUnitPrice.setOnKeyReleased((e) -> {
            if(!txtUnitPrice.getText().matches("^\\d+(\\.\\d+)?$")){
                lblUnitPrice.setText("Invalid Input!");
            }else{
                lblUnitPrice.setText(null);
            }
        });
    }

    private void loadNewFishId() throws SQLException {
        String lastId = FishModel.getLastId();
        String newId = CrudUtil.getNewId(lastId);
        lblFishId.setText(newId);

    }

    private void loadFishTable() throws SQLException {
        List<Fish> fishes = FishModel.getAllFish();
        for(Fish fish : fishes){
            String fishId = fish.getFishId();
            String fishType = fish.getFishType();
            Double unitWeight = fish.getUnitWeight();
            Double unitPrice = fish.getUnitPrice();
            Button action = new Button("Remove");

            FishTM fishTM = new FishTM(fishId, fishType, unitWeight, unitPrice, action);
            setActionButtonOnAction(action, fishTM);

            fishList.add(fishTM);
        }
        tableFish.setItems(fishList);
    }

    private void setActionButtonOnAction(Button action, FishTM fishTM) {
        action.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are sure want to delete this fish", yes, no).showAndWait();

            if(result.orElse(no) == yes) {
                try {
                    boolean isFishDeleted = FishModel.delete(fishTM.getFishId());

                    if (isFishDeleted) {
                        fishList.removeAll(fishTM);
                        tableFish.refresh();

                        String title = "CONFIRMATION";
                        String message = "Fish Deleted Succesfully!";
                        TrayNotification tray = new TrayNotification(title, message, NotificationType.SUCCESS);
                        tray.showAndDismiss(new Duration(3000));
                        loadNewFishId();
                    } else {
                        String title = "WARNING";
                        String message = "Fish Not Deleted!!";
                        TrayNotification tray = new TrayNotification(title, message, NotificationType.WARNING);
                        tray.showAndDismiss(new Duration(3000));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    String title = "ERROR";
                    String message = "Oops Something Went Wrong!";
                    TrayNotification tray = new TrayNotification(title, message, NotificationType.ERROR);
                    tray.showAndDismiss(new Duration(3000));
                }
            }
        });
    }

    private void loadCellValueFactory() {
        colFishId.setCellValueFactory(new PropertyValueFactory<>("fishId"));
        colFishType.setCellValueFactory(new PropertyValueFactory<>("fishType"));
        colUnitWeight.setCellValueFactory(new PropertyValueFactory<>("unitWeight"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("action"));
    }

    @FXML
    void btnAddOnAction(ActionEvent event) throws SQLException {
        if(lblFishType.getText() != null && lblUnitWeight.getText() != null && lblUnitPrice.getText() != null){
            return;
        }
        String fishId = lblFishId.getText();
        String fishType = txtFishType.getText();
        Double unitWeight = Double.valueOf(txtUnitWeight.getText());
        Double unitPrice = Double.valueOf(txtUnitPrice.getText());
        Button action = new Button("Remove");

        Fish fish = new Fish(fishId, fishType, unitWeight, unitPrice);
        FishTM fishTM = new FishTM(fishId, fishType, unitWeight, unitPrice, action);

        if(btnAdd.getText().equals("Add")) {

            setActionButtonOnAction(action, fishTM);

            try {
                boolean isFishSaved = FishModel.save(fish);

                if (isFishSaved) {
                    fishList.add(fishTM);
                    tableFish.setItems(fishList);

                    String title = "CONFIRMATION";
                    String message = "Fish Added Succesfully!";
                    TrayNotification tray = new TrayNotification(title, message, NotificationType.SUCCESS);
                    tray.showAndDismiss(new Duration(3000));
                } else {
                    String title = "WARNING";
                    String message = "Fish Not Added!!";
                    TrayNotification tray = new TrayNotification(title, message, NotificationType.WARNING);
                    tray.showAndDismiss(new Duration(3000));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                String title = "ERROR";
                String message = "Oops Something Went Wrong!";
                TrayNotification tray = new TrayNotification(title, message, NotificationType.ERROR);
                tray.showAndDismiss(new Duration(3000));
            }
        }else{
            try {
                boolean isFishUpdated = FishModel.update(fish);

                if(isFishUpdated){
                    selectedFish.setFishId(fishId);
                    selectedFish.setFishType(fishType);
                    selectedFish.setUnitWeight(unitWeight);
                    selectedFish.setUnitPrice(unitPrice);

                    tableFish.refresh();

                    String title = "CONFIRMATION";
                    String message = "Fish Updated Succesfully!";
                    TrayNotification tray = new TrayNotification(title, message, NotificationType.SUCCESS);
                    tray.showAndDismiss(new Duration(3000));
                }else {
                    String title = "WARNING";
                    String message = "Fish Not Updated!!";
                    TrayNotification tray = new TrayNotification(title, message, NotificationType.WARNING);
                    tray.showAndDismiss(new Duration(3000));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                String title = "ERROR";
                String message = "Oops Something Went Wrong!";
                TrayNotification tray = new TrayNotification(title, message, NotificationType.ERROR);
                tray.showAndDismiss(new Duration(3000));
            }
        }
        btnAdd.setText("Add");
        loadNewFishId();
        clearFields();

        lblFishType.setText(" ");
        lblUnitWeight.setText(" ");
        lblUnitPrice.setText(" ");
    }

    private void clearFields() {
        txtFishType.setText(null);
        txtUnitWeight.setText(null);
        txtUnitPrice.setText(null);
    }


    @FXML
    void tableFishOnMouseClicked(MouseEvent event) {
        selectedFish = tableFish.getSelectionModel().getSelectedItem();

        lblFishId.setText(selectedFish.getFishId());
        txtFishType.setText(selectedFish.getFishType());
        txtUnitWeight.setText(String.valueOf(selectedFish.getUnitWeight()));
        txtUnitPrice.setText(String.valueOf(selectedFish.getUnitPrice()));

        btnAdd.setText("Update");
    }

    @FXML
    void btnFishPriceListOnAction(ActionEvent event) {
        new Thread(() -> {
            try {
                Connection con = DBConnection.getInstance().getConnection();

                InputStream input = new FileInputStream(new File("F:/Github/go-fish/src/main/resources/reports/fish_price_list.jrxml"));
                JasperDesign jasperDesign = JRXmlLoader.load(input);
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

                Map<String, Object> data = new HashMap();

                JasperPrint fillReport = JasperFillManager.fillReport(jasperReport, data, con);
                JasperViewer.viewReport(fillReport, false);


            } catch (JRException | FileNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }).start();

    }

    @FXML
    void btnSendPriceListOnAction(ActionEvent event) throws SQLException {
        if(isPriceAlreadySended){
            return;
        }
        isPriceAlreadySended = true;
        String text = "";
        List<Fish> fishList = FishModel.getAllFish();
        for(Fish fish : fishList){
            text += fish.getFishId() +" - "+ fish.getFishType() +" - "+ fish.getUnitWeight() +"kg - Rs: "+ fish.getUnitPrice() + "\n";
        }

        try {
            String subject = "Fishing House Fish Price List";

            SendMail sendMail = new SendMail(); //creating an instance of SendMail class
            sendMail.setTo("exampledilshan@gmail.com"); //receiver's sendMail
            sendMail.setSubject(subject); //email subject
            sendMail.setMsg(text);//email message

            Thread thread = new Thread(sendMail);
            thread.start();

            Mail mail = new Mail(CrudUtil.getNewId(MailModel.getLastId()), subject + "$" +  text, LocalDateTime.now());
            boolean isMailRecorded = MailModel.save(mail, CrewModel.getCrewIds());

            if(isMailRecorded){
                String title = "CONFIRMATION";
                String message = "Mails Send Succesfully!";
                TrayNotification tray = new TrayNotification(title, message, NotificationType.SUCCESS);
                tray.showAndDismiss(new Duration(3000));
            }else {
                String title = "WARNING";
                String message = "Mails Not Send!";
                TrayNotification tray = new TrayNotification(title, message, NotificationType.WARNING);
                tray.showAndDismiss(new Duration(3000));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            String title = "ERROR";
            String message = "Oops...Something went wrong!!!";
            TrayNotification tray = new TrayNotification(title, message, NotificationType.ERROR);
            tray.showAndDismiss(new Duration(3000));
        }
    }
}
