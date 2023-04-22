package lk.ijse.util;

import lk.ijse.DB.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CrudUtil {

    public static <T>T execute(String sql, Object... args) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);

        for (int i = 0; i < args.length; i++) {
            pstm.setObject((i+1), args[i]);
        }

        if(sql.startsWith("SELECT") || sql.startsWith("select")) {
            return (T) pstm.executeQuery(); // ResultSet
        }
        return (T) (Boolean)(pstm.executeUpdate() > 0); //Boolean
    }

    public static String getNewId(String lastId) {
        String prefix = lastId.replaceAll("[0-9]", "");
        int number = Integer.parseInt(lastId.replaceAll("\\D", ""));
        number++;
        String newNumber = String.format("%0" + (lastId.length() - prefix.length()) + "d", number);
        return prefix + newNumber;
    }
}
