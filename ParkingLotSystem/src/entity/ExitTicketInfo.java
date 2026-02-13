package entity;

import java.time.LocalDateTime;

public class ExitTicketInfo {
    private final String ticketId;
    private final String plateNo;
    private final String spotId;
    private final LocalDateTime entryTime;

    public ExitTicketInfo(String ticketId, String plateNo, String spotId, LocalDateTime entryTime) {
        this.ticketId = ticketId;
        this.plateNo = plateNo;
        this.spotId = spotId;
        this.entryTime = entryTime;
    }

    public String getTicketId() { return ticketId; }
    public String getPlateNo() { return plateNo; }
    public String getSpotId() { return spotId; }
    public LocalDateTime getEntryTime() { return entryTime; }
}