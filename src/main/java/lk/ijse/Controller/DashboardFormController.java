package lk.ijse.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardFormController implements Initializable {
    @FXML
    private AnchorPane root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root.setVisible(false);
    }

    @FXML
    void btnDashboardOnAction(ActionEvent event) {
        root.setVisible(false);
    }

    @FXML
    void btnCrewManageOnAction(ActionEvent event) throws IOException {
        root.setVisible(true);

       Node node = FXMLLoader.load(getClass().getResource("/view/crew_registration_form.fxml"));
       root.getChildren().setAll(node);
    }


}
