package a1;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

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
