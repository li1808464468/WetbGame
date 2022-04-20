package h5adapter.utils;

import java.util.Calendar;

public class Dates {

    public static long TIME_HOUR = 60 * 60 * 1000;
    public static long TIME_DAY = 24 * TIME_HOUR;

    public static boolean isSameDay(long time1, long time2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTimeInMillis(time1);
        cal2.setTimeInMillis(time2);
        boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        return sameDay;
    }

    public static long getNextDayTime(int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.HOUR_OF_DAY) > hour
                || (cal.get(Calendar.HOUR_OF_DAY) == hour && cal.get(Calendar.MINUTE) > minute)) {
            cal.add(Calendar.DATE, 1);
        }
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        return cal.getTimeInMillis();
    }
}
