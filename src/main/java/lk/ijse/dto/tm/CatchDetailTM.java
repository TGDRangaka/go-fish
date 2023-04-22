package lk.ijse.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CatchDetailTM {
    String fishId;
    String fishType;
    Double unitPrice;
    Double caughtWeight;
    Double total;
    Button action;
}
