package lk.ijse.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class CrewManageFormController {
    public Pane paneFilter;
    @FXML
    private AnchorPane root;

    public void btnRegisterCrewOnAction(ActionEvent actionEvent) throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("/view/crew_registration_form.fxml"));
        root.getChildren().setAll(node);
    }

    @FXML
    void btnFilterOnAction(ActionEvent event) {
        paneFilter.setVisible(!paneFilter.isVisible());
    }
}
