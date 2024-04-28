package it.polimi.ingsw.am37.common.messages;

public class CreationMessage extends Message{
    private final String creator;
    private final int num;

    public CreationMessage(MessageId id, String creator, int num) {
        super(id);
        this.creator = creator;
        this.num = num;

    }

    public String getCreator() {
        return creator;
    }

    public int getNum() {
        return num;
    }

    @Override
    public String toString() {
        return "Received: " + creator + ", " + num;
    }
}
