package lk.ijse.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoatOwnerTM {
    Integer no;
    Integer boatNo;
    String name;
    String nic;
    String address;
    String contactNo;
    Button action;
}
