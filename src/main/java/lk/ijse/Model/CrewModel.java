package lk.ijse.Model;

import lk.ijse.DB.DBConnection;
import lk.ijse.dto.Boat;
import lk.ijse.dto.BoatOwner;
import lk.ijse.dto.Crew;
import lk.ijse.dto.Crewman;
import lk.ijse.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CrewModel {
    public static String getLastId() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM crew ORDER BY crewId DESC LIMIT 1";

        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        if(resultSet.next()){
            return resultSet.getString(1);
        }
        return "C001";
    }

    public static boolean registerCrew(Crew crew, List<Crewman> crewmenList, List<Boat> boatsList, List<BoatOwner> boatOwnersList) throws SQLException {
        Connection con = null;
        try {
            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            boolean isCrewSaved = save(crew);
            System.out.println("isCrewSaved : " + isCrewSaved);
            if(isCrewSaved){
                boolean isCrewmenSaved = CrewmanModel.save(crewmenList);
                System.out.println("isCrewmenSaved : " + isCrewmenSaved);
                if(isCrewmenSaved){
                    boolean isOwnersSaved = BoatOwnerModel.save(boatOwnersList);
                    System.out.println("isOwnersSaved : " + isOwnersSaved);
                    if(isOwnersSaved){
                        boolean isBoatsSaved = BoatModel.save(boatsList);
                        System.out.println("isBoatsSaved : " + isBoatsSaved);
                        if(isBoatsSaved){
                            con.commit();
                            return true;
                        }
                    }
                }
            }

            return false;
        } catch (SQLException e){
            con.rollback();
            return false;
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static boolean save(Crew crew) throws SQLException {
        String sql = "INSERT INTO crew(crewId, leader, crewmencount, boatscount, availabletime, availabledays, adminId) " +
                "VALUES(?,?,?,?,?,?,?)";

        return CrudUtil.execute(sql,
                crew.getCrewId(),
                crew.getLeader(),
                crew.getCrewmenCount(),
                crew.getBoatsCount(),
                crew.getAvailableTimes(),
                crew.getAvailableDays(),
                crew.getAdminId()
        );
    }
}
