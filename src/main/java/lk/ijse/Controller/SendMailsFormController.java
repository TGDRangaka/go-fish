package lk.ijse.Controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.Model.MailDetailModel;
import lk.ijse.Model.MailModel;
import lk.ijse.dto.Mail;
import lk.ijse.dto.tm.MailRecordsTM;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SendMailsFormController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private TableView<MailRecordsTM> tableMailRecords;

    @FXML
    private TableColumn<?, ?> colMailId;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colTo;

    @FXML
    private TableColumn<?, ?> colSentTime;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private JFXTextField txtSearch;

    private ObservableList<MailRecordsTM> mailRecords = FXCollections.observableArrayList();

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCellValueFactory();
        loadMailRecordsTable();
    }

    private void loadMailRecordsTable() throws SQLException {
        List<Mail> mailList = MailModel.getAllMails();

        for(Mail mail : mailList){
            String mailId = mail.getId();
            String description = mail.getDescription();
            LocalDateTime dateTime = mail.getDateTime();
            String to = MailDetailModel.getRecords(mailId);
            Button action = new Button("Delete");
            action.getStyleClass().add("table-delete-btn");

            MailRecordsTM mailRecord = new MailRecordsTM(mailId, description,to, dateTime, action);
            setDeleteButtonOnAction(action, mailRecord);

            mailRecords.add(mailRecord);
        }

        tableMailRecords.setItems(mailRecords);
    }

    private void setDeleteButtonOnAction(Button action, MailRecordsTM mailRecord) {
        action.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are sure want to delete this mail", yes, no).showAndWait();

            if(result.orElse(no) == yes){
                try {
                    boolean isMailDeleted = MailModel.deleteMail(mailRecord.getMailId());

                    if(isMailDeleted){
                        mailRecords.removeAll(mailRecord);
                        tableMailRecords.refresh();
                        new Alert(Alert.AlertType.CONFIRMATION, "Mail Deleted Succesfully!").show();
                    }else {
                        new Alert(Alert.AlertType.WARNING, "Mail Not Deleted").show();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Oops... Something went wrong!!!").show();
                }
            }
        });
    }

    private void loadCellValueFactory() {
        colMailId.setCellValueFactory(new PropertyValueFactory<>("mailId"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colTo.setCellValueFactory(new PropertyValueFactory<>("to"));
        colSentTime.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("action"));

        txtSearch.setOnAction((e) -> {
            btnSearchOnAction(new ActionEvent());
        });
    }

    @FXML
    void btnSendAMailOnAction(ActionEvent event) throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("/view/mail_form.fxml"));
        root.getChildren().setAll(node);
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String search = txtSearch.getText();
        if(search.length() == 0){
            tableMailRecords.setItems(mailRecords);
        }

        ObservableList<MailRecordsTM> temp = FXCollections.observableArrayList();

        for(MailRecordsTM mailRec : mailRecords){
            if(mailRec.getMailId().equals(search) || mailRec.getTo().contains(search)){
                temp.add(mailRec);
            }
        }

        tableMailRecords.setItems(temp);
    }
}
