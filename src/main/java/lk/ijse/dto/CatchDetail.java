package lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CatchDetail {
    private String catchId;
    private String fishId;
    private Double weight;
    private Double total;
}
