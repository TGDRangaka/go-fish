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
import lk.ijse.util.CrudUtil;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

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
    private JFXRadioButton rbtnYes;

    @FXML
    private JFXRadioButton rbtnNo;

    @FXML
    private JFXTextField txtOwnerName;

    @FXML
    private JFXTextField txtOwnerAddress;

    @FXML
    private JFXTextField txtOwnerNIC;

    @FXML
    private JFXTextField txtOwnerContactNo;

    @FXML
    private JFXComboBox<Integer> cbSelectBoat;

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

    private int crewmanNo = 0;
    private int boatNo = 0;
    private int boatOwnerNo = 0;

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
    }

    private void loadCBBoatsTypes() {
        ObservableList<String> boatTypes = FXCollections.observableArrayList("Commercial fishing vessel","Artisanal fishing vessel", "Recreational fishing vessel");
        cbBoatType.setItems(boatTypes);
    }

    private void setCellValueFactory() {
        colCrewmanNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        colCrewmanName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCrewmanNIC.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colCrewmanAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCrewmanBOD.setCellValueFactory(new PropertyValueFactory<>("bod"));
        colCrewmanEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCrewmanContactNo.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        colCrewmanAction.setCellValueFactory(new PropertyValueFactory<>("action"));

        colBoatNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        colBoatRegistrationNo.setCellValueFactory(new PropertyValueFactory<>("registrationNo"));
        colBoatModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colBoatType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colBoatSattelitePhoneNo.setCellValueFactory(new PropertyValueFactory<>("sattelitePhoneNo"));
        colBoatAction.setCellValueFactory(new PropertyValueFactory<>("action"));

        colOwnerNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        colOwnerBoatNo.setCellValueFactory(new PropertyValueFactory<>("boatNo"));
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
            btnBack.setDisable(false);

            paneVisible(2);
        }else if(prase == 3){
            loadCrewDetails();
            loadBoatsDetails();

            btnNext.setText("Register");

            paneVisible(3);
        }
    }

    private void loadCrewDetails() {
        try {
            String id = CrewmanModel.getLastId();
            crewmanRegList.clear();

            for (int i = 0; i < crewmenList.size(); i++) {
                CrewmanTM crewmanTM = crewmenList.get(i);

                id = getNewId(id);
                String name = crewmanTM.getName();
                String nic = crewmanTM.getNic();
                String email = crewmanTM.getEmail();
                String contactNo = crewmanTM.getContactNo();

                CrewmanRegTM crewman = new CrewmanRegTM(id, name, nic, email, contactNo);
                crewmanRegList.add(crewman);
            }

            tableCrewDetailsReg.setItems(crewmanRegList);
            lblCrewIdReg.setText(getNewId(CrewModel.getLastId()));
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
        try{
            boatsRegList.clear();
            String boatId = BoatModel.getLastId();
            String ownerId = BoatOwnerModel.getLastId();

            for (int i = 0; i < boatList.size(); i++) {
                BoatTM boat = boatList.get(i);
                BoatOwnerTM owner = ownersList.get(i);

                boatId = getNewId(boatId);
                String registrationNo = boat.getRegistrationNo();
                String model = boat.getModel();
                ownerId = getNewId(ownerId);
                String ownerName = owner.getName();

                BoatRegTM boatRegTM = new BoatRegTM(boatId, registrationNo, model, ownerId, ownerName);
                boatsRegList.add(boatRegTM);
            }
            tableOwnerReg.setItems(boatsRegList);
            lblBoatsCountReg.setText(String.valueOf(boatList.size()));
        } catch (SQLException e){
            new Alert(Alert.AlertType.ERROR, "Ooops...Something Wendt Wrong!!!");
        }
    }

    private void registerCrew() {
        try {
            Crew crew = getCrew();
            List<Crewman> crewmenList = getAllCrewmen();
            List<Boat> boatsList = getAllBoats();
            List<BoatOwner> boatOwnersList = getAllBoatOwners();

            boolean isCrewRegistered = CrewModel.registerCrew(crew, crewmenList, boatsList, boatOwnersList);

            if (isCrewRegistered){
                new Alert(Alert.AlertType.CONFIRMATION, "Crew Registered Succesfully!").show();
            }else {
                new Alert(Alert.AlertType.WARNING, "Crew Not Registered!!").show();
            }

            System.out.println("registered");
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
        String id = BoatOwnerModel.getLastId();

        for(int i = 0; i < this.ownersList.size(); i++) {
            BoatOwnerTM boatOwnerTM = this.ownersList.get(i);

            id = getNewId(id);
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

            Boat boat = new Boat(boatId, registrationNo, model, type, sattelitePhoneNo, ownerId);
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

    String getNewId(String lastId){
        String[] t = lastId.split("0", 3);
        int len = t[0].length();
        String[] ar = new String[0];
        int lastIndex = 0;
        int newIndex = 0;
        String zeros = "";

        if(lastId.charAt(len + 1) == '0') {
            ar = lastId.split("0", 3);
            int count = 1;

            lastIndex = Integer.parseInt(ar[2]);
            newIndex = lastIndex + 1;

            if (lastIndex == 99) count = 0;

            for (int i = 0; i <= count; i++) {
                zeros += "0";
            }
        }else{
            ar = lastId.split("0", 2);

            lastIndex = Integer.parseInt(ar[1]);
            newIndex = lastIndex + 1;

            for (int i = 0; i <= 0; i++) {
                zeros += "0";
            }
        }

        return ar[0] + zeros + newIndex;
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
        String registrarionNo = txtBoatRegistrationNo.getText();
        String model = txtBoatModel.getText();
        String type = cbBoatType.getValue();
        String sattelitePhoneNo = txtBoatSattelitePhoneNo.getText();
        Button action = new Button("Remove");

        setBoatRemoveBtnOnAction(action);



        if(btnAddBoat.getText().equals("Change")){
            BoatTM boat = new BoatTM(boatIndex + 1, registrarionNo, model, type, sattelitePhoneNo, action);
            boatList.remove(boatIndex);
            boatList.add(boatIndex, boat);
            tableBoats.setItems(boatList);
            btnAddBoat.setText("Add");
        }else{
            boatNo++;
            BoatTM boat = new BoatTM(boatNo, registrarionNo, model, type, sattelitePhoneNo, action);
            boatList.add(boat);
            tableBoats.setItems(boatList);

        }
        if(boatList.size() > 1){
            paneYesNo.setDisable(false);
            cbSelectBoat.setDisable(false);
            paneOwnerDetails.setDisable(true);
        }else{
            paneYesNo.setDisable(true);
            cbSelectBoat.setDisable(true);
            paneOwnerDetails.setDisable(false);
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
                setBoatNo();
                tableBoats.refresh();
            }
            if(boatList.size() > 1){
                paneYesNo.setDisable(false);
                cbSelectBoat.setDisable(false);
                paneOwnerDetails.setDisable(true);
            }else{
                paneYesNo.setDisable(true);
                cbSelectBoat.setDisable(true);
                paneOwnerDetails.setDisable(false);
            }
            btnAddBoat.setText("Add");
            btnBoatClearOnAction(e);
        });
    }

    @FXML
    void btnAddCrewmanOnAction(ActionEvent event) {
        String name = txtCrewmanName.getText();
        String nic = txtCrewmanNIC.getText();
        String address = txtCrewmanAddress.getText();
        LocalDate bod = txtCrewmanBOD.getValue();
        String email = txtCrewmanEmail.getText();
        String contactNo = txtContactNo.getText();
        Button action = new Button("Remove");

        setCrewmanRemoveBtnOnAction(action);

        if (btnAddCrewman.getText().equals("Change")) {
            CrewmanTM crewman = new CrewmanTM(crewmanIndex + 1, name, nic, address, bod, email, contactNo, action);
            crewmenList.remove(crewmanIndex);
            crewmenList.add(crewmanIndex, crewman);
            tableCrewmens.setItems(crewmenList);

            btnAddCrewman.setText("Add");
        }else {
            crewmanNo++;
            CrewmanTM crewman = new CrewmanTM(crewmanNo, name, nic, address, bod, email, contactNo, action);
            crewmenList.add(crewman);
            tableCrewmens.setItems(crewmenList);
            lblCrewmenCount.setText(String.valueOf(crewmenList.size()));
        }
        clearCrewmanFields();
    }

    private void setCrewmanNo() {
        int no = 1;
        for(CrewmanTM crewman: crewmenList){
            crewman.setNo(no++);
        }
    }

    private void setBoatNo(){
        int no = 1;
        for(BoatTM boat: boatList){
            boat.setNo(no++);
        }
    }

    private void setOwnerNo() {
        int no = 1;
        for(BoatOwnerTM boatOwner: ownersList){
            boatOwner.setNo(no++);
        }
    }

    private void setCrewmanRemoveBtnOnAction(Button action) {
        action.setOnAction((e) -> {
           ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
           ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure want to remove this crewman", yes, no).showAndWait();
            if(result.orElse(no) == yes){
                int index = tableCrewmens.getSelectionModel().getSelectedIndex();

                crewmenList.remove(index);
                setCrewmanNo();
                tableCrewmens.refresh();
                lblCrewmenCount.setText(String.valueOf(crewmenList.size()));
            }
            btnAddCrewman.setText("Add");
            btnClearCrewmanOnAction(e);
        });
    }

    @FXML
    void btnAddOwnerOnAction(ActionEvent event) {
        Integer boatNo = cbSelectBoat.getValue();
        String name = txtOwnerName.getText();
        String nic = txtOwnerNIC.getText();
        String address = txtOwnerAddress.getText();
        String contactNo = txtOwnerContactNo.getText();
        Button action = new Button("Remove");
        setOwnerRemoveBtnOnAction(action);

        if(btnAddOwner.getText().equals("Change")){
            BoatOwnerTM owner = new BoatOwnerTM(boatOwnerIndex + 1, boatNo, name, nic, address, contactNo, action);
            ownersList.remove(boatOwnerIndex);
            ownersList.add(boatOwnerIndex, owner);
            tableOwners.setItems(ownersList);
            btnAddOwner.setText("Add");
        }else{
            boatOwnerNo++;
            BoatOwnerTM owner = new BoatOwnerTM(boatOwnerNo, boatNo, name, nic, address, contactNo, action);
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
                setOwnerNo();
                tableOwners.refresh();
            }
            btnAddOwner.setText("Add");
            btnOwnerClearOnAction(e);
        });
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

        cbSelectBoat.setValue(selectedItem.getBoatNo());
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
    }

    @FXML
    void btnBoatClearOnAction(ActionEvent event) {
        txtBoatRegistrationNo.setText(null);
        txtBoatModel.setText(null);
        cbBoatType.setValue(null);
        txtBoatSattelitePhoneNo.setText(null);
    }

    @FXML
    void btnOwnerClearOnAction(ActionEvent event) {
        txtOwnerName.setText(null);
        txtOwnerAddress.setText(null);
        txtOwnerNIC.setText(null);
        txtOwnerContactNo.setText(null);
    }

    public void cbSelectBoatOnMouseClicked(MouseEvent mouseEvent) {
        ObservableList<Integer> boatNoList = FXCollections.observableArrayList();

        for(BoatTM boat: boatList){
            boatNoList.add(boat.getNo());
        }
        cbSelectBoat.setItems(boatNoList);
    }

    @FXML
    void rBtnNoOnMouseClicked(MouseEvent event) {
        if(rbtnNo.isSelected()){
            rbtnYes.setSelected(false);

            cbSelectBoat.setDisable(false);
            paneOwnerDetails.setDisable(false);
        }
    }

    @FXML
    void rBtnYesOnMouseClicked(MouseEvent event) {
        if(rbtnYes.isSelected()){
            rbtnNo.setSelected(false);

            cbSelectBoat.setDisable(true);
            paneOwnerDetails.setDisable(false);
        }
    }
}
