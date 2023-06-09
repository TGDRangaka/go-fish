package lk.ijse.Model;

import lk.ijse.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminModel {
    public static String adminId;

    public static boolean userVerify(String username, String password) throws SQLException {
        String sql = "SELECT * FROM admin WHERE userName = ? AND password = ?";

        ResultSet rs = CrudUtil.execute(sql, username, password);

        if(rs.next()){
            adminId = rs.getString(1);
            return true;
        }

        return false;
    }

    public static String getName(String username, String password) throws SQLException {
        String sql = "SELECT * FROM admin WHERE userName = ? AND password = ?";

        ResultSet rs = CrudUtil.execute(sql, username, password);
        rs.next();
        return rs.getString(2);
    }
}
