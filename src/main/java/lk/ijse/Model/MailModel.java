package lk.ijse.Model;

import lk.ijse.DB.DBConnection;
import lk.ijse.dto.Mail;
import lk.ijse.util.CrudUtil;

import java.awt.image.DataBuffer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MailModel {
    public static List<Mail> getAllMails() throws SQLException {
        String sql = "SELECT * FROM mail";

        ResultSet rs = CrudUtil.execute(sql);
        List<Mail> mailList = new ArrayList<>();

        while (rs.next()){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(rs.getString(3), formatter);

            mailList.add(new Mail(
                    rs.getString(1),
                    rs.getString(2),
                    dateTime
            ));
        }

        return mailList;
    }

    public static String getLastId() throws SQLException {
        String sql = "SELECT * FROM mail ORDER BY mailId desc LIMIT 1";

        ResultSet rs = CrudUtil.execute(sql);
        if(rs.next()){
            return rs.getString(1);
        }
        return "M000";
    }

    public static boolean save(Mail mail, List<String> idList) throws SQLException {
        Connection con = null;
        try{
            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            boolean isMailSaved = save(mail);
            if(isMailSaved){
                boolean isMailDetailSaved = MailDetailModel.save(mail.getId(), idList);
                if(isMailDetailSaved){
                    con.commit();
                    return true;
                }
            }

            return false;
        }catch (SQLException e){
            System.out.println(e);
            con.rollback();
            return false;
        }finally {
            con.setAutoCommit(true);
        }
    }

    private static boolean save(Mail mail) throws SQLException {
        String sql = "INSERT INTO mail(mailId,description,sentDate) VALUES (?,?,?)";

        return CrudUtil.execute(sql, mail.getId(), mail.getDescription(), mail.getDateTime());
    }

    public static boolean deleteMail(String mailId) throws SQLException {
        Connection con = null;
        try {
            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            boolean isMailDetailsDeleted = MailDetailModel.delete(mailId);
            if(isMailDetailsDeleted){
                boolean isMailDeleted = MailModel.delete(mailId);
                if(isMailDeleted){
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

    private static boolean delete(String mailId) throws SQLException {
        String sql = "DELETE FROM mail WHERE mailId =?";

        return CrudUtil.execute(sql, mailId);
    }

    public static boolean isWeatherSendToday(LocalDate now) throws SQLException {
        String sql = "SELECT * FROM mail WHERE DATE(sentDate) = ? && description LIKE 'Weather Forecast%'";

        ResultSet rs = CrudUtil.execute(sql, now);
        if(rs.next()){
            return true;
        }
        return false;
    }

    public static boolean isSentToAll(String mailId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM maildetail WHERE mailId = ?";

        ResultSet rs = CrudUtil.execute(sql, mailId);
        int count = 0;
        if(rs.next()){
            count = rs.getInt(1);
        }

        return count == CrewModel.getCrewIds().size();
    }

    public static String getMailBody(String mailId) throws SQLException {
        String sql = "SELECT * FROM mail WHERE mailId = ?";

        ResultSet rs = CrudUtil.execute(sql, mailId);
        if(rs.next()){
            String[] split = rs.getString("description").split("\\$");
            if(split.length == 1){
                return rs.getString("description");
            }else{
                return split[1];
            }
        }
        return null;
    }
}
