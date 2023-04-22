package lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class Catch {
    private String id;
    private Double totalWeight;
    private LocalDate catchDate;
    private Double paymentAmount;
    private LocalTime paymentTime;
    private LocalTime tripStartedTime;
    private LocalTime tripEndedTime;
    private String crewId;
    private String adminId;
}
