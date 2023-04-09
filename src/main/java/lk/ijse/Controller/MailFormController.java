package lk.ijse.Controller;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.Model.CrewModel;
import lk.ijse.dto.Crew;
import lk.ijse.dto.tm.SelectCrewTM;
import lk.ijse.util.SendMail;
import lombok.SneakyThrows;

import java.net.URL;
import java.sql.SQLException;
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

    private void loadCellValueFactory() {
        colCbox.setCellValueFactory(new PropertyValueFactory<>("cbox"));
        colCrewId.setCellValueFactory(new PropertyValueFactory<>("crewId"));
        colLeader.setCellValueFactory(new PropertyValueFactory<>("leader"));
        colCrewmen.setCellValueFactory(new PropertyValueFactory<>("crewmenCount"));
        colBoats.setCellValueFactory(new PropertyValueFactory<>("boatsCount"));
    }

    @FXML
    void btnSendOnAction(ActionEvent event) {
        System.out.println("Start");

        SendMail sendMail = new SendMail(); //creating an instance of SendMail class
        sendMail.setMsg(txtDescription.getText());//email message
        sendMail.setTo("tgdilshanrangaka2002@gmail.com"); //receiver's sendMail
        sendMail.setSubject(txtSubject.getText()); //email subject

        Thread thread = new Thread(sendMail);
        thread.start();

        System.out.println("end");
    }

    @FXML
    void cboxSelectAllOnMouseClicked(MouseEvent event) {
        boolean isSelected = cboxSelectAll.isSelected();

        for(SelectCrewTM selectCrewTM : selectCrewTMS){
            selectCrewTM.getCbox().setSelected(isSelected);
        }
    }

}
