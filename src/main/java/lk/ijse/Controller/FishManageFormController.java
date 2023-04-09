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
import lk.ijse.Model.FishModel;
import lk.ijse.dto.Fish;
import lk.ijse.dto.tm.FishTM;
import lk.ijse.util.CrudUtil;
import lombok.SneakyThrows;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

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

        txtUnitWeight.setOnAction((e) -> {
            txtUnitPrice.requestFocus();
        });

        txtUnitPrice.setOnAction((e) -> {
            btnAdd.fire();
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

                        new Alert(Alert.AlertType.CONFIRMATION, "Fish Deleted Succesfully!").show();
                        loadNewFishId();
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Fish Not Deleted!!").show();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Oops Something Went Wrong!").show();
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

                    new Alert(Alert.AlertType.CONFIRMATION, "Fish Added Succesfully!").show();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Fish Not Added!!").show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Oops Something Went Wrong!").show();
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

                    new Alert(Alert.AlertType.CONFIRMATION, "Fish Updated Succesfully!").show();
                }else {
                    new Alert(Alert.AlertType.WARNING, "Fish Not Updated!!").show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Oops Something Went Wrong!").show();
            }
        }
        btnAdd.setText("Add");
        loadNewFishId();
        clearFields();
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

}
