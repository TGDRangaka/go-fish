package lk.ijse.Controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lk.ijse.Model.CatchModel;
import lk.ijse.Model.CrewModel;
import lk.ijse.dto.Crew;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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


    @FXML
    private VBox vbox;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadComboBox();
        modifyComponets();
        loadCatchWeightBarChart();
        loadCatchCountBarChart();

        new Thread(){
            public void run() {
                Platform.runLater(() -> {
                    try {
                        List<String> crewIds = CrewModel.getCrewIds();
                        for (String crewId : crewIds){
                            addNewPane(crewId);
                        }
                    } catch (SQLException | IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }.start();

    }

    private void loadComboBox() {
        ObservableList<String> timePeriodList = FXCollections.observableArrayList();
        timePeriodList.add("Daily");
        timePeriodList.add("Weekly");
        timePeriodList.add("Monthly");
        cbTimePeriod.setItems(timePeriodList);
        cbTimePeriod.setValue("Daily");
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
        datePickerFrom.setValue(LocalDate.now().minusDays(15));

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

    private void addNewPane(String crewId) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/crew_data_pane_form.fxml"));
        AnchorPane anchorPane = fxmlLoader.load();

        CrewDataPaneFormController crewPane = fxmlLoader.getController();

        Crew crew = CrewModel.getCrew(crewId);
        String totalCatchesCount = CatchModel.getCatchCount(crewId);
        String totalCatchesWeight = CatchModel.getCatchWeight(crewId);
        String totalCatchesPayments = CatchModel.getCatchPayments(crewId);

        LocalDate date = LocalDate.now().minusDays(20);
        XYChart.Series<String, Double> series = new XYChart.Series();
        series.setName("Catch Weight");
        for (int i = 1; i <= 20; i++) {
            List<String> data = CatchModel.getCatchweight(crewId, date);

            Double weight = Double.valueOf(data.get(0));
            LocalDate day = LocalDate.parse(data.get(1));
            series.getData().add(new XYChart.Data<>(day.format(DateTimeFormatter.ofPattern("MM/dd")), weight));

            date = date.plusDays(1);
        }

        crewPane.setData(crew, totalCatchesCount, totalCatchesWeight, totalCatchesPayments, series);

        vbox.getChildren().add(anchorPane);
    }
}
