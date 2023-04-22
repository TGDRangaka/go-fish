package lk.ijse.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoatRegTM {
    private String id;
    private String registrationNo;
    private String model;
    private String ownerId;
    private String ownerName;
}
