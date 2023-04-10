package lk.ijse.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.Model.CatchDetailModel;
import lk.ijse.Model.CatchModel;
import lk.ijse.Model.CrewModel;
import lk.ijse.Model.FishModel;
import lk.ijse.dto.Catch;
import lk.ijse.dto.CatchDetail;
import lk.ijse.dto.Fish;
import lk.ijse.dto.tm.CatchDetailTM;
import lk.ijse.dto.tm.CatchTM;
import lombok.SneakyThrows;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static lk.ijse.util.CrudUtil.getNewId;

public class CatchRecordFormController implements Initializable {

    @FXML
    private TableView<CatchDetailTM> tableCatchDetail;

    @FXML
    private TableColumn<?, ?> colFishId;

    @FXML
    private TableColumn<?, ?> colFishType;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private TableColumn<?, ?> colCaughtWeight;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private JFXComboBox<String> cbFishType;


    @FXML
    private JFXTextField txtTripStartedTime;

    @FXML
    private JFXTextField txtTripEndedTime;

    @FXML
    private JFXButton btnPayment;

    @FXML
    private JFXButton btnAdd;

    @FXML
    private Label lblCatchId;

    @FXML
    private Label lblCatchDate;

    @FXML
    private Label lblFishId;

    @FXML
    private Label lblUnitWeight;

    @FXML
    private Label lblUnitPrice;

    @FXML
    private TextField txtCaughtWeight;

    @FXML
    private Label lblNetTotal;

    @FXML
    private Label lblTotalWeight;

    @FXML
    private JFXComboBox<String> cbCrewId;

