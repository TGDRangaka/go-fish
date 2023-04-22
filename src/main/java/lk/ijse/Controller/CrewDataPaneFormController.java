package lk.ijse.Controller;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import lk.ijse.dto.Crew;

public class CrewDataPaneFormController {

    @FXML
    private Label lblCrewId;

    @FXML
    private Label lblLeader;

    @FXML
    private Label lblCrewmenCount;

    @FXML
    private Label lblBoatsCount;

    @FXML
    private Label lblTotalCatches;

    @FXML
    private Label lblTotalCatchesWeight;

    @FXML
    private Label lblTotalCatchesPayments;

    @FXML
    private BarChart<String, Double> lineChart;


    public void setData(Crew crew, String totalCatchesCount, String totalCatchesWeight, String totalCatchesPayments, XYChart.Series<String, Double> series) {
        lblCrewId.setText(crew.getCrewId());
        lblLeader.setText(crew.getLeader());
        lblCrewmenCount.setText(String.valueOf(crew.getCrewmenCount()));
        lblBoatsCount.setText(String.valueOf(crew.getBoatsCount()));

        lblTotalCatches.setText(totalCatchesCount);
        lblTotalCatchesWeight.setText(totalCatchesWeight + "kg");
        lblTotalCatchesPayments.setText("Rs: " + totalCatchesPayments);

        lineChart.setAnimated(false);
        lineChart.getData().add(series);
    }
}
