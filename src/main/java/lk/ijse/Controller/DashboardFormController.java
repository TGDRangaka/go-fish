package lk.ijse.Controller;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.chart.ChartData;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lk.ijse.Model.CatchModel;
import lk.ijse.Model.CrewModel;
import lk.ijse.Model.FishModel;
import lk.ijse.dto.Fish;
import lk.ijse.dto.Weather;
import lk.ijse.dto.tm.FishPricesTM;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DashboardFormController implements Initializable {
    @FXML
    private Pane paneChart;
    @FXML
    private Pane paneRadialChart;

    @FXML
    private Label lblCatchesCount;

    @FXML
    private Label lblCatchesWeight;

    @FXML
    private Label lblCatchesPayments;

    @FXML
    private Label lblRegisteredCrewsCount;


    @FXML
    private ImageView imgWeather1;

    @FXML
    private Label lblWeather1Condition;

    @FXML
    private Label lblWeather1Date;

    @FXML
    private Label lblWeather1Temp;

    @FXML
    private Label lblWeather1Wind;

    @FXML
    private ImageView imgWeather2;

    @FXML
    private Label lblWeather2Condition;

    @FXML
    private Label lblWeather2Date;

    @FXML
    private Label lblWeather2Temp;

    @FXML
    private Label lblWeather2Wind;

    @FXML
    private ImageView imgWeather3;

    @FXML
    private Label lblWeather3Condition;

    @FXML
    private Label lblWeather3Date;

    @FXML
    private Label lblWeather3Temp;

    @FXML
    private Label lblWeather3Wind;


    private Tile radialChartTile;

    @FXML
    private TableView<FishPricesTM> tableFishPrices;

    @FXML
    private TableColumn<?, ?> colFishType;

    @FXML
    private TableColumn<?, ?> colUnitWeight;

    @FXML
    private TableColumn<?, ?> colUnitPrice;


    @FXML
    private BarChart<String, Double> barChart;
    @FXML
    private CategoryAxis x;
    @FXML
    private NumberAxis y;

    private List<Weather> weathers = null;

    private ObservableList<FishPricesTM> fishPricesTMS = FXCollections.observableArrayList();

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(){
            public void run() {
                Platform.runLater(() -> {
                    try {
                        loadCellValueFactory();
                        loadRadialChart();
                        loadLabels();
                        loadBarChart();
                        loadPriceTable();
                        loadWeather();
                    }catch (Exception e) {
                        System.out.println("error!");
                    }
                });
            }
        }.start();
    }

    private void loadWeather() throws IOException, InterruptedException {
        if(weathers == null) {
            weathers = getWeather();
        }
        weathers.remove(0);

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/dd");

        lblWeather1Date.setText(today.format(formatter));
        lblWeather1Temp.setText(weathers.get(0).getTemp());
        lblWeather1Condition.setText(weathers.get(0).getCondition());
        lblWeather1Wind.setText(weathers.get(0).getWindSpeed() + "km/h");
        imgWeather1.setImage(new Image("F:/Github/go-fish/src/main/resources/img/" + weathers.get(0).getIconURL()));

        lblWeather2Date.setText(today.plusDays(1).format(formatter));
        lblWeather2Temp.setText(weathers.get(1).getTemp());
        lblWeather2Condition.setText(weathers.get(1).getCondition());
        lblWeather2Wind.setText(weathers.get(1).getWindSpeed() + "km/h");
        imgWeather2.setImage(new Image("F:/Github/go-fish/src/main/resources/img/" + weathers.get(1).getIconURL()));

        lblWeather3Date.setText(today.plusDays(2).format(formatter));
        lblWeather3Temp.setText(weathers.get(2).getTemp());
        lblWeather3Condition.setText(weathers.get(2).getCondition());
        lblWeather3Wind.setText(weathers.get(2).getWindSpeed() + "km/h");
        imgWeather3.setImage(new Image("F:/Github/go-fish/src/main/resources/img/" + weathers.get(2).getIconURL()));
    }

    private void loadCellValueFactory() {
        colFishType.setCellValueFactory(new PropertyValueFactory<>("fishType"));
        colUnitWeight.setCellValueFactory(new PropertyValueFactory<>("unitWeight"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
    }

    private void loadPriceTable() throws SQLException {
        List<Fish> fishList = FishModel.getAllFish();
        for(Fish fish : fishList){
            FishPricesTM fishPricesTM = new FishPricesTM(fish.getFishType(), fish.getUnitWeight(), fish.getUnitPrice());
            fishPricesTMS.add(fishPricesTM);
        }
        tableFishPrices.setItems(fishPricesTMS);
    }

    private void loadBarChart() throws SQLException {
        XYChart.Series<String, Double> series4 = new XYChart.Series();
        series4.setName("Past Days Catches Weights");

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
        for (int i = 7; i >= 1; i--) {
            LocalDate date = today.minusDays(i);
            String formattedDate = date.format(formatter);
            Double weight = CatchModel.getCatchWeight(date);

            series4.getData().add(new XYChart.Data(formattedDate, weight));

        }
        barChart.setAnimated(false);
        barChart.getData().addAll(series4);
    }

    private void loadLabels() throws SQLException {
        Integer catchesCount = CatchModel.getCatchCount(LocalDate.now());
        lblCatchesCount.setText(String.valueOf(catchesCount));

        Double catchesWeight = CatchModel.getCatchWeight(LocalDate.now());
        lblCatchesWeight.setText(String.valueOf(catchesWeight) + "kg");

        Double catchPayments = CatchModel.getCatchPayments(LocalDate.now());
        lblCatchesPayments.setText("Rs:" + String.valueOf(catchPayments));

        Integer totRegisteredCrews = CrewModel.getAllCrew().size();
        lblRegisteredCrewsCount.setText(String.valueOf(totRegisteredCrews));
    }

    private void loadRadialChart() throws SQLException {
        List<String> data = CatchModel.getTopFiveFishDate(LocalDate.now());
        List<ChartData> chartDataList= new ArrayList<>();
        List<Color> colorList = new ArrayList<>();

        colorList.add(Color.valueOf("#38C190"));
        colorList.add(Color.valueOf("#00A995"));
        colorList.add(Color.valueOf("#009192"));
        colorList.add(Color.valueOf("#007886"));
        colorList.add(Color.valueOf("#235F72"));
        for(int i = 0, j = 0; i < data.size(); i++,j++){
            Double weight = Double.valueOf(data.get(i));
            String fishType = data.get(++i);
            Color color = colorList.get(j);
            chartDataList.add(new ChartData(fishType, weight, color));
        }

        radialChartTile = TileBuilder.create()
                .skinType(Tile.SkinType.RADIAL_CHART)
                .prefSize(paneRadialChart.getPrefWidth(), paneRadialChart.getPrefHeight())
                .title("Today Top 5 Caught Fishes")
                .animated(true)
                .backgroundColor(Color.TRANSPARENT)
                .textVisible(false)
                .chartData(chartDataList)
                .build();
        paneRadialChart.getChildren().add(radialChartTile);
    }

    private List<Weather> getWeather() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://weatherapi-com.p.rapidapi.com/forecast.json?q=Mirissa&days=14"))
                .header("X-RapidAPI-Key", "b01129e8fcmsh04f353984676b9fp1b2e56jsn11e9ba0e2ca6")
                .header("X-RapidAPI-Host", "weatherapi-com.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        String s = response.body();
        String[] split = s.split("[\"][d][a][t][e][\"]");

        List<Weather> weathers = new ArrayList<>();

        for (String t : split) {
            String[] ar = t.split(",");
            int hour = 0;


            String temp = "";
            String condition = "";
            String windSpeed = "";
            String iconURL = "";

            for(String v : ar){
                String[] text = v.split("[:]");

                if(text[0].equals("\"time\"")){
                    hour++;
//                    System.out.println(v);
                }


                if(hour == 12){
                    if(text[0].equals("\"temp_c\"")){
//                        System.out.println(v);
                        String tempDouble = v.replaceAll("\"temp_c\":", "");
//                        System.out.println(tempDouble);
                        if(tempDouble.contains(".")) {
                            temp = tempDouble.substring(0, tempDouble.indexOf("."));
                        }else{
                            temp = tempDouble;
                        }
                    }else if(text[0].equals("\"condition\"")){
//                        System.out.println(v);
                        condition = v.replace("{", "").replaceAll("\"condition\":\"text\":\"", "").replace("\"", "");
                        condition = filterCondition(condition);
                        iconURL = condition + ".png";
                    }else if(text[0].equals("\"wind_kph\"")){
//                        System.out.println(v);
                        windSpeed = v.replaceAll("\"wind_kph\":", "");
                    }
                }

            }
            Weather weather = new Weather(temp, condition, windSpeed, iconURL);
            weathers.add(weather);
//            System.out.println("/////////next dayf////////");
        }

        return weathers;
    }

    private String filterCondition(String condition) {
        if(condition.contains("rain") || condition.contains("Rain")){
            return "Rainy";
        }else if(condition.contains("sunny") || condition.contains("Sunny")){
            return "Sunny";
        }else if(condition.contains("cloud") || condition.contains("[cloudy]") || condition.contains("Cloud") || condition.contains("clear")){
            return "Cloudy";
        }else if(condition.contains("storm") || condition.contains("Storm")){
            return "Storm";
        }else{
            return "Clear";
        }
    }
//
//    public void loadWeather() {
//        Image rainy = new Image("F:/Github/go-fish/src/main/resources/img/rain.gif");
//        Image storm = new Image("F:/Github/go-fish/src/main/resources/img/storm.gif");
//        Image sunny = new Image("F:/Github/go-fish/src/main/resources/img/sun.gif");
//        Image wind = new Image("F:/Github/go-fish/src/main/resources/img/wind.gif");
//        Image cloudy = new Image("F:/Github/go-fish/src/main/resources/img/cloudy.gif");
//        List<Image> images = new ArrayList<>();
//        images.add(sunny); images.add(cloudy); images.add(rainy); images.add(storm); images.add(wind);
//        String[] weatherConditions = {"sunny", "cloudy", "rainy", "stormy", "windy"};
//        int minTemp = 15;
//        int maxTemp = 40;
//        int minWindSpeed = 0;
//        int maxWindSpeed = 50;
//
//        ImageView[] imageViews = {imgWeather1, imgWeather2, imgWeather3, imgWeather4, imgWeather5, imgWeather6, imgWeather7};
//        for (int i = 0; i < 7; i++) {
//            Object[] randomWeatherReport = generateRandomWeatherReport(weatherConditions, images, minTemp, maxTemp, minWindSpeed, maxWindSpeed);
//            String weather = String.valueOf(randomWeatherReport[0]);
//            int temp = (int) randomWeatherReport[1];
//            int windSpeed = (int) randomWeatherReport[2];
//            imageViews[i].setImage((Image) randomWeatherReport[3]);
//
//            System.out.println(i +"- Today's weather report:");
//            System.out.println("Weather: " + weather);
//            System.out.println("Temperature: " + temp + " degrees Celsius");
//            System.out.println("Wind speed: " + windSpeed + " km/h");
//            System.out.println("//////////////////////////////");
//        }
//    }
//
//    public static Object[] generateRandomWeatherReport(String[] weatherConditions, List<Image> images, int minTemp, int maxTemp, int minWindSpeed, int maxWindSpeed) {
//        Random random = new Random();
//        String weather = weatherConditions[random.nextInt(weatherConditions.length)];
//        int temp = 0;
//        int windSpeed = 0;
//        Image image = null;
//
//        // Set realistic temperature and wind speed based on weather condition
//        switch (weather) {
//            case "sunny":
//                temp = random.nextInt((maxTemp - 20) + 1) + 20; // high temperature
//                windSpeed = random.nextInt((maxWindSpeed - 5) + 1) + 5; // low wind speed
//                image = images.get(0);
//                break;
//            case "cloudy":
//                temp = random.nextInt((maxTemp - minTemp) + 1) + minTemp; // moderate temperature
//                windSpeed = random.nextInt((maxWindSpeed - minWindSpeed) + 1) + minWindSpeed; // moderate wind speed
//                image = images.get(1);
//                break;
//            case "rainy":
//                temp = random.nextInt((maxTemp - minTemp) + 1) + minTemp; // moderate temperature
//                windSpeed = random.nextInt((maxWindSpeed - 10) + 1) + 10; // high wind speed
//                image = images.get(2);
//                break;
//            case "stormy":
//                temp = random.nextInt((maxTemp - minTemp) + 1) + minTemp; // moderate temperature
//                windSpeed = random.nextInt((maxWindSpeed - 20) + 1) + 20; // very high wind speed
//                image = images.get(3);
//                break;
//            case "windy":
//                temp = random.nextInt((maxTemp - minTemp) + 1) + minTemp; // moderate temperature
//                windSpeed = random.nextInt((maxWindSpeed - 15) + 1) + 15; // high wind speed
//                image = images.get(4);
//                break;
//        }
//
//        Object[] weatherReport = new Object[4];
//        weatherReport[0] = weather;
//        weatherReport[1] = temp;
//        weatherReport[2] = windSpeed;
//        weatherReport[3] = image;
//
//        return weatherReport;
//    }

}
