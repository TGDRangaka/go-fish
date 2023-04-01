package lk.ijse.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CrewmanTM {
    private Integer no;
    private String name;
    private String nic;
    private String address;
    private LocalDate bod;
    private String email;
    private String contactNo;
    private Button action;
}
