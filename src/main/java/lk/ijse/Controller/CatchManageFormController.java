package lk.ijse.Controller;

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
    }

    @FXML
    void btnNewCatchOnAction(ActionEvent event) throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("/view/catch_record_form.fxml"));
        root.getChildren().setAll(node);
    }

}
