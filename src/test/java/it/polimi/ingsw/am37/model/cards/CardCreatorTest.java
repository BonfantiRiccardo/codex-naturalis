package it.polimi.ingsw.am37.model.cards;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.am37.model.cards.placeable.GoldCard;
import it.polimi.ingsw.am37.model.cards.placeable.ResourceCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardCreatorTest {
    CardCreator cc = new CardCreator();

    /**
     * The cardCreationTest() tests that the creation of the gold and resource cards works as intended. If the creation
     * fails the assertion will not be passed and the test will fail.
     */
    @Test
    void cardCreationTest() {
        List<Card> rcList = cc.createCards("/it/polimi/ingsw/am37/cards/ResourceCards.json", new TypeToken<ArrayList<ResourceCard>>() {}.getType());

        assertNotNull(rcList);

        ResourceCard rc = (ResourceCard) rcList.get(0);
        System.out.println(rc.toString());

        List<Card> gcList = cc.createCards("/it/polimi/ingsw/am37/cards/GoldCards.json", new TypeToken<ArrayList<GoldCard>>() {}.getType());

        assertNotNull(gcList);

        GoldCard gc = (GoldCard) gcList.get(0);
        System.out.println(gc.toString());

        assertThrows(RuntimeException.class, () -> cc.createCards("wrong_filename.json", new TypeToken<ArrayList<ResourceCard>>() {}.getType()));

    }
}