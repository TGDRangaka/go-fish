package lk.ijse.Model;

import lk.ijse.DB.DBConnection;
import lk.ijse.dto.BoatOwner;
import lk.ijse.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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

    public static List<String> getBoatOwnersId(String crewId) throws SQLException {
        String sql = "SELECT boatowner.ownerId FROM boatowner " +
                "INNER JOIN boat " +
                "ON boatowner.ownerId = boat.ownerId " +
                "WHERE boat.crewId = ?";

        ResultSet rs =  CrudUtil.execute(sql, crewId);
        List<String> boatOwnerIdList = new ArrayList<>();

        while (rs.next()){
            boatOwnerIdList.add(rs.getString(1));
        }
        return boatOwnerIdList;
    }

    public static boolean delete(String ownerId) throws SQLException {
        String sql = "DELETE FROM boatowner WHERE ownerId = ?";
        System.out.println(ownerId + " in delete()");
        return CrudUtil.execute(sql, ownerId);
    }

    public static boolean delete(List<String> boatOwnersIdList) throws SQLException {
        for (String ownerId : boatOwnersIdList){
            boolean isDeleted = delete(ownerId);
            if(!isDeleted){
                return false;
            }
        }

        return true;
    }

    public static List<BoatOwner> getOwners() throws SQLException {
        String sql = "SELECT * FROM boatowner " +
                "INNER JOIN boat " +
                "ON boatowner.ownerId = boat.ownerId " +
                "INNER JOIN crew " +
                "ON boat.crewId = crew.crewId ";

        List<BoatOwner> boatOwners = new ArrayList<>();
        ResultSet rs = CrudUtil.execute(sql);

        while (rs.next()){
            boatOwners.add(new BoatOwner(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5)
            ));
        }
        return boatOwners;
    }

    public static List<BoatOwner> getOwners(String crewId) throws SQLException {
        String sql = "SELECT * FROM boatowner " +
                "INNER JOIN boat " +
                "ON boatowner.ownerId = boat.ownerId " +
                "INNER JOIN crew " +
                "ON boat.crewId = crew.crewId " +
                "WHERE crew.crewId = ?";

        List<BoatOwner> boatOwners = new ArrayList<>();
        ResultSet rs = CrudUtil.execute(sql, crewId);

        while (rs.next()){
            boatOwners.add(new BoatOwner(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5)
            ));
        }
        return boatOwners;
    }

}
