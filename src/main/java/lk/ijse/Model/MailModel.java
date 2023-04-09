package lk.ijse.Model;

import lk.ijse.dto.Mail;
import lk.ijse.util.CrudUtil;

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
}
