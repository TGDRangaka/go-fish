package lk.ijse.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FishPricesTM {
    private String fishType;
    private Double unitWeight;
    private Double unitPrice;
}
