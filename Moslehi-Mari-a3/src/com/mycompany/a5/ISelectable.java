package com.mycompany.a5;

public interface ISelectable {
    /**
     * Determines whether the point (adjustedX, adjustedY) is within the bounds
     * of the selectable object.
     * 
     * @param adjustedX The X-coordinate of the point.
     * @param adjustedY The Y-coordinate of the point.
     * @return True if the point is within the object, otherwise false.
     */
    boolean contains(int adjustedX, int adjustedY);

    /**
     * Sets whether the object is selected.
     * 
     * @param isSelected True to select the object, false to deselect.
     */
    void setSelected(boolean isSelected);

    /**
     * Checks if the object is currently selected.
     * 
     * @return True if selected, otherwise false.
     */
    boolean isSelected();
}
