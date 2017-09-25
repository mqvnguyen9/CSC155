package a1;

/*
Michael Nguyen
CSC 155
Assignment 1
 */

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

/*
This class allows users to press the letter 'c' to change the color of the triangle from a solid color to a gradient of
three colors.
 */
class ColorCommand extends AbstractAction {
    private final StarterModel starterModel;

    public ColorCommand(StarterModel starterModel) {
        this.starterModel = starterModel;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        int result = (starterModel.isGradient() + 1) % 2;
        starterModel.setGradient(result);

    }
}
