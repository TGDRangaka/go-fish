package lk.ijse.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoatOwnerTM {
    private String id;
    private String boatId;
    private String name;
    private String nic;
    private String address;
    private String contactNo;
    private Button action;
}
