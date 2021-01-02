package sample;

public class Appointment {

        private int day;
        private int month;
        private int year;
        private int hour;
        private int minutes;
        private String note;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
        public String toString() {
            return "Appointment:: Date=" + this.day + "." + this.month + "." + this.year +
                    " Time=" + this.hour + ":" + this.minutes + " Note=" + this.note;

        }
}