package lk.ijse.Model;

import lk.ijse.DB.DBConnection;
import lk.ijse.dto.Fish;
import lk.ijse.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FishModel {
    public static List<Fish> getAllFish() throws SQLException {
        String sql = "SELECT * FROM fish";

        ResultSet rs = CrudUtil.execute(sql);
        List<Fish> fishList = new ArrayList<>();

        while(rs.next()){
            fishList.add(new Fish(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getDouble(3),
                    rs.getDouble(4)
            ));
        }

        return fishList;
    }

    public static String getLastId() throws SQLException {
        String sql = "SELECT * FROM fish ORDER BY fishId desc LIMIT 1";

        ResultSet rs = CrudUtil.execute(sql);

        if(rs.next()){
            return rs.getString(1);
        }

        return "F000";
    }

    public static Fish getFish(String selectedFish) throws SQLException {
        String sql = "SELECT * FROM fish WHERE fishType = ?";

        ResultSet rs = CrudUtil.execute(sql, selectedFish);
        if(rs.next()){
            return new Fish(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getDouble(3),
                    rs.getDouble(4)
            );
        }
        return null;
    }

    public static boolean save(Fish fish) throws SQLException {
        String sql = "INSERT INTO fish(fishId, fishType, unitWeight, unitPrice) " +
                "VALUES(?,?,?,?)";

        return CrudUtil.execute(sql,
                fish.getFishId(),
                fish.getFishType(),
                fish.getUnitWeight(),
                fish.getUnitPrice()
        );
    }

    public static boolean update(Fish fish) throws SQLException {
        String sql = "UPDATE fish SET fishType = ?, unitWeight = ?, unitPrice = ? WHERE fishId = ?";

        return CrudUtil.execute(sql,
                fish.getFishType(),
                fish.getUnitWeight(),
                fish.getUnitPrice(),
                fish.getFishId()
        );
    }

    public static boolean delete(String fishId) throws SQLException {
        Connection con = null;
        try{
            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            boolean isFishHaveRecords = CatchDetailModel.isFishHaveRecords(fishId);
            boolean isFishRecordsDeleted = false;
            if(isFishHaveRecords){
                isFishRecordsDeleted = CatchDetailModel.deleteFishRecords(fishId);
            }

            if(!isFishHaveRecords || (isFishHaveRecords && isFishRecordsDeleted)){
                boolean isFishDeleted = deleteFish(fishId);
                if(isFishDeleted){
                    con.commit();
                    return true;
                }
            }

            return false;
        }catch (SQLException e){
            con.rollback();
            return false;
        }finally {
            con.setAutoCommit(true);
        }


    }

    private static boolean deleteFish(String fishId) throws SQLException {
        String sql = "DELETE FROM fish WHERE fishId = ?";

        return CrudUtil.execute(sql, fishId);
    }
}
