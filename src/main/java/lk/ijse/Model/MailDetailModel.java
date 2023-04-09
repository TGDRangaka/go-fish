package lk.ijse.Model;

import lk.ijse.util.CrudUtil;

import java.awt.image.RescaleOp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MailDetailModel {
    public static boolean delete(String crewId) throws SQLException {
        String sql = "DELETE FROM maildetail WHERE crewId = ?";

        return CrudUtil.execute(sql, crewId);
    }

    public static boolean isHaveData(String crewId) throws SQLException {
        String sql = "SELECT * FROM maildetail WHERE crewId = ?";

        ResultSet rs = CrudUtil.execute(sql, crewId);
        if(rs.next()){
            return true;
        }
        return false;
    }

    public static String getRecords(String mailId) throws SQLException {
        String sql = "SELECT * FROM maildetail WHERE mailId = ?";

        ResultSet rs = CrudUtil.execute(sql, mailId);
        StringBuilder to = new StringBuilder();

        while(rs.next()){
            to.append(rs.getString(1) + ",");
        }

        return String.valueOf(to.deleteCharAt(to.length() - 1));
    }
}
