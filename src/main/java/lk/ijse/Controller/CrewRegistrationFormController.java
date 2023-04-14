package lk.ijse.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lk.ijse.Model.*;
import lk.ijse.dto.*;
import lk.ijse.dto.tm.*;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static lk.ijse.util.CrudUtil.getNewId;

public class CrewRegistrationFormController implements Initializable {


    public TableColumn colCrewmanNo;
    public Label lblCrewmenCount;
    public Pane paneYesNo;
    public Pane paneOwnerDetails;

    private int prase = 1;

    @FXML
    private AnchorPane subRoot;

    @FXML
    private Pane paneRegistrationForm;

    @FXML
    private TableView<CrewmanRegTM> tableCrewDetailsReg;

    @FXML
    private TableColumn<?, ?> colCrewmanIDReg;

    @FXML
    private TableColumn<?, ?> colCrewmanNameReg;

    @FXML
    private TableColumn<?, ?> colCrewmanNICReg;

    @FXML
    private TableColumn<?, ?> colCrewmanEmailReg;

    @FXML
    private TableColumn<?, ?> colContactNoReg;

    @FXML
    private Label lblCrewIdReg;

    @FXML
    private Label lblCrewmenCountReg;

    @FXML
    private TableView<BoatRegTM> tableOwnerReg;

    @FXML
    private TableColumn<?, ?> colBoatIdReg;

    @FXML
    private TableColumn<?, ?> colBoatRegistrationNoReg;

    @FXML
    private TableColumn<?, ?> colBoatModelReg;

    @FXML
    private TableColumn<?, ?> colBoatOwnerIdReg;

    @FXML
    private TableColumn<?, ?> colBoatOwnerNameReg;

    @FXML
    private Label lblBoatsCountReg;

    @FXML
    private Label lblBoatOwnersCountReg;

    @FXML
    private JFXCheckBox cboxSendMails;

    @FXML
    private Label lblCrewLeaderReg;

    @FXML
    private Label lblAvailableTimesReg;

    @FXML
    private JFXTextArea txtAvailableDaysReg;

    @FXML
    private Pane paneBoatsDetails;

    @FXML
    private JFXTextField txtBoatRegistrationNo;

    @FXML
    private JFXTextField txtBoatModel;

    @FXML
    private JFXTextField txtBoatSattelitePhoneNo;

    @FXML
    private JFXComboBox<String> cbBoatType;

    @FXML
    private JFXButton btnAddBoat;

    @FXML
    private JFXButton btnRemoveBoat;

    @FXML
    private TableView<BoatTM> tableBoats;

    @FXML
    private JFXTextField txtOwnerName;

    @FXML
    private JFXTextField txtOwnerAddress;

    @FXML
    private JFXTextField txtOwnerNIC;

    @FXML
    private JFXTextField txtOwnerContactNo;

    @FXML
    private JFXComboBox<String> cbSelectBoat;

    @FXML
    private JFXButton btnAddOwner;

    @FXML
    private JFXButton btnRemoveOwner;

    @FXML
    private TableView<BoatOwnerTM> tableOwners;

    @FXML
    private TableColumn<?, ?> colOwnerNo;

    @FXML
    private TableColumn<?, ?> colOwnerBoatNo;

    @FXML
    private TableColumn<?, ?> colOwnerName;

    @FXML
    private TableColumn<?, ?> colOwnerNIC;

    @FXML
    private TableColumn<?, ?> colOwnerAddress;

    @FXML
    private TableColumn<?, ?> colOwnerContactNo;

    @FXML
    private TableColumn<?, ?> colOwnerAction;

    @FXML
    private Pane paneCrewDetails;

    @FXML
    private TableView<CrewmanTM> tableCrewmens;

    @FXML
    private TableColumn<?, ?> colCrewmanName;

    @FXML
    private TableColumn<?, ?> colCrewmanNIC;

    @FXML
    private TableColumn<?, ?> colCrewmanAddress;

    @FXML
    private TableColumn<?, ?> colCrewmanBOD;

    @FXML
    private TableColumn<?, ?> colCrewmanEmail;

    @FXML
    private TableColumn<?, ?> colCrewmanContactNo;

    @FXML
    private TableColumn<?, ?> colCrewmanAction;

    @FXML
    private TableColumn<?, ?> colBoatNo;

    @FXML
    private TableColumn<?, ?> colBoatRegistrationNo;

    @FXML
    private TableColumn<?, ?> colBoatModel;

    @FXML
    private TableColumn<?, ?> colBoatType;

    @FXML
    private TableColumn<?, ?> colBoatSattelitePhoneNo;

    @FXML
    private TableColumn<?, ?> colBoatAction;

    @FXML
    private JFXButton btnAddCrewman;

    @FXML
    private ComboBox<String> cbCrewLeader;

    @FXML
    private CheckBox cboxMorning;

    @FXML
    private JFXComboBox<String> cbCrewMembers;

    @FXML
    private CheckBox cboxEvening;

    @FXML
    private CheckBox cboxNight;

    @FXML
    private CheckBox cboxMonday;

    @FXML
    private CheckBox cboxTuesday;

    @FXML
    private CheckBox cboxWednesday;

    @FXML
    private CheckBox cboxThursday;

