package lk.ijse.Model;

import lk.ijse.DB.DBConnection;
import lk.ijse.dto.Crewman;
import lk.ijse.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
}
