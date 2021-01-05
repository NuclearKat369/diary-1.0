//package sample;
//
//import org.w3c.dom.ls.LSOutput;
//
//import java.time.LocalDateTime;
//import java.time.Year;
//import java.time.YearMonth;
//import java.time.format.TextStyle;
//import java.util.*;
//import java.util.jar.JarOutputStream;
//
//
//public class StatCalendar {
//
//    private static LocalDateTime cal;
//    Dates dates = new Dates();
////    ArrayList<Dates> datesList = new ArrayList<>();
//
//    public StatCalendar(){
//        getCalendar();
//    }
//
//    public void getCalendar() {
//
//        cal = LocalDateTime.now();
//        dates.setYear(cal.getYear());
//        System.out.println("Year: " + dates.getYear());
//        for (int month = 1; month <= 12; month++) {
//
////            if (statCal.isLeapYear(statCal.YEAR) && month == 1) {     //проверяет год на високосность
////                days[month] = 29;
////            }
//
//            dates.setMonth(cal.withMonth(month).getMonth().getDisplayName(TextStyle.FULL, Locale.UK));
//            dates.setMonthNumber(month);
//            System.out.println("Month # " + dates.getMonth());
//
//            System.out.println("_____________________________" + "\n" +
//                    " Mon Tue Wed Thu Fri Sat Sun");
//
//            String[] calendarTable = new String[42];
//
//            for (int n = 0; n < calendarTable.length; n++) {
//
//
//                n += cal.withMonth(month).withDayOfMonth(1).getDayOfWeek().getValue() - 1;
////                System.out.println("dow CAL " + cal.withMonth(month).withDayOfMonth(1).getDayOfWeek().getValue());
//                dates.setDayOfWeek(cal.withMonth(dates.getMonthNumber()).withDayOfMonth(1).getDayOfWeek().toString());
//                System.out.println("DoW: " + dates.getDayOfWeek());
//
//                for (int s = 0; s < n; s++) {
//                    calendarTable[s] = " ";
//                    System.out.printf("%4s", calendarTable[s]);
//                }
//
//                YearMonth yearMonth = YearMonth.of(dates.getYear(), dates.getMonthNumber());
//
//                for (int i = 1; i <= yearMonth.lengthOfMonth(); i++, n++) {
//                    dates.setDay(i);
//                    dates.setDayOfWeek(cal.withYear(dates.getYear())
//                            .withMonth(dates.getMonthNumber())
//                            .withDayOfMonth(dates.getDay())
//                            .getDayOfWeek()
//                            .toString());
//                    if ((n + 1) % 7 == 0) {
////                        System.out.printf("%4d,%10s", dates.getDay(), dates.getDayOfWeek());
//                        System.out.printf("%4d", dates.getDay());
//                        System.out.println();
//                    } else
////                        System.out.printf("%4d,%10s", dates.getDay(), dates.getDayOfWeek());
//                        System.out.printf("%4d", dates.getDay());
//
//                }
//                System.out.println();
//                break;
//            }
////            datesList.add(dates);
//        }
//    }
//
//}