    @FXML
    private CheckBox cboxFriday;

    @FXML
    private CheckBox cboxSaturday;

    @FXML
    private CheckBox cboxSunday;

    @FXML
    private JFXTextField txtCrewmanName;

    @FXML
    private JFXTextField txtCrewmanAddress;

    @FXML
    private JFXTextField txtCrewmanEmail;

    @FXML
    private JFXTextField txtCrewmanNIC;

    @FXML
    private DatePicker txtCrewmanBOD;

    @FXML
    private JFXTextField txtContactNo;

    @FXML
    private Label lblCrewmanName;

    @FXML
    private Label lblAddress;

    @FXML
    private Label lblCrewmanBOD;

    @FXML
    private Label lblCrewmanNIC;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblCrewmanContactNo;
    @FXML
    private Label lblBoatRegNo;

    @FXML
    private Label lblBoatModel;

    @FXML
    private Label lblBoatType;

    @FXML
    private Label lblSatellitePhoneNo;
    @FXML
    private Label lblOwnerName;

    @FXML
    private Label lblOwnerNIC;

    @FXML
    private Label lblOwnerAddress;

    @FXML
    private Label lblOwnerContactNo;

    @FXML
    private Button btnWeekdays;

    @FXML
    private Button btnWeekend;

    @FXML
    private Button btnFullWeek;

    @FXML
    private Label lblCrewId;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnNext;

    private String selectedCrewmanId = null;
    private String newCrewmanId = null;

    private String selectedBoatId = null;
    private String newBoatId = null;

    private String selectedBoatOwnerId = null;
    private String newBoatOwnerId = null;

    private ObservableList<CrewmanTM> crewmenList = FXCollections.observableArrayList();

    private ObservableList<BoatTM> boatList = FXCollections.observableArrayList();

    private ObservableList<BoatOwnerTM> ownersList = FXCollections.observableArrayList();

    private ObservableList<CrewmanRegTM> crewmanRegList = FXCollections.observableArrayList();

    private ObservableList<BoatRegTM> boatsRegList = FXCollections.observableArrayList();

    int crewmanIndex;

    int boatIndex;

    int boatOwnerIndex;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        loadCBBoatsTypes();
        newCrewmanId = CrewmanModel.getLastId();
        newBoatId = BoatModel.getLastId();
        newBoatOwnerId = BoatOwnerModel.getLastId();

        if(CrewManageFormController.isBtnUpdatePressed){
            System.out.println("Button update was pressed! - " + CrewManageFormController.crew);

            loadTables(CrewManageFormController.crew);
        }

