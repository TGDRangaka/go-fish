package lk.ijse.Model;

import lk.ijse.DB.DBConnection;
import lk.ijse.dto.Crewman;
import lk.ijse.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CrewmanModel {
    public static String getLastId() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM crewman ORDER BY crewmanId DESC LIMIT 1";

        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        if(resultSet.next()){
            return resultSet.getString(1);
        }
        return "CM001";
    }

    public static boolean save(Crewman crewman) throws SQLException {
        String sql = "INSERT INTO crewman(crewmanId,name,nic,address,bod,email,contactNo,crewId) " +
                "VALUES (?,?,?,?,?,?,?,?)";

        System.out.println(crewman.getBod());

        return CrudUtil.execute(sql,
                crewman.getCrewmanId(),
                crewman.getName(),
                crewman.getNic(),
                crewman.getAddress(),
                crewman.getBod(),
                crewman.getEmail(),
                crewman.getContactNo(),
                crewman.getCrewId()
        );
    }

    public static boolean save(List<Crewman> crewmenList) throws SQLException {
        for (Crewman crewman : crewmenList){
            boolean isSaved = save(crewman);
            if(!isSaved){
                return false;
            }
        }

        return true;
    }

    public static boolean delete(String crewId) throws SQLException {
        String sql = "DELETE FROM crewman WHERE crewId = ?";

        return CrudUtil.execute(sql, crewId);
    }

    public static List<Crewman> getCrewmen() throws SQLException {
        String sql = "SELECT * FROM crewman";

        List<Crewman> crewmenList = new ArrayList<>();
        ResultSet rs = CrudUtil.execute(sql);

        while(rs.next()){
            crewmenList.add(new Crewman(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    LocalDate.parse(rs.getString(5)),
                    rs.getString(6),
                    rs.getString(7),
                    rs.getString(8)
            ));
        }
        return crewmenList;
    }

    public static List<Crewman> getCrewmen(String crewId) throws SQLException {
        String sql = "SELECT * FROM crewman WHERE crewId = ?";

        List<Crewman> crewmenList = new ArrayList<>();
        ResultSet rs = CrudUtil.execute(sql, crewId);

        while(rs.next()){
            crewmenList.add(new Crewman(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    LocalDate.parse(rs.getString(5)),
                    rs.getString(6),
                    rs.getString(7),
                    rs.getString(8)
            ));
        }
        return crewmenList;
    }

    public static List<String> getEmails(List<String> idList) throws SQLException {
        String sql = "SELECT name,email FROM crewman WHERE crewId = ?";

        List<String> emailList = new ArrayList<>();
        for (String crewId : idList){
            ResultSet rs = CrudUtil.execute(sql, crewId);
            while (rs.next()){
                emailList.add(rs.getString(1));
                emailList.add(rs.getString(2));
            }
        }

        return emailList;
    }
}
