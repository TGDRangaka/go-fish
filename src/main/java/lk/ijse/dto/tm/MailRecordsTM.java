package lk.ijse.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MailRecordsTM {
    private String mailId;
    private String description;
    private String to;
    private LocalDateTime dateTime;
    private Button action;
}
