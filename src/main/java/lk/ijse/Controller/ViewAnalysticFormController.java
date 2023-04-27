package lk.ijse.Controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lk.ijse.Model.CatchModel;
import lk.ijse.Model.FishModel;
import lk.ijse.dto.tm.AnalysisTM;
import lombok.SneakyThrows;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ViewAnalysticFormController implements Initializable {
    @FXML
    private JFXComboBox<String> cbTimePeriod;

    @FXML
    private DatePicker datePickerFrom;

    @FXML
    private DatePicker datePickerTo;
    @FXML
    private BarChart<String, Double> barchartCatchWeight;

    @FXML
    private CategoryAxis xAxisCatchWeight;

    @FXML
    private NumberAxis yAxisCatchWeight;

    @FXML
    private BarChart<String, Number> barchartCatchCount;

    @FXML
    private CategoryAxis xAxisCatchCount;

    @FXML
    private NumberAxis yAxisCatchCount;

    //      Tab 2
    @FXML
    private Label lblCrewId;

    @FXML
    private TableView<AnalysisTM> tableCrewCatch;

    @FXML
    private TableColumn<?, ?> colCrewId;

    @FXML
    private TableColumn<?, ?> colLeader;

    @FXML
    private TableColumn<?, ?> colCatchWeight;

    @FXML
    private BarChart<String, Double> barchartWeight;

    @FXML
    private BarChart<String, Number> barchartCount;

    @FXML
    private Label lblTotCatchesWeight;

    @FXML
    private Label lblTotCatchesCount;

    @FXML
    private Label lblTotPayments;

    @FXML
    private Label lblTotWorkedDays;

    @FXML
    private JFXComboBox<String> cbTimePeriod1;

    @FXML
    private DatePicker datePickerFrom1;

    @FXML
    private DatePicker datePickerTo1;

    //      Tab 3
    @FXML
    private Label lblFishId;

    @FXML
    private TableView<AnalysisTM> tableFishCatch;

    @FXML
    private TableColumn<?, ?> colFishId;

    @FXML
    private TableColumn<?, ?> colFishType;

    @FXML
    private TableColumn<?, ?> colCatchFishWeight;

    @FXML
    private BarChart<String, Double> barchartFishWeight;

    @FXML
    private BarChart<String, Number> barchartFishCount;

    @FXML
    private Label lblTotFishCatchesWeight;

    @FXML
    private Label lblCrewCount;

    @FXML
    private Label lblTotFishPayments;

    @FXML
    private Label lblMostCaughtDay;

    @FXML
    private JFXComboBox<String> cbTimePeriod2;

    @FXML
    private DatePicker datePickerFrom2;

    @FXML
    private DatePicker datePickerTo2;

    @FXML
    private VBox vboxFish;

    private List<String> crewIdList = new ArrayList<>();
    private ArrayList<AnchorPane> anchorPanes = new ArrayList<>();

    private ObservableList<AnalysisTM> crewList = FXCollections.observableArrayList();
    private ObservableList<AnalysisTM> fishList = FXCollections.observableArrayList();

    private AnalysisTM selectedRow = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
            Platform.runLater(() -> {
            try {
                loadComboBox(); //tab 1,2,3
                modifyComponets(); //tab 1,2,3
                loadCatchWeightBarChart(); //tab 1
                loadCatchCountBarChart(); //tab 1

                loadCellValueFactory(); //tab 2,3
                loadCrewTable(); //tab 2
                loadFishTable(); //tab 3

                setBarchartAndPanes(false, 792); //tab 3
                loadAllFishCatchWeightBarChart(); //tab 3
                loadAllFishCatchCountBarChart(); //tab 3
            }catch (SQLException e){
                System.out.println(e);
            }
        });
        }).start();
    }

    private void loadFishTable() throws SQLException {
        AnalysisTM allFish = FishModel.getAllFishCatch();
        fishList.add(allFish);
        selectedRow = allFish;

        List<AnalysisTM> list = FishModel.getFishCatch();
        for(AnalysisTM fish : list){
            fishList.add(fish);
        }

        tableFishCatch.setItems(fishList);
    }

    private void loadCellValueFactory() {
        colCrewId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLeader.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCatchWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));

        colFishId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFishType.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCatchFishWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
    }

    private void loadCrewTable() throws SQLException {
        List<AnalysisTM> crewList = CatchModel.getCrewCatch();

        for(AnalysisTM crew : crewList){
            this.crewList.add(crew);
        }

        tableCrewCatch.setItems(this.crewList);
    }

    private void loadComboBox() {
        ObservableList<String> timePeriodList = FXCollections.observableArrayList();
        timePeriodList.add("Daily");
        timePeriodList.add("Weekly");
        timePeriodList.add("Monthly");

        cbTimePeriod.setItems(timePeriodList);
        cbTimePeriod.setValue("Daily");

        cbTimePeriod1.setItems(timePeriodList);
        cbTimePeriod1.setValue("Daily");

        cbTimePeriod2.setItems(timePeriodList);
        cbTimePeriod2.setValue("Daily");
    }

    private void modifyComponets() {
        datePickerFrom.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isAfter(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #c0c0c0;"); // set a custom style for disabled cells
                }
            }
        });
        datePickerFrom.setOnAction((e) -> {
            try {
                loadCatchWeightBarChart();
                loadCatchCountBarChart();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        datePickerFrom.setValue(LocalDate.now().minusDays(20));

        datePickerTo.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isAfter(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #c0c0c0;"); // set a custom style for disabled cells
                }
            }
        });

        datePickerTo.setOnAction((e) -> {
            try {
                loadCatchWeightBarChart();
                loadCatchCountBarChart();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        datePickerTo.setValue(LocalDate.now());

        cbTimePeriod.setOnAction((e) -> {
            try {
                loadCatchWeightBarChart();
                loadCatchCountBarChart();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        xAxisCatchWeight.setAnimated(false);
        xAxisCatchCount.setAnimated(false);

        //  tab 2
        datePickerFrom1.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isAfter(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #c0c0c0;"); // set a custom style for disabled cells
                }
            }
        });
        datePickerFrom1.setOnAction((e) -> {
            try {
                loadCrewCatchWeightBarChart(selectedRow.getId());
                loadCrewCatchCountBarChart(selectedRow.getId());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        datePickerFrom1.setValue(LocalDate.now().minusDays(10));

        datePickerTo1.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isAfter(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #c0c0c0;"); // set a custom style for disabled cells
                }
            }
        });

        datePickerTo1.setOnAction((e) -> {
            try {
                loadCrewCatchWeightBarChart(selectedRow.getId());
                loadCrewCatchCountBarChart(selectedRow.getId());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        datePickerTo1.setValue(LocalDate.now());

        cbTimePeriod1.setOnAction((e) -> {
            try {
                loadCrewCatchWeightBarChart(selectedRow.getId());
                loadCrewCatchCountBarChart(selectedRow.getId());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        barchartWeight.getXAxis().setAnimated(false);
        barchartCount.getXAxis().setAnimated(false);

        // tab 3
        cbTimePeriod2.setDisable(true);
        datePickerFrom2.setDisable(true);
        datePickerTo2.setDisable(true);

        datePickerFrom2.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isAfter(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #c0c0c0;"); // set a custom style for disabled cells
                }
            }
        });
        datePickerFrom2.setOnAction((e) -> {
            try {
                loadFishCatchWeightBarChart(selectedRow.getId());
                loadFishCatchCountBarChart(selectedRow.getId());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        datePickerFrom2.setValue(LocalDate.now().minusDays(10));

        datePickerTo2.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isAfter(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #c0c0c0;"); // set a custom style for disabled cells
                }
            }
        });

        datePickerTo2.setOnAction((e) -> {
            try {
                loadFishCatchWeightBarChart(selectedRow.getId());
                loadFishCatchCountBarChart(selectedRow.getId());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        datePickerTo2.setValue(LocalDate.now());

        cbTimePeriod2.setOnAction((e) -> {
            try {
                loadFishCatchWeightBarChart(selectedRow.getId());
                loadFishCatchCountBarChart(selectedRow.getId());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        barchartFishWeight.getXAxis().setAnimated(false);
        barchartFishCount.getXAxis().setAnimated(false);
    }

    private void loadCatchWeightBarChart() throws SQLException {
        XYChart.Series<String, Double> series = new XYChart.Series();
        series.setName("Catch Weights");

        LocalDate fromDate = datePickerFrom.getValue();
        LocalDate toDate = datePickerTo.getValue();
        String period = cbTimePeriod.getValue();
        String time = "";
        int timePeriod = period.equals("Daily") ? 1 : period.equals("Weekly") ? 7 : 30;
        int datesGap = (int) ChronoUnit.DAYS.between(fromDate, toDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/dd");

        for(int i = datesGap; i > 0; i -= timePeriod){
            toDate = fromDate.plusDays(timePeriod);

            Double weight = CatchModel.getCatchWeight(fromDate, toDate);
            if(timePeriod == 1){
                time = toDate.format(formatter);
            }else{
                time = fromDate.format(formatter) + "-" + toDate.format(formatter);
            }

            series.getData().add(new XYChart.Data(time, weight));

            fromDate = toDate;
        }
        barchartCatchWeight.getData().clear();
        barchartCatchWeight.getData().addAll(series);
    }

    private void loadCatchCountBarChart() throws SQLException {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Catch Count");

        LocalDate fromDate = datePickerFrom.getValue();
        LocalDate toDate = datePickerTo.getValue();
        String period = cbTimePeriod.getValue();
        String time = "";
        int timePeriod = period.equals("Daily") ? 1 : period.equals("Weekly") ? 7 : 30;
        int datesGap = (int) ChronoUnit.DAYS.between(fromDate, toDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/dd");

        for(int i = datesGap; i > 0; i -= timePeriod){
            toDate = fromDate.plusDays(timePeriod);

            Integer count = CatchModel.getCatchCount(fromDate, toDate);
            if(timePeriod == 1){
                time = toDate.format(formatter);
            }else{
                time = fromDate.format(formatter) + "-" + toDate.format(formatter);
            }

            series.getData().add(new XYChart.Data(time, count));

            fromDate = toDate;
        }

        barchartCatchCount.getData().clear();
        barchartCatchCount.getData().addAll(series);
    }

    @FXML
    void tableCrewCatchOnMouseClicked(MouseEvent event) {
        AnalysisTM crew = tableCrewCatch.getSelectionModel().getSelectedItem();
        if(crew == null || crew.equals(selectedRow) ){
            return;
        }
        selectedRow = crew;

        lblCrewId.setText(crew.getId());

        try {
            loadCrewCatchWeightBarChart(crew.getId());
            loadCrewCatchCountBarChart(crew.getId());

            loadCrewLabels(crew.getId());
        }catch (SQLException e){
            System.out.println(e);
        }

    }

    private void loadCrewLabels(String crewId) throws SQLException {
        Double weight = CatchModel.getCatchWeight(crewId);
        lblTotCatchesWeight.setText(weight + "kg");

        Integer count = CatchModel.getCatchCount(crewId);
        lblTotCatchesCount.setText(String.valueOf(count));

        Double paymentAmount = CatchModel.getCatchPayments(crewId);
        lblTotPayments.setText("Rs: " + paymentAmount);

        String fish = FishModel.getMostCaughtFish(crewId);
        lblTotWorkedDays.setText(fish);
    }

    private void loadCrewCatchWeightBarChart(String crewId) throws SQLException {
        XYChart.Series<String, Double> series = new XYChart.Series();
        series.setName("Catch Weights");

        LocalDate fromDate = datePickerFrom1.getValue();
        LocalDate toDate = datePickerTo1.getValue();
        String period = cbTimePeriod1.getValue();
        String time = "";
        int timePeriod = period.equals("Daily") ? 1 : period.equals("Weekly") ? 7 : 30;
        int datesGap = (int) ChronoUnit.DAYS.between(fromDate, toDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/dd");

        for(int i = datesGap; i > 0; i -= timePeriod){
            toDate = fromDate.plusDays(timePeriod);

            Double weight = CatchModel.getCatchWeight(crewId, fromDate, toDate);
            if(timePeriod == 1){
                time = toDate.format(formatter);
            }else{
                time = fromDate.format(formatter) + "-" + toDate.format(formatter);
            }

            series.getData().add(new XYChart.Data(time, weight));

            fromDate = toDate;
        }

        barchartWeight.getData().clear();
        barchartWeight.getData().addAll(series);
    }

    private void loadCrewCatchCountBarChart(String crewId) throws SQLException {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Catch Count");

        LocalDate fromDate = datePickerFrom1.getValue();
        LocalDate toDate = datePickerTo1.getValue();
        String period = cbTimePeriod1.getValue();
        String time = "";
        int timePeriod = period.equals("Daily") ? 1 : period.equals("Weekly") ? 7 : 30;
        int datesGap = (int) ChronoUnit.DAYS.between(fromDate, toDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/dd");

        for(int i = datesGap; i > 0; i -= timePeriod){
            toDate = fromDate.plusDays(timePeriod);

            Integer count = CatchModel.getCatchCount(crewId, fromDate, toDate);
            if(timePeriod == 1){
                time = toDate.format(formatter);
            }else{
                time = fromDate.format(formatter) + "-" + toDate.format(formatter);
            }

            series.getData().add(new XYChart.Data(time, count));

            fromDate = toDate;
        }

        barchartCount.getData().clear();
        barchartCount.getData().addAll(series);
    }

    @FXML
    void tableFishCatchOnMouseClicked(MouseEvent event) {
        AnalysisTM fish = tableFishCatch.getSelectionModel().getSelectedItem();
        if(fish == null || fish.equals(selectedRow) ){
            return;
        }
        selectedRow = fish;

        lblFishId.setText(fish.getId());

        if(fish.getId().equals("F000")){
            cbTimePeriod2.setDisable(true);
            datePickerFrom2.setDisable(true);
            datePickerTo2.setDisable(true);

            setBarchartAndPanes(false, 792);

            try {
                loadAllFishCatchWeightBarChart();
                loadAllFishCatchCountBarChart();
            }catch (SQLException e){
                System.out.println(e);
            }

        }else {
            cbTimePeriod2.setDisable(false);
            datePickerFrom2.setDisable(false);
            datePickerTo2.setDisable(false);

            setBarchartAndPanes(true, 522);

            try {
                loadFishCatchWeightBarChart(fish.getId());
                loadFishCatchCountBarChart(fish.getId());
                loadFishLabels(fish.getId());
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    private void loadFishLabels(String id) throws SQLException {
        for(AnalysisTM fish : fishList){
            if(fish.getId().equals(id)){
                lblTotFishCatchesWeight.setText(fish.getWeight() + "kg");
                break;
            }
        }

        Integer crewCount = FishModel.getCrewCount(id);
        lblCrewCount.setText(String.valueOf(crewCount));

        Double totPayments = FishModel.getPayments(id);
        lblTotFishPayments.setText("Rs: " + totPayments);

        String fish = FishModel.getMostCaughtWeekday(id);
        lblMostCaughtDay.setText(fish);
    }

    private void loadFishCatchWeightBarChart(String fishId) throws SQLException {
        XYChart.Series<String, Double> series = new XYChart.Series();
        series.setName("Catch Weights");

        LocalDate fromDate = datePickerFrom2.getValue();
        LocalDate toDate = datePickerTo2.getValue();
        String period = cbTimePeriod2.getValue();
        String time = "";
        int timePeriod = period.equals("Daily") ? 1 : period.equals("Weekly") ? 7 : 30;
        int datesGap = (int) ChronoUnit.DAYS.between(fromDate, toDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/dd");

        for(int i = datesGap; i > 0; i -= timePeriod){
            toDate = fromDate.plusDays(timePeriod);

            Double weight = FishModel.getCatchWeight(fishId, fromDate, toDate);
            if(timePeriod == 1){
                time = toDate.format(formatter);
            }else{
                time = fromDate.format(formatter) + "-" + toDate.format(formatter);
            }

            series.getData().add(new XYChart.Data(time, weight));

            fromDate = toDate;
        }

        barchartFishWeight.getData().clear();
        barchartFishWeight.getData().addAll(series);
    }

    private void loadFishCatchCountBarChart(String fishId) throws SQLException {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Catch Count");

        LocalDate fromDate = datePickerFrom2.getValue();
        LocalDate toDate = datePickerTo2.getValue();
        String period = cbTimePeriod2.getValue();
        String time = "";
        int timePeriod = period.equals("Daily") ? 1 : period.equals("Weekly") ? 7 : 30;
        int datesGap = (int) ChronoUnit.DAYS.between(fromDate, toDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/dd");

        for(int i = datesGap; i > 0; i -= timePeriod){
            toDate = fromDate.plusDays(timePeriod);

            Integer count = FishModel.getCatchCount(fishId, fromDate, toDate);
            if(timePeriod == 1){
                time = toDate.format(formatter);
            }else{
                time = fromDate.format(formatter) + "-" + toDate.format(formatter);
            }

            series.getData().add(new XYChart.Data(time, count));

            fromDate = toDate;
        }

        barchartFishCount.getData().clear();
        barchartFishCount.getData().addAll(series);
    }

    private void loadAllFishCatchWeightBarChart() throws SQLException {
        XYChart.Series<String, Double> series = FishModel.getCatchWeightSeries();
        series.setName("Catch Weights");

        barchartFishWeight.getData().clear();
        barchartFishWeight.getData().addAll(series);
    }

    private void loadAllFishCatchCountBarChart() throws SQLException {
        XYChart.Series<String, Number> series = FishModel.getCatchCountSeries();
        series.setName("Catch Count");

        barchartFishCount.getData().clear();
        barchartFishCount.getData().addAll(series);
    }

    private void setBarchartAndPanes(boolean visible, int width){
        vboxFish.setVisible(visible);
        barchartFishWeight.setPrefWidth(width);
        barchartFishCount.setPrefWidth(width);
    }
}
