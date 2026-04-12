package util;

public class OnlineMeeting extends Meeting{
    private String platform;
    private String meetingLink;

    public OnlineMeeting(String title, DateTime start, DateTime end, String owner, String location,
            int invitees, String agenda, String platform, String meetingLink) {
        super(title, start, end, owner, location, invitees, agenda);
        this.platform = platform;
        this.meetingLink = meetingLink;
    }

    public String getPlatform() {
        return platform;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    @Override
    public String getType() {
        return "Online Meeting";
    }

    @Override
    public String toString() {
        return String.format("ONLINE, %s, %s, %s, %s, %s, %d, %s, %s, %s",
                title, start.toString(), end.toString(), owner, location, invitees, agenda, platform, meetingLink);
    }

}
