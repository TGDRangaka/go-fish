package lk.ijse.Model;

import lk.ijse.DB.DBConnection;
import lk.ijse.dto.Boat;
import lk.ijse.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class BoatModel {
    public static String getLastId() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM boat ORDER BY boatId DESC LIMIT 1";

        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        if(resultSet.next()){
            return resultSet.getString(1);
        }
        return "B001";
    }

    public static boolean save(List<Boat> boatsList) throws SQLException {
        for(Boat boat : boatsList){
            boolean isSaved = save(boat);
            if (!isSaved) {
                return false;
            }
        }
        return true;
    }

    public static boolean save(Boat boat) throws SQLException {
        String sql = "INSERT INTO boat(boatId,registrationNo,model,type,sattelitePhoneNo,ownerId) " +
                "VALUES (?,?,?,?,?,?)";

        return CrudUtil.execute(sql,
                boat.getBoatId(),
                boat.getRegistrationNo(),
                boat.getModel(),
                boat.getType(),
                boat.getSattelitePhoneNo(),
                boat.getOwnerId()
        );
    }
}
