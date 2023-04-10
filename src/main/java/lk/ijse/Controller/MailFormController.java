package lk.ijse.Controller;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.Model.CrewModel;
import lk.ijse.Model.CrewmanModel;
import lk.ijse.Model.MailModel;
import lk.ijse.dto.Crew;
import lk.ijse.dto.Mail;
import lk.ijse.dto.tm.MailRecordsTM;
import lk.ijse.dto.tm.SelectCrewTM;
import lk.ijse.util.CrudUtil;
import lk.ijse.util.SendMail;
import lombok.SneakyThrows;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MailFormController implements Initializable {

    @FXML
    private JFXTextArea txtTo;

    @FXML
    private JFXTextField txtSubject;

    @FXML
    private JFXTextArea txtDescription;

    @FXML
    private TableView<SelectCrewTM> tableCrewSelect;

    @FXML
    private TableColumn<?, ?> colCbox;

    @FXML
    private TableColumn<?, ?> colCrewId;

    @FXML
    private TableColumn<?, ?> colLeader;

    @FXML
    private TableColumn<?, ?> colCrewmen;

    @FXML
    private TableColumn<?, ?> colBoats;


    @FXML
    private JFXCheckBox cboxSelectAll;

    @FXML
    private Label lblCrewId;

    private ObservableList<SelectCrewTM> selectCrewTMS = FXCollections.observableArrayList();

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCellValueFactory();
        loadSelectTable();
        loadIdLabel();
    }

    private void loadIdLabel() throws SQLException {
        lblCrewId.setText(CrudUtil.getNewId(MailModel.getLastId()));
    }

    private void loadSelectTable() throws SQLException {
        List<Crew> crewList = CrewModel.getAllCrew();

        for(Crew crew : crewList){
            CheckBox cbox = new CheckBox();
            String crewId = crew.getCrewId();
            String leader = crew.getLeader();
            Integer crewmenCount = crew.getCrewmenCount();
            Integer boatsCount = crew.getBoatsCount();

            SelectCrewTM selectCrewTM = new SelectCrewTM(cbox, crewId, leader, crewmenCount, boatsCount);
            selectCrewTMS.add(selectCrewTM);
        }
        tableCrewSelect.setItems(selectCrewTMS);
    }

    @FXML
    void btnSendOnAction(ActionEvent event) {
        List<String> idList = new ArrayList<>();
        for(SelectCrewTM crew : selectCrewTMS){
            if(crew.getCbox().isSelected()){
                idList.add(crew.getCrewId());
            }
        }

        try {
            List<String> crewmenEmails = CrewmanModel.getEmails(idList);
            StringBuilder list = new StringBuilder();
            for(int i = 0; i < crewmenEmails.size(); i++){
                list.append("\n" + crewmenEmails.get(i) + " - " + crewmenEmails.get(++i));
            }
            String text = String.valueOf(list);

            String subject = txtSubject.getText();
            String body = txtDescription.getText();

            SendMail sendMail = new SendMail(); //creating an instance of SendMail class
            sendMail.setTo("exampledilshan@gmail.com"); //receiver's sendMail
            sendMail.setSubject(subject); //email subject
            sendMail.setMsg(body + "\n" + text);//email message

            Thread thread = new Thread(sendMail);
            thread.start();

            String description = subject + "\t" + body;
            Mail mail = new Mail(lblCrewId.getText(), description, LocalDateTime.now());
            boolean isMailRecorded = MailModel.save(mail, idList);

            if(isMailRecorded){
                loadIdLabel();
                clearFields();
                new Alert(Alert.AlertType.CONFIRMATION, "Mails Send Succesfully!").show();
            }else {
                new Alert(Alert.AlertType.WARNING, "Mails Not Send!").show();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Oops...Something went wrong!!!").show();
        }


    }

    @FXML
    void cboxSelectAllOnMouseClicked(MouseEvent event) {
        boolean isSelected = cboxSelectAll.isSelected();

        for(SelectCrewTM selectCrewTM : selectCrewTMS){
            selectCrewTM.getCbox().setSelected(isSelected);
        }
    }

    private void loadCellValueFactory() {
        colCbox.setCellValueFactory(new PropertyValueFactory<>("cbox"));
        colCrewId.setCellValueFactory(new PropertyValueFactory<>("crewId"));
        colLeader.setCellValueFactory(new PropertyValueFactory<>("leader"));
        colCrewmen.setCellValueFactory(new PropertyValueFactory<>("crewmenCount"));
        colBoats.setCellValueFactory(new PropertyValueFactory<>("boatsCount"));
    }

    private void clearFields(){
        txtSubject.setText(null);
        txtDescription.setText(null);
    }
}
