package it.polimi.ingsw.am37.model.cards.placeable;

import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.sides.Back;
import it.polimi.ingsw.am37.model.sides.Front;

/**
 * The ResourceCard class represents a generic resource card of the game. It is a subclass of the StandardCard
 * abstract class.
 */
public class ResourceCard extends StandardCard {
    /**
     * The ResourceCard(id, front, back, mainResource) constructor uses the constructor of the superclass to assign the
     * id, front side, back side and mainResource.
     * @param id An integer to uniquely identify the card.
     * @param front A Front object that represents the front side of the card.
     * @param back  A Back object that represents the back side of the card.
     */
    public ResourceCard(int id, Front front, Back back){
        super(id, front, back);
    }

    public String toString(){
        String ftl;
        String ftr;
        String fbl;
        String fbr;
        int points=this.getFront().getPointsGivenOnPlacement();
        String br;

        if(this.getFront().getTL().getVisibility()){
            switch (this.getFront().getTL().getResource()){
                case Resource.ANIMAL:
                    ftl="🐺";
                    break;
                case Resource.PLANT:
                    ftl="🍁";
                    break;
                case Resource.INSECT:
                    ftl="🦋";
                    break;
                case Resource.EMPTY:
                    ftl="  ";
                    break;
                case Resource.FUNGI:
                    ftl="🍄";
                    break;
                case Resource.INKWELL:
                    ftl="🖋️";
                    break;
                case Resource.MANUSCRIPT:
                    ftl="📜";
                    break;
                case Resource.QUILL:
                    ftl="🪶";
                    break;
                default:
                    ftl="error";
                    break;
            }
        }
        else{
            ftl="❌";
        }

        if(this.getFront().getTR().getVisibility()){
            switch (this.getFront().getTR().getResource()){
                case Resource.ANIMAL:
                    ftr="🐺";
                    break;
                case Resource.PLANT:
                    ftr="🍁";
                    break;
                case Resource.INSECT:
                    ftr="🦋";
                    break;
                case Resource.EMPTY:
                    ftr="  ";
                    break;
                case Resource.FUNGI:
                    ftr="🍄";
                    break;
                case Resource.INKWELL:
                    ftr="🖋️";
                    break;
                case Resource.MANUSCRIPT:
                    ftr="📜";
                    break;
                case Resource.QUILL:
                    ftr="🪶";
                    break;
                default:
                    ftr="error";
                    break;
            }
        }
        else{
            ftr="❌";
        }

        if(this.getFront().getBL().getVisibility()){
            switch (this.getFront().getBL().getResource()){
                case Resource.ANIMAL:
                    fbl="🐺";
                    break;
                case Resource.PLANT:
                    fbl="🍁";
                    break;
                case Resource.INSECT:
                    fbl="🦋";
                    break;
                case Resource.EMPTY:
                    fbl="  ";
                    break;
                case Resource.FUNGI:
                    fbl="🍄";
                    break;
                case Resource.INKWELL:
                    fbl="🖋️";
                    break;
                case Resource.MANUSCRIPT:
                    fbl="📜";
                    break;
                case Resource.QUILL:
                    fbl="🪶";
                    break;
                default:
                    fbl="error";
                    break;
            }
        }
        else{
            fbl="❌";
        }

        if(this.getFront().getBR().getVisibility()){
            switch (this.getFront().getBR().getResource()){
                case Resource.ANIMAL:
                    fbr="🐺";
                    break;
                case Resource.PLANT:
                    fbr="🍁";
                    break;
                case Resource.INSECT:
                    fbr="🦋";
                    break;
                case Resource.EMPTY:
                    fbr="  ";
                    break;
                case Resource.FUNGI:
                    fbr="🍄";
                    break;
                case Resource.INKWELL:
                    fbr="🖋️";
                    break;
                case Resource.MANUSCRIPT:
                    fbr="📜";
                    break;
                case Resource.QUILL:
                    fbr="🪶";
                    break;
                default:
                    fbr="error";
                    break;
            }
        }
        else{
            fbr="❌";
        }

        switch (this.getBack().getMainResource()){
            case Resource.ANIMAL:
                br="🐺";
                break;
            case Resource.INSECT:
                br="🦋";
                break;
            case Resource.PLANT:
                br="🍁";
                break;
            case Resource.FUNGI:
                br="🍄";
                break;
            default:
                br="error";
                break;
        }

        System.out.println("\n");
        return "––––––––––—–––––––––––—    ––––––––––––—–––––—––––\n| "+ftl+" |     "+points+"      | "+ftr+" |   |    |           |    |\n|————              ————|   |————             ————|\n|                      |   |         "+br+"          |\n|————              ————|   |————             ————|\n| "+fbl+" |            | "+fbr+" |   |    |           |    |\n––––––—––––––—–––––––––    –––––—–—–––––––––––––––";
    }
}
