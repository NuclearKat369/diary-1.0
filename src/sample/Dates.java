package sample;

public class Dates {

    private int year, day, monthNumber;
    private String month, dayOfWeek;

    public Dates(){
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setMonthNumber(int monthNumber){
        this.monthNumber = monthNumber;
    }

    public int getMonthNumber(){
        return monthNumber;
    }
}
