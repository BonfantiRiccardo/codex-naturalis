package it.polimi.ingsw.am37.virtualview;

import it.polimi.ingsw.am37.controller.GameController;

import java.util.EventListener;

public class CLIVirtualView extends VirtualView implements EventListener {
    public CLIVirtualView(GameController controller) {
        super(controller);
    }
}
