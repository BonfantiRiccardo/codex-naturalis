package it.polimi.ingsw.am37.model.sides;

import it.polimi.ingsw.am37.model.game.Resource;

/**
 * The Corner class represents the concept of a generic corner of one of the sides of the card.
 */
public class Corner {
    /**
     * The isVisible attribute contains information about the visibility of this particular Corner.
     */
    private final boolean isVisible;
    /**
     * The resource attribute contains the Resource assigned to this particular Corner.
     */
    private final Resource resource;
    /**
     * The linkedSide attribute contains a reference to the Side object that covers this Corner. It is set to null if
     * the Corner is not yet covered.
     */
    private Side linkedSide;

    /**
     * The Corner(isVisible, resource) constructor assigns the values given as parameters to the isVisible and resource
     * attributes, and it sets the linkedSide to null.
     * @param isVisible A boolean that contains information about the visibility of the Corner. It is true if the
     *                  corner is visible, false if it is not visible.
     * @param resource A Resource object that represents the resource present in this Corner.
     */
    public Corner(boolean isVisible, Resource resource) {
        this.isVisible = isVisible;
        this.resource = resource;
        linkedSide = null;
    }

    /**
     * The getVisibility() method returns the value of the isVisible attribute.
     * @return true if the Corner is visible, false if the Corner is not visible.
     */
    public boolean getVisibility() {
        return isVisible;
    }

    /**
     * The getResource() method returns the Resource object contained in this Corner.
     * @return A Resource object that represents the resource present in this Corner.
     */
    public Resource getResource(){
        return resource;
    }

    /**
     * The getLinkedSide() method returns the Side object that covers this Corner. It returns null if no Side is
     * currently covering the Corner.
     * @return A reference to the Side object that covers this Corner.
     */
    public Side getLinkedSide(){
        return linkedSide;
    }

    /**
     * The setLinkedSide(linkedSide) method assigns the linkedSide attribute to the Side object given as parameter.
     * @param linkedSide A reference to the Side object that covers this Corner.
     */
    public void setLinkedSide(Side linkedSide){
        this.linkedSide = linkedSide;
    }

    @Override
    public String toString() {
        return "Corner{" + "isVisible=" + isVisible + ", resource=" + resource +
                (linkedSide!=null ? (", linkedSide=" + linkedSide) : "") +  '}';
    }
}
