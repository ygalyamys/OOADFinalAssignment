package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import enums.SpotCategory;

public class Ticket {
    private String ticketID;
    private String plateNumber;
    private String spotID;
    private LocalDateTime entryTime;
    private SpotCategory spotCategory;

    public Ticket(String plateNumber, String spotID, SpotCategory spotCategory) {
        this.plateNumber = plateNumber;
        this.spotID = spotID;
        this.spotCategory = spotCategory;
        this.entryTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        this.ticketID = "T-" + plateNumber + "-" + entryTime.format(formatter);
    }

    public String getTicketID() {
        return ticketID;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getSpotID() {
        return spotID;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public String getFormattedEntryTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return entryTime.format(formatter);
    }

    public SpotCategory getSpotCategory() {
        return spotCategory;
    }

}
