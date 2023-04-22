package lk.ijse.dto.tm;

import javafx.scene.control.CheckBox;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SelectCrewTM {
    private CheckBox cbox;
    private String crewId;
    private String leader;
    private Integer crewmenCount;
    private Integer boatsCount;
}
