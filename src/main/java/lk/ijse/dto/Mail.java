package lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class Mail {
    private String id;
    private String description;
    private LocalDateTime dateTime;
}
