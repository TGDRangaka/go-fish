package lk.ijse.Controller;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import lk.ijse.DB.DBConnection;
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
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private Label lblCaughtWeight;

    @FXML
    private JFXCheckBox cboxGetReport;

    @FXML
    private JFXComboBox<String> cbCrewId;

    @FXML
    private JFXButton btnScanQR;

    @FXML
    private ImageView imageCam;
    private Webcam webcam = null;
    private boolean isThreadRunning = false;

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
        validation();


        webcam = Webcam.getDefault();
        webcam.setViewSize(new java.awt.Dimension(320,240));
    }

    private void validation() {
        txtCaughtWeight.setOnKeyReleased((e) -> {
            if(!txtCaughtWeight.getText().matches("^\\d+(\\.\\d+)?$")){
                lblCaughtWeight.setText("Invalid value!");
            }else{
                lblCaughtWeight.setText(null);
            }
        });
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
        btnScanQR.setDisable(true);

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

        txtCaughtWeight.setOnAction((e) -> {
            btnAdd.fire();
        });
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
        if(lblCaughtWeight.getText() != null){
            return;
        }
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
        lblCaughtWeight.setText(" ");
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

                if(cboxGetReport.isSelected()){
                    getReport();
                }
                clearFields();
                catchDetais.clear();
                tableCatchDetail.refresh();
                cbCrewId.setValue(null);
                lblTotalWeight.setText(null);
                lblNetTotal.setText(null);

                loadDetails();
                MainWindowFormController.btnCatch.fire();
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

    @FXML
    void btnScanQROnAction(ActionEvent event) {
        webcam.open();

        if(isThreadRunning){
            isThreadRunning = false;
            webcam.close();
            return;
        }

        isThreadRunning = true;
        scanQR();
        try {
            new Thread(){
                @Override
                public void run() {
                    while (isThreadRunning) {
                            imageCam.setImage(SwingFXUtils.toFXImage(webcam.getImage(), null));
//                        imageCam.setImage(SwingFXUtils.toFXImage(webcam.getImage(), null));
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    System.out.println("btn treads stoped");
                }
            }.start();
        } catch (Exception ex){
            System.out.println(ex);
        }

    }

    private void scanQR() {
        new Thread(){
            @Override
            public void run() {
                while (isThreadRunning) {

                    Result result = null;
                    BufferedImage image = null;

                    if (webcam.isOpen()) {
                        if ((image = webcam.getImage()) == null) {
                            continue;
                        }
                    }

                    LuminanceSource source = new BufferedImageLuminanceSource(image);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                    try {
                        result = new MultiFormatReader().decode(bitmap);
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                    }

                    if(result != null){
                        String text = result.getText();
                        System.out.println(text);
                        webcam.close();
                        isThreadRunning = false;

                        Platform.runLater(() -> {
                            try {
                                setResult(text);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        });



                    }
                }
                System.out.println("scan treads stoped");
            }
        }.start();
    }

    private void setResult(String text) throws SQLException {
        lblCaughtWeight.setText(null);

        String[] data = text.split("%");
        String[] catchData = data[0].split(",");
        String[] catchDetails = data[1].split(",");

        lblCatchDate.setText(catchData[0]);
        cbCrewId.setValue(catchData[1]);

        for(String catchDetail : catchDetails){
            String[] split = catchDetail.split(":");

            cbFishType.setValue(split[0]);
            cbFishTypeOnAction(new ActionEvent());

            txtCaughtWeight.setText(split[1]);
            btnAdd.fire();
            System.out.println("fire");
        }
    }

    private void getReport() {
        try {
            Connection con = DBConnection.getInstance().getConnection();

            InputStream input = new FileInputStream(new File("F:/Github/go-fish/src/main/resources/reports/catch_report.jrxml"));
            JasperDesign jasperDesign = JRXmlLoader.load(input);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            Catch c = CatchModel.getCatch(lblCatchId.getText());
            Map<String, Object> data = new HashMap();
            data.put("crewId",c.getCrewId());
            data.put("catchId", c.getId());
            data.put("catchDate", String.valueOf(c.getCatchDate()));
            data.put("tripStartedTime", String.valueOf(c.getTripStartedTime()));
            data.put("tripEndedTime", String.valueOf(c.getTripEndedTime()));
            data.put("totalWeight", c.getTotalWeight() + "kg");
            data.put("totalPayment", "Rs." + c.getPaymentAmount());
            data.put("paymentTime", String.valueOf(c.getPaymentTime()));

            JasperPrint fillReport = JasperFillManager.fillReport(jasperReport, data, con);
            JasperViewer.viewReport(fillReport, false);


        } catch (JRException | FileNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    void clearFields(){
        cbFishType.setValue(null);
        lblFishId.setText(null);
        lblUnitWeight.setText(null);
        lblUnitPrice.setText(null);
        txtCaughtWeight.setText(null);
    }
}
