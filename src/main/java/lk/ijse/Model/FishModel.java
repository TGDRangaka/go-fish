package lk.ijse.Model;

import javafx.scene.chart.XYChart;
import lk.ijse.DB.DBConnection;
import lk.ijse.dto.Fish;
import lk.ijse.dto.tm.AnalysisTM;
import lk.ijse.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FishModel {
    public static List<Fish> getAllFish() throws SQLException {
        String sql = "SELECT * FROM fish";

        ResultSet rs = CrudUtil.execute(sql);
        List<Fish> fishList = new ArrayList<>();

        while(rs.next()){
            fishList.add(new Fish(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getDouble(3),
                    rs.getDouble(4)
            ));
        }

        return fishList;
    }

    public static String getLastId() throws SQLException {
        String sql = "SELECT * FROM fish ORDER BY fishId desc LIMIT 1";

        ResultSet rs = CrudUtil.execute(sql);

        if(rs.next()){
            return rs.getString(1);
        }

        return "F000";
    }

    public static Fish getFish(String selectedFish) throws SQLException {
        String sql = "SELECT * FROM fish WHERE fishType = ?";

        ResultSet rs = CrudUtil.execute(sql, selectedFish);
        if(rs.next()){
            return new Fish(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getDouble(3),
                    rs.getDouble(4)
            );
        }
        return null;
    }

    public static boolean save(Fish fish) throws SQLException {
        String sql = "INSERT INTO fish(fishId, fishType, unitWeight, unitPrice) " +
                "VALUES(?,?,?,?)";

        return CrudUtil.execute(sql,
                fish.getFishId(),
                fish.getFishType(),
                fish.getUnitWeight(),
                fish.getUnitPrice()
        );
    }

    public static boolean update(Fish fish) throws SQLException {
        String sql = "UPDATE fish SET fishType = ?, unitWeight = ?, unitPrice = ? WHERE fishId = ?";

        return CrudUtil.execute(sql,
                fish.getFishType(),
                fish.getUnitWeight(),
                fish.getUnitPrice(),
                fish.getFishId()
        );
    }

    public static boolean delete(String fishId) throws SQLException {
        Connection con = null;
        try{
            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            boolean isFishHaveRecords = CatchDetailModel.isFishHaveRecords(fishId);
            boolean isFishRecordsDeleted = false;
            if(isFishHaveRecords){
                isFishRecordsDeleted = CatchDetailModel.deleteFishRecords(fishId);
            }

            if(!isFishHaveRecords || (isFishHaveRecords && isFishRecordsDeleted)){
                boolean isFishDeleted = deleteFish(fishId);
                if(isFishDeleted){
                    con.commit();
                    return true;
                }
            }

            return false;
        }catch (SQLException e){
            con.rollback();
            return false;
        }finally {
            con.setAutoCommit(true);
        }


    }

    private static boolean deleteFish(String fishId) throws SQLException {
        String sql = "DELETE FROM fish WHERE fishId = ?";

        return CrudUtil.execute(sql, fishId);
    }

    public static String getMostCaughtFish(String crewId) throws SQLException {
        String sql = "SELECT fish.fishType, SUM(catchdetail.weight)AS sum FROM fish " +
                "INNER JOIN catchdetail " +
                "ON fish.fishId = catchdetail.fishId " +
                "INNER JOIN catch " +
                "ON catchdetail.catchId = catch.catchId " +
                "WHERE catch.crewId = ? " +
                "GROUP BY fish.fishType " +
                "ORDER BY sum desc " +
                "LIMIT 1";

        ResultSet rs = CrudUtil.execute(sql, crewId);
        if (rs.next()){
            return rs.getString(1);
        }

        return "N/A";
    }

    public static List<AnalysisTM> getFishCatch() throws SQLException {
        String sql = "SELECT fish.fishId, fish.fishType, SUM(catchdetail.weight)AS sum " +
                "FROM fish " +
                "INNER JOIN catchdetail " +
                "ON fish.fishId = catchdetail.fishId " +
                "GROUP BY fish.fishId " +
                "ORDER BY fish.fishId";

        ResultSet rs = CrudUtil.execute(sql);
        List<AnalysisTM> list = new ArrayList<>();
        while(rs.next()){
            list.add(new AnalysisTM(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getDouble(3)
            ));
        }

        return list;
    }

    public static AnalysisTM getAllFishCatch() throws SQLException {
        String sql = "SELECT SUM(catchdetail.weight)AS sum " +
                "FROM fish " +
                "INNER JOIN catchdetail " +
                "ON fish.fishId = catchdetail.fishId";

        ResultSet rs = CrudUtil.execute(sql);
        if(rs.next()){
            return new AnalysisTM(
                    "F000",
                    "All Fish",
                    rs.getDouble(1)
            );
        }

        return null;
    }

    public static XYChart.Series<String, Double> getCatchWeightSeries() throws SQLException {
        String sql = "SELECT fish.fishType, SUM(catchdetail.weight)AS sum " +
                "FROM fish " +
                "INNER JOIN catchdetail " +
                "ON fish.fishId = catchdetail.fishId " +
                "GROUP BY fish.fishId " +
                "ORDER BY fish.fishId";

        ResultSet rs = CrudUtil.execute(sql);
        XYChart.Series<String, Double> series = new XYChart.Series<>();

        while (rs.next()){
            series.getData().add(new XYChart.Data<>(rs.getString(1), rs.getDouble(2)));
        }

        return series;
    }

    public static XYChart.Series<String, Number> getCatchCountSeries() throws SQLException {
        String sql = "SELECT fish.fishType, COUNT(fish.fishId)AS count " +
                "FROM fish " +
                "INNER JOIN catchdetail " +
                "ON fish.fishId = catchdetail.fishId " +
                "GROUP BY fish.fishId";

        ResultSet rs = CrudUtil.execute(sql);
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        while (rs.next()){
            series.getData().add(new XYChart.Data<>(rs.getString(1), rs.getInt(2)));
        }

        return series;
    }

    public static Double getCatchWeight(String fishId, LocalDate fromDate, LocalDate toDate) throws SQLException {
        String sql = "SELECT SUM(catchdetail.weight)AS weight " +
                "FROM catchdetail " +
                "INNER JOIN fish " +
                "ON catchdetail.fishId = fish.fishId " +
                "INNER JOIN catch " +
                "ON catchdetail.catchId = catch.catchId " +
                "WHERE catch.catchDate > ? && catch.catchDate <= ? && fish.fishId = ?";

        ResultSet rs = CrudUtil.execute(sql, fromDate, toDate, fishId);
        if(rs.next()){
            return rs.getDouble(1);
        }
        return 0.0;
    }

    public static Integer getCatchCount(String fishId, LocalDate fromDate, LocalDate toDate) throws SQLException {
        String sql = "SELECT COUNT(catchdetail.fishId)AS count " +
                "FROM catchdetail " +
                "INNER JOIN fish " +
                "ON catchdetail.fishId = fish.fishId " +
                "INNER JOIN catch " +
                "ON catchdetail.catchId = catch.catchId " +
                "WHERE catch.catchDate > ? && catch.catchDate <= ? && fish.fishId = ?";

        ResultSet rs = CrudUtil.execute(sql, fromDate, toDate, fishId);
        if(rs.next()){
            return rs.getInt(1);
        }
        return 0;
    }

    public static Integer getCrewCount(String id) throws SQLException {
        String sql = "SELECT COUNT(*) AS total_count \n" +
                "FROM (\n" +
                "    SELECT crew.crewId, COUNT(*) AS count \n" +
                "    FROM crew \n" +
                "    INNER JOIN catch ON crew.crewId = catch.crewId \n" +
                "    INNER JOIN catchdetail ON catch.catchId = catchdetail.catchId \n" +
                "    INNER JOIN fish ON catchdetail.fishId = fish.fishId \n" +
                "    WHERE fish.fishId = ? \n" +
                "    GROUP BY crew.crewId\n" +
                ") AS subquery";

        ResultSet rs = CrudUtil.execute(sql, id);
        if(rs.next()){
            return rs.getInt(1);
        }
        return 0;
    }

    public static Double getPayments(String id) throws SQLException {
        String sql = "SELECT SUM(catchdetail.total)AS sum \n" +
                "FROM catchdetail \n" +
                "INNER JOIN fish \n" +
                "ON catchdetail.fishId = fish.fishId \n" +
                "WHERE fish.fishId = ?";

        ResultSet rs = CrudUtil.execute(sql, id);
        if(rs.next()){
            return rs.getDouble(1);
        }
        return 0.0;
    }

    public static String getMostCaughtWeekday(String id) throws SQLException {
        String sql = "SELECT COUNT(*)as count, DAYNAME(catch.catchDate)as day \n" +
                "FROM catch \n" +
                "INNER JOIN catchdetail\n" +
                "ON catch.catchId = catchdetail.catchId\n" +
                "WHERE catchdetail.fishId = ? \n" +
                "GROUP BY day \n" +
                "ORDER BY count desc\n" +
                "LIMIT 1";

        ResultSet rs = CrudUtil.execute(sql, id);
        if(rs.next()){
            return rs.getString(2);
        }
        return "N/A";
    }
}
