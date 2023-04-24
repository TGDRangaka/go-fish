package lk.ijse.Model;

import lk.ijse.DB.DBConnection;
import lk.ijse.dto.*;
import lk.ijse.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
            System.out.println("in CrewModel.registerCrew() try{}1");
            con = DBConnection.getInstance().getConnection();
            System.out.println("in CrewModel.registerCrew() try{}2");
            con.setAutoCommit(false);

            boolean isCrewSaved = save(crew);
            System.out.println("in CrewModel.registerCrew() try{}3");
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
            System.out.println("error");
            System.out.println(e);
            con.rollback();
            return false;
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static boolean save(Crew crew) throws SQLException {
        System.out.println("in crew model save method");

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

    public static List<Crew> getAllCrew() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM crew";

        ResultSet rs = con.createStatement().executeQuery(sql);
        List<Crew> crewList = new ArrayList<>();

        while (rs.next()){
            crewList.add(new Crew(
               rs.getString(1),
               rs.getString(2),
               rs.getInt(3),
               rs.getInt(4),
               rs.getString(5),
               rs.getString(6),
               rs.getString(7)
            ));
        }

        return crewList;
    }

    public static boolean delete(String crewId) throws SQLException {
        String sql = "DELETE FROM crew WHERE crewId = ?";

        return CrudUtil.execute(sql, crewId);
    }

    public static boolean deleteCrew(String crewId, List<String> boatownersIdList, List<String> catchIdList) throws SQLException {
        Connection con = null;
        try{
            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            boolean isBoatsDeleted = BoatModel.delete(crewId);
            System.out.println("isBoatDeleted" + isBoatsDeleted);
            if(isBoatsDeleted){

                boolean isHaveCatches = CatchModel.isHaveData(crewId);
                System.out.println("isHaveCatches" + isHaveCatches);
                boolean isHaveMails = MailDetailModel.isHaveData(crewId);
                System.out.println("isHaveMails" + isHaveMails);
                boolean isCatchDetailsDeleted = false;
                boolean isMailDetailsDeleted = false;
                boolean isCatchsDeleted = false;
                boolean isCatchsOk = false;
                boolean isMailsOk = false;
                if(isHaveCatches) {
                    isCatchDetailsDeleted = CatchDetailModel.delete(catchIdList);
                    System.out.println("isCatchDetailsDeleted" + isCatchDetailsDeleted);
                    isCatchsDeleted = CatchModel.delete(crewId);
                    System.out.println("isCatchsDeleted" + isCatchsDeleted);
                    if(isCatchDetailsDeleted && isCatchsDeleted){
                        isCatchsOk = true;
                    }
                }
                if(isHaveMails){
                    isMailDetailsDeleted = MailDetailModel.delete(crewId);
                    System.out.println("isMailDetailsDeleted" + isMailDetailsDeleted);
                    if(isMailDetailsDeleted){
                        isMailsOk = true;
                    }
                }

                if((!isHaveCatches || !isHaveMails) || isCatchsOk || isMailsOk) {

                    boolean isBoatOwnerDeleted = BoatOwnerModel.delete(boatownersIdList);
                    System.out.println("isBoatOwnerDeleted" + isBoatOwnerDeleted);
                    if (isBoatOwnerDeleted) {
                        boolean isCrewmenDeleted = CrewmanModel.delete(crewId);
                        System.out.println("isCrewmenDeleted" + isCrewmenDeleted);
                        if (isCrewmenDeleted) {
                            boolean isCrewDeleted = delete(crewId);
                            System.out.println("isCrewDeleted" + isCrewDeleted);
                            if (isCrewDeleted) {
                                con.commit();
                                return true;
                            }
                        }
                    }
                }
            }

            con.rollback();
            return false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            con.rollback();
            return false;
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static Crew getCrew(String crewId) throws SQLException {
        String sql = "SELECT * FROM crew WHERE crewId = ?";

        ResultSet rs = CrudUtil.execute(sql, crewId);

        if(rs.next()){
            return new Crew(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getInt(3),
                    rs.getInt(4),
                    rs.getString(5),
                    rs.getString(6),
                    rs.getString(7)
            );
        }

        return null;
    }

    public static boolean updateCrew(Crew crew, List<Crewman> crewmenList, List<Boat> boatsList, List<BoatOwner> boatOwnersList, List<String> ownersIdList) throws SQLException {
        Connection con = null;
        try{
            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            System.out.println(crew.getCrewId());
            boolean isBoatsDeleted = BoatModel.delete(crew.getCrewId());
            boolean isBoatOwnersDeleted = BoatOwnerModel.delete(ownersIdList);
            boolean isCrewmenDeleted = CrewmanModel.delete(crew.getCrewId());

            System.out.println(isBoatsDeleted + " " + isBoatOwnersDeleted + " " + isCrewmenDeleted);

            if(isBoatsDeleted && isBoatOwnersDeleted && isCrewmenDeleted) {
                boolean isBoatOwnersUpdated = BoatOwnerModel.save(boatOwnersList);
                System.out.println("isBoatOwnerUpdated : " + isBoatOwnersUpdated);
                if (isBoatOwnersUpdated){
                    boolean isBoatsUpdated = BoatModel.save(boatsList);
                    System.out.println("isBoatsUpdated : " + isBoatsUpdated);
                    if(isBoatsUpdated) {
                        boolean isCrewmenUpdated = CrewmanModel.save(crewmenList);
                        System.out.println("isCrewmenUpdated : " + isCrewmenUpdated);
                        if(isCrewmenUpdated){
                            boolean isCrewUpdated = update(crew);
                            System.out.println("isCrewUpdated : " + isCrewUpdated);
                            if (isCrewUpdated){
                                con.commit();
                                return true;
                            }
                        }
                    }
                }
            }

            con.rollback();
            return false;
        } catch (SQLException e){
            e.printStackTrace();
            con.rollback();
            return false;
        } finally {
            con.setAutoCommit(true);
        }
    }

    private static boolean update(Crew crew) throws SQLException {
        String sql = "UPDATE crew SET leader = ?, crewmencount = ?, boatscount = ?, availabletime = ?, availabledays = ? WHERE crewId = ?";

        return CrudUtil.execute(sql,
                crew.getLeader(),
                crew.getCrewmenCount(),
                crew.getBoatsCount(),
                crew.getAvailableTimes(),
                crew.getAvailableDays(),
                crew.getCrewId()
        );
    }

    public static List<String> getCrewIds() throws SQLException {
        String sql = "SELECT * FROM crew";

        ResultSet rs = CrudUtil.execute(sql);
        List<String> crewIds = new ArrayList<>();
        while (rs.next()){
            crewIds.add(rs.getString(1));
        }

        return crewIds;
    }

}
