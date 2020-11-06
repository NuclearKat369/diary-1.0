package sample;

import org.w3c.dom.ls.LSOutput;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.jar.JarOutputStream;


public class StatCalendar {

    private static LocalDateTime cal;
    Dates dates = new Dates();
//    ArrayList<Dates> datesList = new ArrayList<>();

    public StatCalendar(){
        getCalendar();
    }

    public void getCalendar() {

        cal = LocalDateTime.now();
        dates.setYear(cal.getYear());
        System.out.println("Year: " + dates.getYear());
        for (int month = 1; month <= 12; month++) {

//            if (statCal.isLeapYear(statCal.YEAR) && month == 1) {     //проверяет год на високосность
//                days[month] = 29;
//            }

            dates.setMonth(cal.withMonth(month).getMonth().getDisplayName(TextStyle.FULL, Locale.UK));
            dates.setMonthNumber(month);
            System.out.println("Month # " + dates.getMonth());

            System.out.println("_____________________________" + "\n" +
                    " Mon Tue Wed Thu Fri Sat Sun");

            String[] calendarTable = new String[42];

            for (int n = 0; n < calendarTable.length; n++) {


                n += cal.withMonth(month).withDayOfMonth(1).getDayOfWeek().getValue() - 1;
//                System.out.println("dow CAL " + cal.withMonth(month).withDayOfMonth(1).getDayOfWeek().getValue());
                dates.setDayOfWeek(cal.withMonth(dates.getMonthNumber()).withDayOfMonth(1).getDayOfWeek().toString());
                System.out.println("DoW: " + dates.getDayOfWeek());

                for (int s = 0; s < n; s++) {
                    calendarTable[s] = " ";
                    System.out.printf("%4s", calendarTable[s]);
                }

                YearMonth yearMonth = YearMonth.of(dates.getYear(), dates.getMonthNumber());

                for (int i = 1; i <= yearMonth.lengthOfMonth(); i++, n++) {
                    dates.setDay(i);
                    dates.setDayOfWeek(cal.withYear(dates.getYear())
                            .withMonth(dates.getMonthNumber())
                            .withDayOfMonth(dates.getDay())
                            .getDayOfWeek()
                            .toString());
                    if ((n + 1) % 7 == 0) {
//                        System.out.printf("%4d,%10s", dates.getDay(), dates.getDayOfWeek());
                        System.out.printf("%4d", dates.getDay());
                        System.out.println();
                    } else
//                        System.out.printf("%4d,%10s", dates.getDay(), dates.getDayOfWeek());
                        System.out.printf("%4d", dates.getDay());

                }
                System.out.println();
                break;
            }
//            datesList.add(dates);
        }
    }

}



//    private GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
//    Dates dates = new Dates();
//
//
//    public void getCalendar() {
//        cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));    //получает текущий год
//        dates.setYear(cal.get(Calendar.YEAR));
//        System.out.println("Year: " + dates.getYear());
//        for (int month = 0; month <= 11; month++) {
//
////            if (statCal.isLeapYear(statCal.YEAR) && month == 1) {     //проверяет год на високосность
////                days[month] = 29;
////            }
//
//            cal.set(Calendar.MONTH, month);     //настраивает месяц в календаре на нужный
//            System.out.println("month = " + month);
//            System.out.println("Calendar.MONTH = " + cal.get(Calendar.MONTH));
//            dates.setMonth(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.UK));
//            dates.setMonthNumber(month);
//            System.out.println("Month # " + dates.getMonth());
////            System.out.println("Month # " + cal.get(Calendar.MONTH));
////            System.out.println(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.UK));
//
//            System.out.println("_____________________________" + "\n" +
//                    " Mon Tue Wed Thu Fri Sat Sun");
//
//            String[] calendarTable = new String[42];
//
//            for (int n = 0; n < calendarTable.length; n++) {
//
////                cal.set(Calendar.DAY_OF_MONTH, 1);
////                cal.set(Calendar.DAY_OF_WEEK, Calendar.getInstance().get(cal.get(Calendar.DAY_OF_MONTH)));
////
////                if (n > 2) {
////                    for (int k = 2; k < (cal.get(Calendar.DAY_OF_WEEK) - 2); k++) {
////                        n++;
////                    }
////                } else if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
////                    for (int k = 2; k < 6; k++) {
////                        n++;
////                    }
////                }
//
////                cal.set(Calendar.MONTH, month);
//                cal.set(Calendar.DAY_OF_MONTH, 1);
//                n += cal.get(Calendar.DAY_OF_WEEK) - 1;
////                dates.setDayOfWeek(cal.getDisplayName(cal.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH));
////                System.out.println("DoW: " + dates.getDayOfWeek());
//                System.out.println("n = " + n);
//                System.out.println("DoW: " + cal.getDisplayName(cal.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH));
//                System.out.println("month = " + cal.get(Calendar.MONTH));
//
//                for(int s = 0; s < n; s++){
//                    calendarTable[s] = "";
//                    System.out.printf("%4s", calendarTable[s]);
//                }
//
//                for (int i = 1; i <= cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++, n++) {
//                    cal.set(Calendar.DAY_OF_MONTH, i);
//                    calendarTable[n] = Integer.toString(i);
//                    dates.setDay(cal.get(Calendar.DAY_OF_MONTH));
//                    dates.setDayOfWeek(cal.getDisplayName(cal.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH));
//                    if ((n + 1) % 7 == 0) {
//                        System.out.printf("%4d,%10s", dates.getDay(), dates.getDayOfWeek());
////                        System.out.printf("%4s", calendarTable[n]);
//                        System.out.println();
//                    } else System.out.printf("%4d,%10s", dates.getDay(), dates.getDayOfWeek());
////                        System.out.printf("%4s", calendarTable[n]);
//
//                }
//                System.out.println();
//                break;
//            }
//        }
//    }


//
//
////    private String[] months = {
////            "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь",
////            "Октябрь", "Ноябрь", "Декабрь"
////    };
////
////    private int[] days = {
////            31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
////    };
////
////    public void getCalendar(){
////
////        for (int month = 0; month <= 11; month++) {
////
////            if (time.getYear() % 4 == 0 && month == 1) {
////                days[month] = 29;
////            }
////
////            System.out.println(months[month]);
////            for (int i = 1; i <= days[month]; i++) {
////                if (i % 7 == 0) {
////                    System.out.printf("%4d",i);
////                    System.out.println();
////                }
////                else System.out.printf("%4d",i);
////
////            }
////            System.out.println();
////        }
////    }