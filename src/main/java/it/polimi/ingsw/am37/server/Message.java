package it.polimi.ingsw.am37.server;

import java.io.Serializable;

public class Message implements Serializable {
    private final String creator;
    private final int num;

    public Message(String creator, int num) {
        this.creator = creator;
        this.num = num;
    }

    public String getCreator() {
        return creator;
    }

    public int getNum() {
        return num;
    }
}
