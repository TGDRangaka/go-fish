package lk.ijse.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FishTM {
    private String fishId;
    private String fishType;
    private Double unitWeight;
    private Double unitPrice;
    private Button action;
}