    private ObservableList<CatchDetailTM> catchDetais = FXCollections.observableArrayList();

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(CatchManageFormController.isCatchSelected){
            loadDetails(CatchManageFormController.selectedCatch);
        }else {
            loadDetails();
            loadCrewIdComboBox();
            loadFishTypeComboBox();
        }
        loadCellValueFactory();
    }

    private void loadDetails(CatchTM selectedCatch) throws SQLException {
        lblCatchId.setText(selectedCatch.getId());
        lblCatchDate.setText(String.valueOf(selectedCatch.getCatchDate()));
        lblNetTotal.setText(String.valueOf(selectedCatch.getPaymentAmount()));
        lblTotalWeight.setText(String.valueOf(selectedCatch.getCatchWeight()));
        txtTripStartedTime.setText(String.valueOf(selectedCatch.getTripStartedTime()));
        txtTripStartedTime.setDisable(true);
        txtTripEndedTime.setText(String.valueOf(selectedCatch.getTripEndedTime()));
        txtTripEndedTime.setDisable(true);
        cbCrewId.setValue(selectedCatch.getCrewId());

        btnPayment.setDisable(true);
        btnAdd.setDisable(true);

        List<CatchDetail> catchDetails = CatchDetailModel.getCatchDetails(selectedCatch.getId());
        List<Fish> fishList = FishModel.getAllFish();

        loadCatchDetailTable(catchDetails, fishList);
    }

    private void loadCatchDetailTable(List<CatchDetail> catchDetails, List<Fish> fishList) {
        for(CatchDetail catchDetail : catchDetails){
            for(int i = 0; i < fishList.size(); i++){
                if(catchDetail.getFishId().equals(fishList.get(i).getFishId())){
                    Fish fish = fishList.get(i);

                    String fishId = fish.getFishId();
                    String fishType = fish.getFishType();
                    Double unitPrice = fish.getUnitPrice();
                    Double caughtWeight = catchDetail.getWeight();
                    Double total = catchDetail.getTotal();
                    Button action = new Button("Remove");
                    action.setDisable(true);

                    this.catchDetais.add(new CatchDetailTM(
                            fishId,
                            fishType,
                            unitPrice,
                            caughtWeight,
                            total,
                            action
                    ));
                    tableCatchDetail.setItems(this.catchDetais);
                }
            }
        }
    }

    private void loadCrewIdComboBox() throws SQLException {
        List<String> crewIds = CrewModel.getCrewIds();
        ObservableList<String> ids = FXCollections.observableArrayList();
        for (String id : crewIds){
            ids.add(id);
        }

        cbCrewId.setItems(ids);
    }

    private void loadCellValueFactory() {
        colFishId.setCellValueFactory(new PropertyValueFactory<>("fishId"));
        colFishType.setCellValueFactory(new PropertyValueFactory<>("fishType"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colCaughtWeight.setCellValueFactory(new PropertyValueFactory<>("caughtWeight"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("action"));
    }

    private void loadFishTypeComboBox() throws SQLException {
        List<Fish> fishList = FishModel.getAllFish();
        ObservableList<String> fishTypeList = FXCollections.observableArrayList();
        for(Fish fish : fishList){
            fishTypeList.add(fish.getFishType());
        }
        cbFishType.setItems(fishTypeList);
    }

    private void loadDetails() throws SQLException {
        lblCatchId.setText(getNewId(CatchModel.getLastId()));
        lblCatchDate.setText(String.valueOf(LocalDate.now()));
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        String fishId = lblFishId.getText();
        String fishType = cbFishType.getValue();
        Double unitPrice = Double.valueOf(lblUnitPrice.getText());
        Double unitWeight = Double.valueOf(lblUnitWeight.getText());
        Double caughtWeight = Double.valueOf(txtCaughtWeight.getText());
        Double total = (caughtWeight / unitWeight) * unitPrice;
        Button action = new Button("Remove");

        CatchDetailTM catchDetailTM = null;
        if(catchDetais.size() > 0)
        for (int i = 0; i < catchDetais.size(); i++) {
            if (catchDetais.get(i).getFishId().equals(fishId)){
                catchDetailTM = catchDetais.get(i);
            }
        }

        if(catchDetailTM == null){
            CatchDetailTM catchDetail = new CatchDetailTM(fishId, fishType, unitPrice, caughtWeight, total, action);
            setActionButtonOnAction(action, catchDetail);
            catchDetais.add(catchDetail);
            tableCatchDetail.setItems(catchDetais);

        }else {
            catchDetailTM.setCaughtWeight(catchDetailTM.getCaughtWeight() + caughtWeight);
            catchDetailTM.setTotal(catchDetailTM.getTotal() + total);
            tableCatchDetail.refresh();
        }

        calculateTotals(total, caughtWeight);
        clearFields();
    }

    private void calculateTotals(Double total, Double caughtWeight) {
        Double netTotal = Double.valueOf(lblNetTotal.getText());
        lblNetTotal.setText(String.format("%1.2f", netTotal + total));

        Double totalWeight = Double.valueOf(lblTotalWeight.getText());
        lblTotalWeight.setText(String.format("%1.2f", totalWeight + caughtWeight));
    }

    private void setActionButtonOnAction(Button action, CatchDetailTM catchDetail) {
        action.setOnAction((e) -> {
            catchDetais.removeAll(catchDetail);
            tableCatchDetail.refresh();
            calculateTotals(-catchDetail.getTotal(), -catchDetail.getCaughtWeight());
        });
    }

    @FXML
    void btnPaymentOnAction(ActionEvent event) {
        Catch c = getCatch();
        List<CatchDetail> catchDetails = getCatchDetails();

        try {
            boolean isCatchDetailsEmpty = catchDetails.isEmpty();
            boolean isCatchSaved = false;
            if(!isCatchDetailsEmpty) {
                isCatchSaved = CatchModel.saveCatch(c, catchDetails);
            }

            if(isCatchSaved){
                new Alert(Alert.AlertType.CONFIRMATION, "Catch Recorded Succesfully!").show();
            }else {
                new Alert(Alert.AlertType.WARNING, "Catch Not Recorded!!").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();

            new Alert(Alert.AlertType.ERROR, "Oops Something went wrong!").show();
        }
    }

    private List<CatchDetail> getCatchDetails() {
        List<CatchDetail> catchDetails = new ArrayList<>();
        String catchId = lblCatchId.getText();

        for (CatchDetailTM catchDetail : this.catchDetais){
            catchDetails.add(new CatchDetail(catchId, catchDetail.getFishId(), catchDetail.getCaughtWeight(), catchDetail.getTotal()));
        }
        return catchDetails;
    }

    private Catch getCatch() {
        String catchId = lblCatchId.getText();
        Double totalWeight = Double.valueOf(lblTotalWeight.getText());
        LocalDate catchDate = LocalDate.parse(lblCatchDate.getText());
        Double netTotal = Double.valueOf(lblNetTotal.getText());
        LocalTime paymentTime = LocalTime.now();
        LocalTime tripStartedTime = LocalTime.parse(txtTripStartedTime.getText());
        LocalTime tripEndedTime = LocalTime.parse(txtTripEndedTime.getText());
        String crewId = cbCrewId.getValue();
        String adminId = null;

        return new Catch(catchId, totalWeight, catchDate, netTotal, paymentTime, tripStartedTime, tripEndedTime, crewId, adminId);
    }

    @FXML
    void cbFishTypeOnAction(ActionEvent event) throws SQLException {
        String selectedFish = cbFishType.getValue();
        Fish fish = FishModel.getFish(selectedFish);

        lblFishId.setText(fish.getFishId());
        lblUnitPrice.setText(String.format("%1.2f",fish.getUnitPrice()));
        lblUnitWeight.setText(String.format("%1.2f",fish.getUnitWeight()));

        txtCaughtWeight.requestFocus();
    }

    void clearFields(){
        cbFishType.setValue(null);
        lblFishId.setText(null);
        lblUnitWeight.setText(null);
        lblUnitPrice.setText(null);
        txtCaughtWeight.setText(null);
    }
}