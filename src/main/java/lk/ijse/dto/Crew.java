package lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Crew {
    private String crewId;
    private String leader;
    private Integer crewmenCount;
    private Integer boatsCount;
    private String availableTimes;
    private String availableDays;
    private String adminId;
}
