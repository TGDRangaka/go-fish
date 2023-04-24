package lk.ijse;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Launcher extends Application {
    public static Stage stage;
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/view_analystic_form.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/main_window_form.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        stage = primaryStage;
        primaryStage.show();
    }
}
