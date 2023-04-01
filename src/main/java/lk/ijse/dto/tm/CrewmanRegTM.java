package lk.ijse.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CrewmanRegTM {
    private String id;
    private String name;
    private String nic;
    private String email;
    private String contactNo;
}
