package lk.ijse.Model;

import lk.ijse.DB.DBConnection;
import lk.ijse.dto.BoatOwner;
import lk.ijse.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class BoatOwnerModel {
    public static String getLastId() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM boatowner ORDER BY ownerId DESC LIMIT 1";

        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        if(resultSet.next()){
            return resultSet.getString(1);
        }
        return "BO001";
    }

    public static boolean save(List<BoatOwner> boatOwnersList) throws SQLException {
        for(BoatOwner boatOwner : boatOwnersList){
            boolean isSaved = save(boatOwner);
            if(!isSaved){
                return false;
            }
        }

        return true;
    }

    public static boolean save(BoatOwner boatOwner) throws SQLException {
        String sql = "INSERT INTO boatowner(ownerId,name,nic,address,contactNo) " +
                "VALUES (?,?,?,?,?)";

        return CrudUtil.execute(sql,
                boatOwner.getOwnerId(),
                boatOwner.getName(),
                boatOwner.getNic(),
                boatOwner.getAddress(),
                boatOwner.getContactNo()
        );
    }
}
