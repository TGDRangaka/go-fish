package lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CatchDetail {
    String catchId;
    String fishId;
    Double weight;
}
