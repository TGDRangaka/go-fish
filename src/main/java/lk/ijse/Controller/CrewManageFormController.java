package lk.ijse.Controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import lk.ijse.Model.*;
import lk.ijse.dto.BoatOwner;
import lk.ijse.dto.Crew;
import lk.ijse.dto.tm.CrewTM;
import lombok.SneakyThrows;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CrewManageFormController implements Initializable {
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
    private JFXTextField txtSearch;

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
                String availableTimes = getDayTimes(crew.getAvailableTimes());
                String availableDays = getWeekdays(crew.getAvailableDays());
                Button update = new Button("Update");
                update.getStyleClass().add("table-update-btn");
                Button delete = new Button("Delete");
                delete.getStyleClass().add("table-delete-btn");

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

    private String getWeekdays(String digits) {
        String[] weekdays = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        String result = "";
        if (digits.equals("1111111")) {
            return "Everyday";
        } else if (digits.equals("1111100")) {
            return "Workdays";
        } else if (digits.equals("0000011")) {
            return "Weekend";
        }
        for (int i = 0; i < digits.length(); i++) {
            if (digits.charAt(i) == '1') {
                result += weekdays[i] + ",";
            }
        }
        // Remove the last comma
        if (!result.isEmpty()) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
    private String getDayTimes(String dayTimes) {
        StringBuilder result = new StringBuilder();
        if (dayTimes.charAt(0) == '1') result.append("Morning,");
        if (dayTimes.charAt(1) == '1') result.append("Evening,");
        if (dayTimes.charAt(2) == '1') result.append("Night,");
        return result.toString().replaceAll(",$", "");
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
                ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
                Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure want to remove this Crew", yes, no).showAndWait();

                if(result.orElse(no) == (yes)) {
                    try {
                        List<String> boatOwnersIdList = BoatOwnerModel.getBoatOwnersId(crewId);
                        List<String> catchIdList = CatchModel.getAllCatchIds(crewId);
                        boolean isCrewDeleted = CrewModel.deleteCrew(crewId, boatOwnersIdList, catchIdList);

                        if (isCrewDeleted) {
                            crews.removeAll(crewTM);
                            tableCrew.refresh();
                            loadLabels();

                            String title = "Deleted";
                            String message = "Crew Deleted Succesfully!";
                            TrayNotification tray = new TrayNotification(title, message, NotificationType.SUCCESS);
                            tray.showAndDismiss(new Duration(3000));
                        } else {
                            new Alert(Alert.AlertType.WARNING, "Crew Not Deleted!!").show();
                            String title = "Warning";
                            String message = "Crew Not Deleted!!";
                            TrayNotification tray = new TrayNotification(title, message, NotificationType.WARNING);
                            tray.showAndDismiss(new Duration(3000));
                        }
                    } catch (SQLException ex) {
                        System.out.println(ex);

                        String title = "Error";
                        String message = "Oops...Something went wrong!!!";
                        TrayNotification tray = new TrayNotification(title, message, NotificationType.ERROR);
                        tray.showAndDismiss(new Duration(3000));
                    }
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

        txtSearch.setOnAction((e) -> {
            btnSearchOnAction(new ActionEvent());
        });
    }

    public void btnRegisterCrewOnAction(ActionEvent actionEvent)throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("/view/crew_registration_form.fxml"));
        root.getChildren().setAll(node);
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String search = txtSearch.getText();
        if(search.length() == 0){
            tableCrew.setItems(crews);
            return;
        }

        ObservableList<CrewTM> temp = FXCollections.observableArrayList();

        for(CrewTM crew : crews){
            String boatCount = String.valueOf(crew.getBoatsCount());
            String crewmenCount = String.valueOf(crew.getCrewmenCount());
            if(crew.getCrewId().matches(".*" + search + ".*") ||
                    crew.getLeader().matches(".*" + search + ".*") ||
                    crew.getAvailableDays().matches(".*" + search + ".*") ||
                    crew.getAvailableTimes().matches(".*" + search + ".*") ||
                    boatCount.matches(search) ||
                    crewmenCount.matches(search)) {

                temp.add(crew);
            }
        }

        tableCrew.setItems(temp);
    }

}
