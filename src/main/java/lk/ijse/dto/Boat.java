package lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Boat {
    private String boatId;
    private String registrationNo;
    private String model;
    private String type;
    private String sattelitePhoneNo;
    private String ownerId;
    private String crewId;
}
