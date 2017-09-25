package a1;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

class ColorCommand extends AbstractAction {
    private final Starter starter;

    public ColorCommand(Starter starter) {
        this.starter = starter;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        int result = (starter.isGradient() + 1) % 2;
        starter.setGradient(result);

    }
}
