package util;

public class Appointment extends CalendarItem{
    private String withWhom;



    public Appointment(String title, DateTime start, DateTime end, String owner, String location,
                       String withWhom) {
        super(title, start, end, owner, location);
        this.withWhom = withWhom;
    }

    public String getWithWhom() {
        return withWhom;
    }

    @Override
    public String getType() {
        return "Appointment";
    }
    @Override
    public String getDetails() {
        return null;
    }

    @Override
    public String toString() {
        return String.format("APPOINTMENT, %s, %s, %s, %s, %s, %s",
                title, start.toString(), end.toString(), owner, location, withWhom);
    }
}
