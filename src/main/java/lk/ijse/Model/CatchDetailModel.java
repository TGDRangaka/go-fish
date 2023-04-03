package lk.ijse.Model;

import lk.ijse.dto.CatchDetail;
import lk.ijse.util.CrudUtil;

import java.sql.SQLException;
import java.util.List;

public class CatchDetailModel {
    public static boolean save(List<CatchDetail> catchDetails) throws SQLException {
        for(CatchDetail catchDetail : catchDetails){
            boolean isSaved = save(catchDetail);
            if(!isSaved){
                return false;
            }
        }
        return true;
    }

    private static boolean save(CatchDetail catchDetail) throws SQLException {
        String sql = "INSERT INTO catchdetail(catchId, fishId, weight) VALUES (?,?,?)";

        return CrudUtil.execute(sql,
                catchDetail.getCatchId(),
                catchDetail.getFishId(),
                catchDetail.getWeight()
        );
    }
}
