package it.polimi.ingsw.am37.common.messages;

public class ErrorMessage extends Message{
    private final String description;

    public ErrorMessage(MessageId id, String description) {
        super(id);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
