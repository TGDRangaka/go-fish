package lk.ijse.Controller;

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
import javafx.scene.layout.Pane;
import lk.ijse.Model.BoatModel;
import lk.ijse.Model.BoatOwnerModel;
import lk.ijse.Model.CrewModel;
import lk.ijse.Model.CrewmanModel;
import lk.ijse.dto.BoatOwner;
import lk.ijse.dto.Crew;
import lk.ijse.dto.tm.CrewTM;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class CrewManageFormController implements Initializable {
    public Pane paneFilter;
    @FXML
    private AnchorPane root;
    @FXML
    private TableView<CrewTM> tableCrew;

    @FXML
    private TableColumn<?, ?> colCrewId;

    @FXML
    private TableColumn<?, ?> colLeaderName;

    @FXML
    private TableColumn<?, ?> colCrewmenCount;

    @FXML
    private TableColumn<?, ?> colBoatsCount;

    @FXML
    private TableColumn<?, ?> colAvailableTimes;

    @FXML
    private TableColumn<?, ?> colAvailableDays;

    @FXML
    private TableColumn<?, ?> colAction1;

    @FXML
    private TableColumn<?, ?> colAction2;

    @FXML
    private Label lblCrewCount;

    @FXML
    private Label lblBoatsCount;

    @FXML
    private Label lblCrewmenCount;

    @FXML
    private Label lblBoatOwnersCount;

    private ObservableList<CrewTM> crews =  FXCollections.observableArrayList();

    public static String crew = null;

    public static boolean isBtnUpdatePressed = false;



    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        crew = null;
        isBtnUpdatePressed = false;
        loadCellValueFactory();
        loadCrewTable();
        loadLabels();
    }

    private void loadLabels() throws SQLException {
        int crewCount = CrewModel.getAllCrew().size();
        lblCrewCount.setText(String.valueOf(crewCount));

        int crewmenCount = CrewmanModel.getCrewmen().size();
        lblCrewmenCount.setText(String.valueOf(crewmenCount));

        int boatsCount = BoatModel.getBoats().size();
        lblBoatsCount.setText(String.valueOf(boatsCount));

        int boatOwnersCount = BoatOwnerModel.getOwners().size();
        lblBoatOwnersCount.setText(String.valueOf(boatOwnersCount));
    }

    private void loadCrewTable() {
        try {
            List<Crew> crewList = CrewModel.getAllCrew();

            for (Crew crew : crewList){
                String crewId = crew.getCrewId();
                String leader = crew.getLeader();
                Integer crewmenCount = crew.getCrewmenCount();
                Integer boatsCount = crew.getBoatsCount();
                String availableTimes =crew.getAvailableTimes();
                String availableDays = crew.getAvailableDays();
                Button update = new Button("Update");
                Button delete = new Button("Delete");

                CrewTM crewTM = new CrewTM(crewId, leader, crewmenCount, boatsCount, availableTimes, availableDays, update, delete);

                setButtonsOnAction(crewTM, crewId, update);
                setButtonsOnAction(crewTM, crewId, delete);

                crews.add(crewTM);
            }

            tableCrew.setItems(crews);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setButtonsOnAction(CrewTM crewTM, String crewId, Button btn) {
        if(btn.getText().equals("Update")){
            btn.setOnAction((e) -> {
                try {

                    crew = crewTM.getCrewId();
                    isBtnUpdatePressed = true;
                    btnRegisterCrewOnAction(e);

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            });
        }else{
            btn.setOnAction((e) -> {
                try {
                    List<String> boatOwnersIdList = BoatOwnerModel.getBoatOwnersId(crewId);
                    boolean isCrewDeleted = CrewModel.deleteCrew(crewId, boatOwnersIdList);

                    if(isCrewDeleted){
                        crews.removeAll(crewTM);
                        tableCrew.refresh();
                        new Alert(Alert.AlertType.CONFIRMATION, "Crew Deleted Succesfully!").show();
                    }else{
                        new Alert(Alert.AlertType.WARNING, "Crew Not Deleted!!").show();
                    }
                } catch (SQLException ex){
                    System.out.println(ex);
                    new Alert(Alert.AlertType.ERROR, "Oops...Something went wrong!!!").show();
                }
            });
        }
    }

    private void loadCellValueFactory() {
        colCrewId.setCellValueFactory(new PropertyValueFactory<>("crewId"));
        colLeaderName.setCellValueFactory(new PropertyValueFactory<>("leader"));
        colCrewmenCount.setCellValueFactory(new PropertyValueFactory<>("crewmenCount"));
        colBoatsCount.setCellValueFactory(new PropertyValueFactory<>("boatsCount"));
        colAvailableTimes.setCellValueFactory(new PropertyValueFactory<>("availableTimes"));
        colAvailableDays.setCellValueFactory(new PropertyValueFactory<>("availableDays"));
        colAction1.setCellValueFactory(new PropertyValueFactory<>("update"));
        colAction2.setCellValueFactory(new PropertyValueFactory<>("delete"));
    }

    public void btnRegisterCrewOnAction(ActionEvent actionEvent)throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("/view/crew_registration_form.fxml"));
        root.getChildren().setAll(node);
    }

    @FXML
    void btnFilterOnAction(ActionEvent event) {
        paneFilter.setVisible(!paneFilter.isVisible());
    }
}
