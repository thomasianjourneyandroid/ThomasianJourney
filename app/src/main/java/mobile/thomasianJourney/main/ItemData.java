package mobile.thomasianJourney.main;

public class ItemData {
    private String eventName, eventVenue, eventTime;

    public ItemData(String eventName, String eventVenue, String eventTime) {
        this.eventName = eventName;
        this.eventVenue = eventVenue;
        this.eventTime = eventTime;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public String getEventTime() {
        return eventTime;
    }
}
