package lk.ijse.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CrewTM {
    String crewId;
    String leader;
    Integer crewmenCount;
    Integer boatsCount;
    String availableTimes;
    String availableDays;
    Button update;
    Button delete;
}
