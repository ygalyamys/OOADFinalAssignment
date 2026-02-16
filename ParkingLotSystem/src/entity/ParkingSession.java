package entity;

import java.time.LocalDateTime;

public class ParkingSession {
    private final String sessionId;
    private final String plateNo;
    private final String spotID;
    private final LocalDateTime entryTime;

    public ParkingSession(String sessionId, String plateNo, String spotID, LocalDateTime entryTime) {
        this.sessionId = sessionId;
        this.plateNo = plateNo;
        this.spotID = spotID;
        this.entryTime = entryTime;
    }

    public String getSessionId() { return sessionId; }
    public String getPlateNo() { return plateNo; }
    public String getSpotID() { return spotID; }
    public LocalDateTime getEntryTime() { return entryTime; }
}