package lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoatOwner {
    private String ownerId;
    private String name;
    private String nic;
    private String address;
    private String contactNo;
}
