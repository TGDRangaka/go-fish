package lk.ijse.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoatTM {
    private String id;
    private String registrationNo;
    private String model;
    private String type;
    private String sattelitePhoneNo;
    private Button action;
}
