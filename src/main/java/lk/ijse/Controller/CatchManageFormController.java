package lk.ijse.Controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.Model.CatchModel;
import lk.ijse.dto.Catch;
import lk.ijse.dto.tm.CatchTM;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class CatchManageFormController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<CatchTM> tableCatches;

    @FXML
    private TableColumn<?, ?> colCatchId;

    @FXML
    private TableColumn<?, ?> colCrewId;

    @FXML
    private TableColumn<?, ?> colCatchWeight;

    @FXML
    private TableColumn<?, ?> colCatchDate;

    @FXML
    private TableColumn<?, ?> colPaymentAmount;

    @FXML
    private TableColumn<?, ?> colPaymentTime;

    @FXML
    private TableColumn<?, ?> colTripStartedTime;

    @FXML
    private TableColumn<?, ?> colTripEndedTime;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private JFXTextField txtSearch;

    private ObservableList<CatchTM> catchList = FXCollections.observableArrayList();

    public static boolean isCatchSelected = false;

    public static CatchTM selectedCatch = null;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCellFactory();
        loadCatchTable();
        isCatchSelected = false;
        selectedCatch = null;
    }

    private void loadCatchTable() throws SQLException {
        List<Catch> catches = CatchModel.getAllCatches();
        for(Catch c : catches){
            String id = c.getId();
            String crewId = c.getCrewId();
            Double totalWeight = c.getTotalWeight();
            LocalDate catchDate = c.getCatchDate();
            Double paymentAmount = c.getPaymentAmount();
            LocalTime paymentTime = c.getPaymentTime();
            LocalTime tripStartedTime = c.getTripStartedTime();
            LocalTime tripEndedTime = c.getTripEndedTime();
            Button action = new Button("Details");

            CatchTM catchTM = new CatchTM(id, crewId, totalWeight, catchDate, paymentAmount, paymentTime, tripStartedTime, tripEndedTime, action);
            setActionButtonOnAction(action, catchTM);

            catchList.add(catchTM);
        }
        tableCatches.setItems(catchList);
    }

    private void setActionButtonOnAction(Button action, CatchTM catchTM) {
        action.setOnAction((e) -> {
            try {
                isCatchSelected = true;
                selectedCatch = catchTM;

                Node node = null;
                node = FXMLLoader.load(getClass().getResource("/view/catch_record_form.fxml"));
                root.getChildren().setAll(node);

            } catch (IOException ex) {
                ex.printStackTrace();
            }

            System.out.println(catchTM.toString());
        });
    }

    private void loadCellFactory() {
        colCatchId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCrewId.setCellValueFactory(new PropertyValueFactory<>("crewId"));
        colCatchWeight.setCellValueFactory(new PropertyValueFactory<>("catchWeight"));
        colCatchDate.setCellValueFactory(new PropertyValueFactory<>("catchDate"));
        colPaymentAmount.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        colPaymentTime.setCellValueFactory(new PropertyValueFactory<>("paymentTime"));
        colTripStartedTime.setCellValueFactory(new PropertyValueFactory<>("tripStartedTime"));
        colTripEndedTime.setCellValueFactory(new PropertyValueFactory<>("tripEndedTime"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("action"));

        txtSearch.setOnAction((e) -> {
            btnSearchOnAction(new ActionEvent());
        });
    }

    @FXML
    void btnNewCatchOnAction(ActionEvent event) throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("/view/catch_record_form.fxml"));
        root.getChildren().setAll(node);
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String search = txtSearch.getText();
        if(search.length() == 0){
            tableCatches.setItems(catchList);
            return;
        }

        ObservableList<CatchTM> temp = FXCollections.observableArrayList();

        for(CatchTM catchTM : catchList){
            String date = String.valueOf(catchTM.getCatchDate());
            if(catchTM.getId().matches(".*"+search+".*") ||
                    catchTM.getCrewId().matches(".*"+search+".*") ||
                    date.matches(search) ||
                    (search.contains("-") && date.matches(".*"+search+".*")) ||
                    String.valueOf(catchTM.getCatchWeight()).matches(search) ||
                    String.valueOf(catchTM.getPaymentAmount()).matches(search) ||
                    (search.contains(":") && (String.valueOf(catchTM.getPaymentTime()).matches(".*"+search+".*") ||
                            String.valueOf(catchTM.getTripStartedTime()).matches(".*"+search+".*") ||
                            String.valueOf(catchTM.getTripEndedTime()).matches(".*"+search+".*"))
                    )){
                temp.add(catchTM);
            }
        }

        tableCatches.setItems(temp);
    }
}
