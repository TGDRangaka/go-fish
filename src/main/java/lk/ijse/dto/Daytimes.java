package lk.ijse.dto;

import java.util.ArrayList;
import java.util.List;

public enum Daytimes {
    MORNING,EVENING,NIGHT;

    public static String getAvailableTimesBinary(boolean... times) {
        String s = "";
        for(Boolean time : times){
            s += time ? "1" : "0";
        }
        return s;
    }


}
