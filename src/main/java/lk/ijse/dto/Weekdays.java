package lk.ijse.dto;

public enum Weekdays {
    MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY;

    public static String getAvailableDaysBinary(boolean... days) {
        String s = "";
        for(Boolean day : days){
            s += day ? "1" : "0";
        }
        return s;
    }
}
