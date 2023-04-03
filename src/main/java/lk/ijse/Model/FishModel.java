package lk.ijse.Model;

import lk.ijse.dto.Fish;
import lk.ijse.util.CrudUtil;

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

        return null;
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
}
