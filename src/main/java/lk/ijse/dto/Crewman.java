package lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
public class Crewman {
    private String crewmanId;
    private String name;
    private String nic;
    private String address;
    private LocalDate bod;
    private String email;
    private String contactNo;
    private String crewId;
}
