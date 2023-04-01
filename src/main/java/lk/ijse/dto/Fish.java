package lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Fish {
    private String fishId;
    private String fishType;
    private Double unitWeight;
    private Double unitPrice;
}
