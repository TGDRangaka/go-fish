package lk.ijse.Controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import java.util.ArrayList;
import java.util.Collection;
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

    private List<String> crewIdList = new ArrayList<>();
    private ArrayList<AnchorPane> anchorPanes = new ArrayList<>();

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadComboBox();
        modifyComponets();
        loadCatchWeightBarChart();
        loadCatchCountBarChart();
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

}