        setFieldsOnAction();
    }

    private void setFieldsOnAction() {
        txtCrewmanName.setOnAction((e) -> {
            txtCrewmanNIC.requestFocus();
        });
        txtCrewmanName.setOnKeyReleased((e) -> {
            if(!txtCrewmanName.getText().matches("[A-Z][a-z]*(?:\\s[A-Z][a-z]*)*(?:\\s[A-Z][a-z]*\\.?)*$")){
                lblCrewmanName.setText("Invalid Input!");
            }else{
                lblCrewmanName.setText(null);
            }
        });

        txtCrewmanNIC.setOnAction((e) -> {
            txtCrewmanAddress.requestFocus();
        });
        txtCrewmanNIC.setOnKeyReleased((e) -> {
            if(!(txtCrewmanNIC.getText().matches("^\\d{12}$") || txtCrewmanNIC.getText().matches("^\\d{9}[VX]$"))){
                lblCrewmanNIC.setText("Invalid Input!");
            }else {
                lblCrewmanNIC.setText(null);
            }
        });

        txtCrewmanAddress.setOnAction((e) -> {
            txtCrewmanBOD.requestFocus();
        });
        txtCrewmanAddress.setOnKeyReleased((e) -> {
            if(!(txtCrewmanAddress.getText().length() > 3)){
                lblAddress.setText("Invalid Input!");
            }else {
                lblAddress.setText(null);
            }
        });

        txtCrewmanBOD.setOnAction((e) -> {
            txtCrewmanEmail.requestFocus();
            String s = String.valueOf(txtCrewmanBOD.getValue());
            System.out.println(s);
            if(!String.valueOf(txtCrewmanBOD.getValue()).matches("^\\d{4}-\\d{2}-\\d{2}$")){
                lblCrewmanBOD.setText("Invalid Input!");
            }else {
                lblCrewmanBOD.setText(null);
            }
        });
        txtCrewmanBOD.setOnKeyReleased((e) -> {
            String s = String.valueOf(txtCrewmanBOD.getValue());
            if(!String.valueOf(txtCrewmanBOD.getValue()).matches("^\\d{4}-\\d{2}-\\d{2}$")){
                lblCrewmanBOD.setText("Invalid Input!");
            }else {
                lblCrewmanBOD.setText(null);
            }
        });

        txtCrewmanEmail.setOnAction((e) -> {
            txtContactNo.requestFocus();
        });
        txtCrewmanEmail.setOnKeyReleased((e) -> {
            if(!txtCrewmanEmail.getText().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")){
                lblEmail.setText("Invalid Input!");
            }else {
                lblEmail.setText(null);
            }
        });

        txtContactNo.setOnAction((e) -> {
            btnAddCrewman.fire();
            txtCrewmanName.requestFocus();
        });
        txtContactNo.setOnKeyReleased((e) -> {
            if(!txtContactNo.getText().matches("^(?:\\+94|0)[1-9]\\d{8}$")){
                lblCrewmanContactNo.setText("Invalid Input!");
            }else {
                lblCrewmanContactNo.setText(null);
            }
        });
/////////////////////////////////////////////////////////////
        txtBoatRegistrationNo.setOnAction((e) -> {
            txtBoatModel.requestFocus();
        });
        txtBoatRegistrationNo.setOnKeyReleased((e) -> {
            if(!(txtBoatRegistrationNo.getText().matches("^[A-Z]{2}-\\d{4}-[A-Z]{2}$") || txtBoatRegistrationNo.getText().matches("^[A-Z]{3}\\s\\d{4}$"))){
                lblBoatRegNo.setText("Invalid Input!");
            }else {
                lblBoatRegNo.setText(null);
            }
        });

        txtBoatModel.setOnAction((e) -> {
            cbBoatType.requestFocus();
        });

        cbBoatType.setOnAction((e) -> {
            txtBoatSattelitePhoneNo.requestFocus();
        });

        txtBoatSattelitePhoneNo.setOnAction((e) -> {
            btnAddBoat.fire();
            txtBoatRegistrationNo.requestFocus();
        });
        txtBoatSattelitePhoneNo.setOnKeyReleased((e) -> {
            if(!(txtBoatSattelitePhoneNo.getText().matches("^\\+?[0-9]{7,15}$") || txtBoatSattelitePhoneNo.getText() == null)){
                lblSatellitePhoneNo.setText("Invalid Input!");
            }else {
                lblSatellitePhoneNo.setText(null);
            }
        });

/////////////////////////////////////////////////////////////
        txtOwnerName.setOnAction((e) -> {
            txtOwnerNIC.requestFocus();
        });
        txtOwnerName.setOnKeyReleased((e) -> {
            if(!txtOwnerName.getText().matches("[A-Z][a-z]*(?:\\s[A-Z][a-z]*)*(?:\\s[A-Z][a-z]*\\.?)*$")){
                lblOwnerName.setText("Invalid Input!");
            }else {
                lblOwnerName.setText(null);
            }
        });

        txtOwnerNIC.setOnAction((e) -> {
            txtOwnerAddress.requestFocus();
        });
        txtOwnerNIC.setOnKeyReleased((e) -> {
            if(!(txtOwnerNIC.getText().matches("^\\d{12}$") || txtOwnerNIC.getText().matches("^\\d{9}[VX]$"))){
                lblOwnerNIC.setText("Invalid Input!");
            }else {
                lblOwnerNIC.setText(null);
            }
        });

        txtOwnerAddress.setOnAction((e) -> {
            txtOwnerContactNo.requestFocus();
        });
        txtOwnerAddress.setOnKeyReleased((e) -> {
            if(!(txtOwnerAddress.getText().length() > 3)){
                lblOwnerAddress.setText("Invalid Input!");
            }else {
                lblOwnerAddress.setText(null);
            }
        });

        txtOwnerContactNo.setOnAction((e) -> {
            btnAddOwner.fire();
            txtOwnerName.requestFocus();
        });
        txtOwnerContactNo.setOnKeyReleased((e) -> {
            if(!txtOwnerContactNo.getText().matches("^(?:\\+94|0)[1-9]\\d{8}$")){
                lblOwnerContactNo.setText("Invalid Input!");
            }else {
                lblOwnerContactNo.setText(null);
            }
        });
    }

    private void loadTables(String crewId) {
        try {
            Crew crew = CrewModel.getCrew(crewId);
            List<Crewman> crewmenList = CrewmanModel.getCrewmen(crewId);
            List<Boat> boatsList = BoatModel.getBoats(crewId);
            List<BoatOwner> boatOwnersList = BoatOwnerModel.getOwners(crewId);

            loadCrewmenTable(crewmenList);
            loadBoatsTable(boatsList);
            loadBoatOwnersTable(boatOwnersList, boatsList);
            loadCrewDetails(crew);
            //ToDo

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCrewDetails(Crew crew) {
        lblCrewId.setText(crew.getCrewId());
        lblCrewmenCount.setText(String.valueOf(crew.getCrewmenCount()));
        cbCrewLeader.setValue(crew.getLeader());
        setAvailableTimes(crew.getAvailableTimes());
        setAvailableDays(crew.getAvailableDays());
    }

    private void setAvailableDays(String availableDays) {
        CheckBox[] checkBoxes = {cboxMonday,cboxTuesday,cboxWednesday,cboxThursday,cboxFriday,cboxSaturday,cboxSunday};

        for(int i = 0; i < 7; i++){
            if (availableDays.charAt(i) == '1') {
                checkBoxes[i].setSelected(true);
            }
        }
    }

    private void setAvailableTimes(String availableTimes) {
        CheckBox[] checkBoxes = {cboxMorning,cboxEvening,cboxNight};

        for(int i = 0; i < 3; i++){
            if(availableTimes.charAt(i) == '1'){
                checkBoxes[i].setSelected(true);
            }
        }
    }

    private void loadBoatOwnersTable(List<BoatOwner> boatOwnersList, List<Boat> boatList) {
        for(Boat boat : boatList) {
            for (int i = 0; i < boatOwnersList.size(); i++) {
                BoatOwner boatOwner = boatOwnersList.get(i);
                if(boat.getOwnerId().equals(boatOwner.getOwnerId())) {
                    Button action = new Button("Remove");
                    setOwnerRemoveBtnOnAction(action);

                    this.ownersList.add(new BoatOwnerTM(
                            boat.getOwnerId(),
                            boat.getBoatId(),
                            boatOwner.getName(),
                            boatOwner.getNic(),
                            boatOwner.getAddress(),
                            boatOwner.getContactNo(),
                            action
                    ));
                }
            }
        }

        tableOwners.setItems(this.ownersList);
    }

    private void loadBoatsTable(List<Boat> boatsList) {
        for(Boat boat : boatsList){
            Button action = new Button("Remove");
            setBoatRemoveBtnOnAction(action);

            this.boatList.add(new BoatTM(
                    boat.getBoatId(),
                    boat.getRegistrationNo(),
                    boat.getModel(),
                    boat.getType(),
                    boat.getSattelitePhoneNo(),
                    action
            ));
        }
        tableBoats.setItems(this.boatList);
    }

    private void loadCrewmenTable(List<Crewman> crewmenList) {
        for(Crewman crewman : crewmenList){
            Button action = new Button("Remove");
            setCrewmanRemoveBtnOnAction(action);

            this.crewmenList.add(new CrewmanTM(
                    crewman.getCrewmanId(),
                    crewman.getName(),
                    crewman.getNic(),
                    crewman.getAddress(),
                    crewman.getBod(),
                    crewman.getEmail(),
                    crewman.getContactNo(),
                    action
            ));
        }
        tableCrewmens.setItems(this.crewmenList);
    }

    private void loadCBBoatsTypes() {
        ObservableList<String> boatTypes = FXCollections.observableArrayList("Commercial fishing vessel","Artisanal fishing vessel", "Recreational fishing vessel");
        cbBoatType.setItems(boatTypes);
        cbBoatType.setValue("Commercial fishing vessel");
    }

    private void setCellValueFactory() {
        colCrewmanNo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCrewmanName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCrewmanNIC.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colCrewmanAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCrewmanBOD.setCellValueFactory(new PropertyValueFactory<>("bod"));
        colCrewmanEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCrewmanContactNo.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        colCrewmanAction.setCellValueFactory(new PropertyValueFactory<>("action"));

        colBoatNo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colBoatRegistrationNo.setCellValueFactory(new PropertyValueFactory<>("registrationNo"));
        colBoatModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colBoatType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colBoatSattelitePhoneNo.setCellValueFactory(new PropertyValueFactory<>("sattelitePhoneNo"));
        colBoatAction.setCellValueFactory(new PropertyValueFactory<>("action"));

        colOwnerNo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colOwnerBoatNo.setCellValueFactory(new PropertyValueFactory<>("boatId"));
        colOwnerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colOwnerNIC.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colOwnerAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colOwnerContactNo.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        colOwnerAction.setCellValueFactory(new PropertyValueFactory<>("action"));

        colCrewmanIDReg.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCrewmanNameReg.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCrewmanNICReg.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colCrewmanEmailReg.setCellValueFactory(new PropertyValueFactory<>("email"));
        colContactNoReg.setCellValueFactory(new PropertyValueFactory<>("contactNo"));

        colBoatIdReg.setCellValueFactory(new PropertyValueFactory<>("id"));
        colBoatRegistrationNoReg.setCellValueFactory(new PropertyValueFactory<>("registrationNo"));
        colBoatModelReg.setCellValueFactory(new PropertyValueFactory<>("model"));
        colBoatOwnerIdReg.setCellValueFactory(new PropertyValueFactory<>("ownerId"));
        colBoatOwnerNameReg.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
    }

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        if(prase == 2 || prase == 3) {
            prase--;
        }

        if(prase == 1){
            btnBack.setDisable(true);

            paneVisible(1);
        }else if(prase == 2){
            btnNext.setText("Next");

            paneVisible(2);
        }
    }

    @FXML
    void btnNextOnAction(ActionEvent event) throws IOException {
        if(prase == 1 || prase == 2) {
            prase++;
        }else if(prase == 3){
            registerCrew();
        }


        if(prase == 2) {
            if(crewmenList.size() > 3 && crewmenList.size() < 13 && cbCrewLeader.getValue() != null) {
                btnBack.setDisable(false);
                loadCbCrewmembers();

                paneVisible(2);
            }else if(crewmenList.size() < 4){
                new Alert(Alert.AlertType.WARNING, "The crewmen count is must be higher than 3 !").show();
                prase--;
            }else if(crewmenList.size() > 12){
                new Alert(Alert.AlertType.WARNING, "The crewmen count is must be lower than 13 !").show();
                prase--;
            }else if(cbCrewLeader.getValue() == null){
                new Alert(Alert.AlertType.WARNING, "Please select a leader for this crew !").show();
                prase--;
            }
        }else if(prase == 3){
            if(boatList.size() > 0 && boatList.size() < 5 && ownersList.size() > 0 && ownersList.size() < 5){
                loadCrewDetails();
                loadBoatsDetails();

                if(CrewManageFormController.isBtnUpdatePressed){
                    btnNext.setText("Update");
                }else {
                    btnNext.setText("Register");
                }
            }else {
                new Alert(Alert.AlertType.WARNING, "Boats and Owners count must at least need to be 1").show();
                prase--;
            }


            paneVisible(3);
        }
    }

    private void loadCbCrewmembers() {
        ObservableList<String> crewmembers = FXCollections.observableArrayList();
        for(CrewmanTM crewmanTM : crewmenList){
            crewmembers.add(crewmanTM.getName());
        }
        cbCrewMembers.setItems(crewmembers);
    }

    private void loadCrewDetails() {
        try {
            //String id = CrewmanModel.getLastId();
            crewmanRegList.clear();

            for (int i = 0; i < crewmenList.size(); i++) {
                CrewmanTM crewmanTM = crewmenList.get(i);

                String id = crewmanTM.getId();
                String name = crewmanTM.getName();
                String nic = crewmanTM.getNic();
                String email = crewmanTM.getEmail();
                String contactNo = crewmanTM.getContactNo();

                CrewmanRegTM crewman = new CrewmanRegTM(id, name, nic, email, contactNo);
                crewmanRegList.add(crewman);
            }

            tableCrewDetailsReg.setItems(crewmanRegList);
            if(CrewManageFormController.isBtnUpdatePressed){
                lblCrewIdReg.setText(lblCrewId.getText());
            }else {
                lblCrewIdReg.setText(getNewId(CrewModel.getLastId()));
            }
            lblCrewmenCountReg.setText(String.valueOf(crewmenList.size()));
            lblCrewLeaderReg.setText(cbCrewLeader.getValue());
            lblAvailableTimesReg.setText(getAvailableTimes().replace("[", "").replace("]", ""));
            txtAvailableDaysReg.setText(getAvailableDays().replace("[", "").replace("]", ""));
        } catch (SQLException e){
            new Alert(Alert.AlertType.ERROR, "Ooops...Something Wendt Wrong!!!");
        }
    }

    private String getAvailableDays() {
        ArrayList<String> days = new ArrayList<>();
        if(cboxMonday.isSelected()){
            days.add("Monday");
        }
        if(cboxTuesday.isSelected()){
            days.add("Tuesday");
        }
        if(cboxWednesday.isSelected()){
            days.add("Wednesday");
        }
        if(cboxThursday.isSelected()){
            days.add("Thursday");
        }
        if(cboxFriday.isSelected()){
            days.add("Friday");
        }
        if(cboxSaturday.isSelected()){
            days.add("Saturday");
        }
        if(cboxSunday.isSelected()){
            days.add("Sunday");
        }

        return days.toString();
    }

    private String getAvailableTimes() {
        ArrayList<String> times = new ArrayList<>();
        if(cboxMorning.isSelected()){
            times.add("Morning");
        }
        if(cboxEvening.isSelected()){
            times.add("Evening");
        }
        if(cboxNight.isSelected()){
            times.add("Night");
        }

        return times.toString();
    }

    private void loadBoatsDetails() {
        boatsRegList.clear();

        for (int i = 0; i < boatList.size(); i++) {
            BoatTM boat = boatList.get(i);
            for(int j = 0; j < ownersList.size(); j++) {
                BoatOwnerTM owner = ownersList.get(i);
                if(boat.getId().equals(owner.getBoatId())) {

                    String boatId = boat.getId();
                    String registrationNo = boat.getRegistrationNo();
                    String model = boat.getModel();
                    String ownerId = owner.getId();
                    String ownerName = owner.getName();

                    BoatRegTM boatRegTM = new BoatRegTM(boatId, registrationNo, model, ownerId, ownerName);
                    boatsRegList.add(boatRegTM);
                    break;
                }
            }
        }
        tableOwnerReg.setItems(boatsRegList);
        lblBoatsCountReg.setText(String.valueOf(boatList.size()));
    }

    private void registerCrew() {
        try {
            Crew crew = getCrew();
            List<Crewman> crewmenList = getAllCrewmen();
            List<Boat> boatsList = getAllBoats();
            List<BoatOwner> boatOwnersList = getAllBoatOwners();

            if(btnNext.getText().equals("Register")) {
                boolean isCrewRegistered = CrewModel.registerCrew(crew, crewmenList, boatsList, boatOwnersList);

                if (isCrewRegistered) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Crew Registered Succesfully!").show();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Crew Not Registered!!").show();
                }

                System.out.println("registered");
            } else if (btnNext.getText().equals("Update")) {

                boolean isCrewUpdated = CrewModel.updateCrew(crew, crewmenList, boatsList, boatOwnersList);

                if (isCrewUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Crew Updated Succesfully!").show();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Crew Not Updated!!").show();
                }

                System.out.println("Update");
            }

        } catch (SQLException e){
            new Alert(Alert.AlertType.ERROR, "OOps something went wrong!!!").show();
        }
    }

    private Crew getCrew() {
        String crewId = lblCrewIdReg.getText();
        String leader = cbCrewLeader.getValue();
        Integer crewmenCount = Integer.parseInt(lblCrewmenCountReg.getText());
        Integer boatsCount = Integer.parseInt(lblBoatsCountReg.getText());
        String availableTimes = Daytimes.getAvailableTimesBinary(cboxMorning.isSelected(), cboxEvening.isSelected(), cboxNight.isSelected());
        String availableDays = Weekdays.getAvailableDaysBinary(
                cboxMonday.isSelected(),
                cboxTuesday.isSelected(),
                cboxWednesday.isSelected(),
                cboxThursday.isSelected(),
                cboxFriday.isSelected(),
                cboxSaturday.isSelected(),
                cboxSunday.isSelected()
        );
        String adminId = AdminModel.adminId;

        return new Crew(crewId,leader, crewmenCount, boatsCount, availableTimes, availableDays, adminId);
    }

    private List<BoatOwner> getAllBoatOwners() throws SQLException {
        List<BoatOwner> boatOwnerList = new ArrayList<>();

        String id = "";
        for(int i = 0; i < this.ownersList.size(); i++) {
            BoatOwnerTM boatOwnerTM = this.ownersList.get(i);

            if(id.equals(boatOwnerTM.getId())){
                continue;
            }

            id = boatOwnerTM.getId();
            String name = boatOwnerTM.getName();
            String nic = boatOwnerTM.getNic();
            String address = boatOwnerTM.getAddress();
            String contactNo = boatOwnerTM.getContactNo();

            BoatOwner boatOwner = new BoatOwner(id ,name, nic, address, contactNo);
            boatOwnerList.add(boatOwner);
        }

        return boatOwnerList;
    }

    private List<Boat> getAllBoats() {
        List<Boat> boatsList = new ArrayList<>();

        for(int i = 0; i < this.boatList.size(); i++) {
            BoatTM boatTM = this.boatList.get(i);
            BoatRegTM boatRegTM = boatsRegList.get(i);

            String boatId = boatRegTM.getId();
            String registrationNo = boatTM.getRegistrationNo();
            String model = boatTM.getModel();
            String type = boatTM.getType();
            String sattelitePhoneNo = boatTM.getSattelitePhoneNo();
            String ownerId = boatRegTM.getOwnerId();
            String crewId = lblCrewIdReg.getText();

            Boat boat = new Boat(boatId, registrationNo, model, type, sattelitePhoneNo, ownerId, crewId);
            boatsList.add(boat);
        }

        return boatsList;
    }

    private List<Crewman> getAllCrewmen() {
        List<Crewman> crewmenList = new ArrayList<>();

        for (int i = 0; i < this.crewmenList.size(); i++) {
            CrewmanTM crewman = this.crewmenList.get(i);
            CrewmanRegTM crewmanReg = crewmanRegList.get(i);

            String name = crewman.getName();
            String crewmanId = crewmanReg.getId();
            String nic = crewman.getNic();
            String address = crewman.getAddress();
            LocalDate bod = crewman.getBod();
            String email = crewman.getEmail();
            String contactNo = crewman.getContactNo();
            String crewId = lblCrewIdReg.getText();

            Crewman cm = new Crewman(crewmanId, name, nic, address, bod, email, contactNo, crewId);
            crewmenList.add(cm);
        }

        return crewmenList;
    }

    void paneVisible(int prase){
        Pane[] panes = {paneCrewDetails, paneBoatsDetails, paneRegistrationForm};

        for(int i = 1; i < 4; i++){
            if(prase == i){
                panes[i - 1].setVisible(true);
            }else{
                panes[i - 1].setVisible(false);
            }
        }
    }

    @FXML
    void btnAddBoatOnAction(ActionEvent event) {
        newBoatId = getNewId(newBoatId);
        String registrarionNo = txtBoatRegistrationNo.getText();
        String model = txtBoatModel.getText();
        String type = cbBoatType.getValue();
        String sattelitePhoneNo = txtBoatSattelitePhoneNo.getText();
        Button action = new Button("Remove");

        setBoatRemoveBtnOnAction(action);



        if(btnAddBoat.getText().equals("Change")){
            BoatTM boat = new BoatTM(selectedBoatId, registrarionNo, model, type, sattelitePhoneNo, action);
            boatList.remove(boatIndex);
            boatList.add(boatIndex, boat);
            tableBoats.setItems(boatList);
            btnAddBoat.setText("Add");
        }else{
            BoatTM boat = new BoatTM(newBoatId, registrarionNo, model, type, sattelitePhoneNo, action);
            boatList.add(boat);
            tableBoats.setItems(boatList);

        }
        btnBoatClearOnAction(event);
    }

    private void setBoatRemoveBtnOnAction(Button action) {
        action.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure want to remove this boat", yes, no).showAndWait();
            if(result.orElse(no) == yes){
                int index = tableBoats.getSelectionModel().getSelectedIndex();

                boatList.remove(index);
                try {
                    setBoatIds();
                } catch (SQLException ex) {
                    System.out.println("ex = - setBoatIds()");
                    ex.printStackTrace();
                }
                tableBoats.refresh();
            }
            btnAddBoat.setText("Add");
            btnBoatClearOnAction(e);
        });
    }

    private void setBoatIds() throws SQLException {
        newBoatId = BoatModel.getLastId();
        for(BoatTM boat : boatList){
            newBoatId = getNewId(newBoatId);
            boat.setId(newBoatId);
        }
    }

    @FXML
    void btnAddCrewmanOnAction(ActionEvent event) throws SQLException {
        if(!(lblCrewmanName.getText() == null &&
        lblCrewmanNIC.getText() == null &&
        lblAddress.getText() == null &&
        lblCrewmanBOD.getText() == null &&
        lblEmail.getText() == null &&
        lblCrewmanContactNo.getText() == null)){
            return;
        }

        newCrewmanId = getNewId(newCrewmanId);
        System.out.println(newCrewmanId);
        String name = txtCrewmanName.getText();
        String nic = txtCrewmanNIC.getText();
        String address = txtCrewmanAddress.getText();
        LocalDate bod = txtCrewmanBOD.getValue();
        String email = txtCrewmanEmail.getText();
        String contactNo = txtContactNo.getText();
        Button action = new Button("Remove");

        setCrewmanRemoveBtnOnAction(action);

        if (btnAddCrewman.getText().equals("Change")) {
            CrewmanTM crewman = new CrewmanTM(selectedCrewmanId, name, nic, address, bod, email, contactNo, action);
            crewmenList.remove(crewmanIndex);
            crewmenList.add(crewmanIndex, crewman);
            tableCrewmens.setItems(crewmenList);

            btnAddCrewman.setText("Add");
        }else {
            CrewmanTM crewman = new CrewmanTM(newCrewmanId, name, nic, address, bod, email, contactNo, action);
            crewmenList.add(crewman);
            tableCrewmens.setItems(crewmenList);
            lblCrewmenCount.setText(String.valueOf(crewmenList.size()));
        }
        clearCrewmanFields();
    }

    private void setCrewmanRemoveBtnOnAction(Button action) {
        action.setOnAction((e) -> {
           ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
           ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure want to remove this crewman", yes, no).showAndWait();
            if(result.orElse(no) == yes){
                int index = tableCrewmens.getSelectionModel().getSelectedIndex();

                crewmenList.remove(index);
                try {
                    setCrewmanIds();
                } catch (SQLException ex) {
                    System.out.println("ex - setCrewmanIds()");
                    ex.printStackTrace();
                }
                tableCrewmens.refresh();
                lblCrewmenCount.setText(String.valueOf(crewmenList.size()));
            }
            btnAddCrewman.setText("Add");
            btnClearCrewmanOnAction(e);
        });
    }

    private void setCrewmanIds() throws SQLException {
        newCrewmanId = CrewmanModel.getLastId();

        for (CrewmanTM crewman : crewmenList){
            newCrewmanId = getNewId(newCrewmanId);
            crewman.setId(newCrewmanId);
        }
    }

    @FXML
    void btnAddOwnerOnAction(ActionEvent event) {
        if(cbSelectBoat.getValue() == null){
            new Alert(Alert.AlertType.WARNING, "Choose the a boat!!!").show();
        }

        newBoatOwnerId = getNewId(newBoatOwnerId);
        String boatId = cbSelectBoat.getValue();
        String name = txtOwnerName.getText();
        String nic = txtOwnerNIC.getText();
        String address = txtOwnerAddress.getText();
        String contactNo = txtOwnerContactNo.getText();
        Button action = new Button("Remove");
        setOwnerRemoveBtnOnAction(action);

        if(btnAddOwner.getText().equals("Change")){
            BoatOwnerTM owner = new BoatOwnerTM(selectedBoatOwnerId, boatId, name, nic, address, contactNo, action);
            ownersList.remove(boatOwnerIndex);
            ownersList.add(boatOwnerIndex, owner);
            tableOwners.setItems(ownersList);
            btnAddOwner.setText("Add");
        }else{
            BoatOwnerTM owner = new BoatOwnerTM(newBoatOwnerId, boatId, name, nic, address, contactNo, action);
            ownersList.add(owner);
            tableOwners.setItems(ownersList);

        }
        btnOwnerClearOnAction(event);
    }

    private void setOwnerRemoveBtnOnAction(Button action) {
        action.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure want to remove this owner", yes, no).showAndWait();
            if(result.orElse(no) == yes){
                int index = tableOwners.getSelectionModel().getSelectedIndex();

                ownersList.remove(index);
                try {
                    setOwnerIds();
                } catch (SQLException ex) {
                    System.out.println("ex - setOwnerIds");
                    ex.printStackTrace();
                }
                tableOwners.refresh();
            }
            btnAddOwner.setText("Add");
            btnOwnerClearOnAction(e);
        });
    }

    private void setOwnerIds() throws SQLException {
        newBoatOwnerId = BoatOwnerModel.getLastId();
        for(BoatOwnerTM boatOwner : ownersList){
            newBoatOwnerId = getNewId(newBoatOwnerId);
            boatOwner.setId(newBoatOwnerId);
        }
    }

    @FXML
    void btnFullWeekOnAction(ActionEvent event) {
        cboxMonday.setSelected(true);
        cboxTuesday.setSelected(true);
        cboxWednesday.setSelected(true);
        cboxThursday.setSelected(true);
        cboxFriday.setSelected(true);
        cboxSaturday.setSelected(true);
        cboxSunday.setSelected(true);
    }

    @FXML
    void btnWeekdaysOnAction(ActionEvent event) {
        cboxMonday.setSelected(true);
        cboxTuesday.setSelected(true);
        cboxWednesday.setSelected(true);
        cboxThursday.setSelected(true);
        cboxFriday.setSelected(true);
        cboxSaturday.setSelected(false);
        cboxSunday.setSelected(false);
    }

    @FXML
    void btnWeekendOnAction(ActionEvent event) {
        cboxMonday.setSelected(false);
        cboxTuesday.setSelected(false);
        cboxWednesday.setSelected(false);
        cboxThursday.setSelected(false);
        cboxFriday.setSelected(false);
        cboxSaturday.setSelected(true);
        cboxSunday.setSelected(true);
    }

    @FXML
    void tableBoatsOnMouseClicked(MouseEvent event) {
        BoatTM selectedItem = tableBoats.getSelectionModel().getSelectedItem();
        boatIndex = tableBoats.getSelectionModel().getSelectedIndex();

        selectedBoatId = selectedItem.getId();
        txtBoatRegistrationNo.setText(selectedItem.getRegistrationNo());
        txtBoatModel.setText(selectedItem.getModel());
        cbBoatType.setValue(selectedItem.getType());
        txtBoatSattelitePhoneNo.setText(selectedItem.getSattelitePhoneNo());

        btnAddBoat.setText("Change");
    }

    @FXML
    void tableOwnersOnMouseClicked(MouseEvent event) {
        BoatOwnerTM selectedItem = tableOwners.getSelectionModel().getSelectedItem();
        boatOwnerIndex = tableOwners.getSelectionModel().getSelectedIndex();

        selectedBoatOwnerId = selectedItem.getId();
        cbSelectBoat.setValue(selectedItem.getBoatId());
        txtOwnerName.setText(selectedItem.getName());
        txtOwnerNIC.setText(selectedItem.getNic());
        txtOwnerAddress.setText(selectedItem.getAddress());
        txtOwnerContactNo.setText(selectedItem.getContactNo());

        btnAddOwner.setText("Change");
    }

    @FXML
    void cbCrewLeaderOnMouseClicked(MouseEvent event) {
        ObservableList<String> namesList = FXCollections.observableArrayList();

        for(CrewmanTM crewman : crewmenList){
            namesList.add(crewman.getName());
        }

        cbCrewLeader.setItems(namesList);
    }

    @FXML
    void tableCrewmensOnMouseClicked(MouseEvent event) {
        CrewmanTM selectedItem = tableCrewmens.getSelectionModel().getSelectedItem();
        crewmanIndex = tableCrewmens.getSelectionModel().getSelectedIndex();

        selectedCrewmanId = selectedItem.getId();
        txtCrewmanName.setText(selectedItem.getName());
        txtCrewmanNIC.setText(selectedItem.getNic());
        txtCrewmanAddress.setText(selectedItem.getAddress());
        txtCrewmanBOD.setValue(selectedItem.getBod());
        txtCrewmanEmail.setText(selectedItem.getEmail());
        txtContactNo.setText(selectedItem.getContactNo());

        btnAddCrewman.setText("Change");
    }

    @FXML
    void btnClearCrewmanOnAction(ActionEvent event) {
        clearCrewmanFields();
    }

    void clearCrewmanFields(){
        txtCrewmanName.setText(null);
        txtCrewmanNIC.setText(null);
        txtCrewmanAddress.setText(null);
        txtCrewmanBOD.setValue(null);
        txtCrewmanEmail.setText(null);
        txtContactNo.setText(null);

        btnAddCrewman.setText("Add");
    }

    @FXML
    void btnBoatClearOnAction(ActionEvent event) {
        txtBoatRegistrationNo.setText(null);
        txtBoatModel.setText(null);
        cbBoatType.setValue(null);
        txtBoatSattelitePhoneNo.setText(null);

        btnAddBoat.setText("Add");
    }

    @FXML
    void btnOwnerClearOnAction(ActionEvent event) {
        txtOwnerName.setText(null);
        txtOwnerAddress.setText(null);
        txtOwnerNIC.setText(null);
        txtOwnerContactNo.setText(null);

        btnAddOwner.setText("Add");
    }

    public void cbSelectBoatOnMouseClicked(MouseEvent mouseEvent) {
        ObservableList<String> boatNoList = FXCollections.observableArrayList();

        for(BoatTM boat: boatList){
            boatNoList.add(boat.getId());
        }
        cbSelectBoat.setItems(boatNoList);
    }

    @FXML
    void cbCrewMembersOnAction(ActionEvent event) {
        String selectedItem = cbCrewMembers.getSelectionModel().getSelectedItem();
        for(CrewmanTM crewmanTM:crewmenList){
            if(selectedItem.equals(crewmanTM.getName())){
                txtOwnerName.setText(crewmanTM.getName());
                txtOwnerNIC.setText(crewmanTM.getNic());
                txtOwnerAddress.setText(crewmanTM.getAddress());
                txtOwnerContactNo.setText(crewmanTM.getContactNo());
            }
        }
    }
}
