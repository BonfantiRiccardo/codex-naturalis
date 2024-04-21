package it.polimi.ingsw.am37.virtualview;

import it.polimi.ingsw.am37.controller.GameController;

import java.util.EventListener;

public class GUIVirtualView extends VirtualView implements EventListener {
    public GUIVirtualView(GameController controller) {
        super(controller);
    }
}
