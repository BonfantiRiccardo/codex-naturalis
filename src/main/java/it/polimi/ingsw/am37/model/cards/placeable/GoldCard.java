package it.polimi.ingsw.am37.model.cards.placeable;

import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.sides.Back;
import it.polimi.ingsw.am37.model.sides.Front;

/**
 * The GoldCard class represents a generic gold card of the game. It is a subclass of the StandardCard abstract class.
 */
public class GoldCard extends StandardCard {
    /**
     * The GoldCard(id, front, back, mainResource) constructor uses the constructor of the superclass to assign the
     * id, front side, back side and mainResource.
     * @param id An integer to uniquely identify the card.
     * @param front A Front object that represents the front side of the card.
     * @param back  A Back object that represents the back side of the card.
     */
    public GoldCard(int id, Front front, Back back){
        super(id, front, back);
    }

    public String toString(){
        String ftl;
        String ftr;
        String fbl;
        String fbr;
        String br;
        String bonus;
        String plc;


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

        switch (this.getId()){
            case 41:
                bonus="1 🪶";
                plc="  🍄🍄🐺  ";
                break;
            case 42:
                bonus="1 🖋️";
                plc="  🍄🍄🍁  ";
                break;
            case 43:
                bonus="1 📜";
                plc="  🍄🍄🦋  ";
                break;
            case 44:
                bonus="2 🔲";
                plc=" 🍄🍄🍄🐺 ";
                break;
            case 45:
                bonus="2 🔲";
                plc=" 🍄🍄🍄🍁 ";
                break;
            case 46:
                bonus="2 🔲";
                plc=" 🍄🍄🍄🦋 ";
                break;
            case 47,48,49:
                bonus=" 3  ";
                plc="  🍄🍄🍄  ";
                break;
            case 50:
                bonus=" 5  ";
                plc="🍄🍄🍄🍄🍄";
                break;
            case 51:
                bonus="1 🪶";
                plc="  🍁🍁🦋  ";
                break;
            case 52:
                bonus="1 📜";
                plc="  🍁🍁🍄  ";
                break;
            case 53:
                bonus="1 🖋";
                plc="  🍁🍁🐺  ";
                break;
            case 54:
                bonus="2 🔲";
                plc=" 🍁🍁🍁🦋 ";
                break;
            case 55:
                bonus="2 🔲";
                plc=" 🍁🍁🍁🐺 ";
                break;
            case 56:
                bonus="2 🔲";
                plc=" 🍁🍁🍁🍄 ";
                break;
            case 57,58,59:
                bonus=" 3  ";
                plc="  🍁🍁🍁  ";
                break;
            case 60:
                bonus=" 5  ";
                plc="🍁🍁🍁🍁🍁";
                break;
            case 61:
                bonus="1 🖋";
                plc="  🐺🐺🦋  ";
                break;
            case 62:
                bonus="1 📜";
                plc="  🐺🐺🍁  ";
                break;
            case 63:
                bonus="1 🪶";
                plc="  🐺🐺🍄  ";
                break;
            case 64:
                bonus="2 🔲";
                plc=" 🐺🐺🐺🦋 ";
                break;
            case 65:
                bonus="2 🔲";
                plc=" 🐺🐺🐺🍄 ";
                break;
            case 66:
                bonus="2 🔲";
                plc=" 🐺🐺🐺🍁 ";
                break;
            case 67:
                bonus=" 3  ";
                plc="  🐺🐺🐺  ";
                break;
            case 68:
                bonus=" 3  ";
                plc="  🐺🐺🐺  ";
                break;
            case 69:
                bonus=" 3  ";
                plc="  🐺🐺🐺  ";
                break;
            case 70:
                bonus=" 5  ";
                plc="🐺🐺🐺🐺🐺";
                break;
            case 71:
                bonus="1 🪶";
                plc="  🦋🦋🍁  ";
                break;
            case 72:
                bonus="1 📜";
                plc="  🦋🦋🐺  ";
                break;
            case 73:
                bonus="1 🖋";
                plc="  🦋🦋🍄  ";
                break;
            case 74:
                bonus="2 🔲";
                plc=" 🦋🦋🦋🐺 ";
                break;
            case 75:
                bonus="2 🔲";
                plc=" 🦋🦋🦋🍁 ";
                break;
            case 76:
                bonus="2 🔲";
                plc=" 🦋🦋🦋🍄 ";
                break;
            case 77,78,79:
                bonus=" 3  ";
                plc="  🦋🦋🦋  ";
                break;
            case 80:
                bonus=" 5  ";
                plc="🦋🦋🦋🦋🦋";
                break;
            default:
                bonus="error";
                plc="error";
                break;
        }

        //System.out.println("\n");
        return "––––––––––—–––––––––––—    ––––––––––––—–––––—––––\n| "+ftl+" |    "+bonus+"    | "+ftr+" |   |    |           |    |\n|————              ————|   |————             ————|\n|                      |   |         "+br+"          |\n|————              ————|   |————             ————|\n| "+fbl+" | "+plc+" | "+fbr+" |   |    |           |    |\n––––––—––––––—–––––––––    –––––—–—–––––––––––––––";
    }

}