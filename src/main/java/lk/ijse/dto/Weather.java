package lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Weather {
    private String temp;
    private String condition;
    private String windSpeed;
    private String iconURL;
}
