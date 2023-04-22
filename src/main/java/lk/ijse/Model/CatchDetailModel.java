package lk.ijse.Model;

import lk.ijse.dto.CatchDetail;
import lk.ijse.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    public static boolean save(CatchDetail catchDetail) throws SQLException {
        String sql = "INSERT INTO catchdetail(catchId, fishId, weight, total) VALUES (?,?,?,?)";

        return CrudUtil.execute(sql,
                catchDetail.getCatchId(),
                catchDetail.getFishId(),
                catchDetail.getWeight(),
                catchDetail.getTotal()
        );
    }

    public static List<CatchDetail> getCatchDetails(String id) throws SQLException {
        String sql = "SELECT * FROM catchdetail WHERE catchId = ?";

        ResultSet rs = CrudUtil.execute(sql, id);
        List<CatchDetail> catchDetails = new ArrayList<>();

        while(rs.next()){
            catchDetails.add(new CatchDetail(rs.getString(1),rs.getString(2),rs.getDouble(3), rs.getDouble(4)));
        }

        return catchDetails;
    }

    public static boolean delete(List<String> catchIdList) throws SQLException {
        for(String catchId : catchIdList){
            boolean isDeleted = delete(catchId);
            if(!isDeleted){
                return false;
            }
        }
        return true;
    }

    public static boolean delete(String catchId) throws SQLException {
        String sql = "DELETE FROM catchdetail WHERE catchId = ?";

        return CrudUtil.execute(sql, catchId);
    }

    public static boolean isFishHaveRecords(String fishId) throws SQLException {
        String sql = "SELECT * FROM catchdetail WHERE fishId = ?";

        ResultSet rs = CrudUtil.execute(sql, fishId);
        if(rs.next()) {
            return true;
        }
        return false;
    }

    public static boolean deleteFishRecords(String fishId) throws SQLException {
        String sql = "DELETE FROM catchdetail WHERE fishId = ?";

        return CrudUtil.execute(sql, fishId);
    }
}
