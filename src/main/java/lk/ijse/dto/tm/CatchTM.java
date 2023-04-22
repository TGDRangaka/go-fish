package lk.ijse.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class CatchTM {
    private String id;
    private String crewId;
    private Double catchWeight;
    private LocalDate catchDate;
    private Double paymentAmount;
    private LocalTime paymentTime;
    private LocalTime tripStartedTime;
    private LocalTime tripEndedTime;
    private Button action;
}
