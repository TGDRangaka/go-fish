package lk.ijse.Model;

import lk.ijse.util.CrudUtil;

import java.awt.image.RescaleOp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MailDetailModel {
    public static boolean delete(String id) throws SQLException {
        String sql = "";
        if(id.charAt(0) == 'C') {
            sql = "DELETE FROM maildetail WHERE crewId = ?";
        }else {
            sql = "DELETE FROM maildetail WHERE mailId = ?";
        }

        return CrudUtil.execute(sql, id);
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

    public static boolean save(String id, List<String> idList) throws SQLException {
        for(String crewId : idList){
            boolean isMailDetailSaved = save(id, crewId);
            if(!isMailDetailSaved){
                return false;
            }
        }
        return true;
    }

    public static boolean save(String mailId, String crewId) throws SQLException {
        String sql = "INSERT INTO maildetail(crewId, mailId) VALUES (?,?)";

        return CrudUtil.execute(sql, crewId, mailId);
    }
}
