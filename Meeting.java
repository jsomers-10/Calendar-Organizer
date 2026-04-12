package util;

public class Meeting extends CalendarItem{
    int invitees;
    String agenda;

    public Meeting(String title, DateTime start, DateTime end, String owner, String location,
                   int invitees, String agenda) {
        super(title, start, end, owner, location);
        this.invitees = invitees;
        this.agenda = agenda;
    }

    public int getInvitees() {
        return invitees;
    }

    public String getAgenda() {
        return agenda;
    }

    @Override
    public String getType() {
        return "In-person meeting";
    }

    @Override
    public String getDetails() {
        return null;
    }
    @Override
    public String toString() {
        return String.format("MEETING, %s, %s, %s, %s, %s, %d, %s",
                title, start.toString(), end.toString(), owner, location, invitees, agenda);
    }
}
