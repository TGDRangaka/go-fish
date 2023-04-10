package lk.ijse.Model;

import lk.ijse.DB.DBConnection;
import lk.ijse.dto.Catch;
import lk.ijse.dto.CatchDetail;
import lk.ijse.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CatchModel {
    public static boolean saveCatch(Catch c, List<CatchDetail> catchDetails) throws SQLException {
        Connection con = null;
        try{
            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            boolean isCatchSaved = save(c);
            if(isCatchSaved){
                boolean isCatchDetailsSaved = CatchDetailModel.save(catchDetails);
                if(isCatchDetailsSaved){
                    con.commit();
                    return true;
                }
            }


            return false;
        } catch (SQLException e){
            System.out.println(e);
            return false;
        }finally {
            con.setAutoCommit(true);
        }
    }

    private static boolean save(Catch c) throws SQLException {
        String sql = "INSERT INTO catch(catchId,weightOfCatch,catchDate,paymentAmount,paymentTime,tripStartedTime,tripEndedTime,crewId,adminId)" +
                "VALUES (?,?,?,?,?,?,?,?,?)";

        return CrudUtil.execute(sql,
                c.getId(),
                c.getTotalWeight(),
                c.getCatchDate(),
                c.getPaymentAmount(),
                c.getPaymentTime(),
                c.getTripStartedTime(),
                c.getTripEndedTime(),
                c.getCrewId(),
                c.getAdminId()
        );
    }

    public static String getLastId() throws SQLException {
        String sql = "SELECT * FROM catch ORDER BY catchId DESC LIMIT 1";

        ResultSet rs = CrudUtil.execute(sql);
        if(rs.next()){
            return rs.getString(1);
        }
        return "CA001";
    }

    public static List<Catch> getAllCatches() throws SQLException {
        String sql = "SELECT * FROM catch";

        ResultSet rs = CrudUtil.execute(sql);
        List<Catch> catchList = new ArrayList<>();

        while (rs.next()){
            catchList.add(new Catch(
                    rs.getString(1),
                    rs.getDouble(2),
                    LocalDate.parse(rs.getString(3)),
                    rs.getDouble(4),
                    LocalTime.parse(rs.getString(5)),
                    LocalTime.parse(rs.getString(6)),
                    LocalTime.parse(rs.getString(7)),
                    rs.getString(8),
                    rs.getString(9)
            ));
        }
        return catchList;
    }

    public static List<String> getAllCatchIds(String crewId) throws SQLException {
        String sql = "SELECT (catchId) FROM catch WHERE crewId = ?";

        ResultSet rs = CrudUtil.execute(sql, crewId);
        List<String> ids = new ArrayList<>();

        while (rs.next()){
            ids.add(rs.getString(1));
        }
        return ids;
    }

    public static boolean delete(String crewId) throws SQLException {
        String sql = "DELETE FROM catch WHERE crewId = ?";

        return CrudUtil.execute(sql, crewId);
    }

    public static boolean isHaveData(String crewId) throws SQLException {
        String sql = "SELECT * FROM catch WHERE crewId = ?";

        ResultSet rs = CrudUtil.execute(sql, crewId);
        if(rs.next()){
            return true;
        }
        return false;
    }
}