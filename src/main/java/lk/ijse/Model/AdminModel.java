package lk.ijse.Model;

import lk.ijse.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminModel {
    public static String adminId;

    public static boolean userVerify(String username, String password) throws SQLException {
        String sql = "SELECT * FROM admin WHERE userName = ? AND password = ?";

        ResultSet rs = (ResultSet) CrudUtil.execute(sql, username, password);

        if(rs.next()){
            adminId = rs.getString(1);
            return true;
        }

        return false;
    }
}
