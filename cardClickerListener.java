import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class cardClickerListener implements ActionListener {
    UnoFlipModelViewFrame view;
    UnoFlipEvent event;
    private UnoFlip.Direction direction;
    private UnoFlip model;
    public cardClickerListener(UnoFlip model,UnoFlipEvent event, UnoFlipModelViewFrame view) {
        this.model = model;
        this.event = event;
        this.view = view;
        this.direction = UnoFlip.Direction.FORWARD;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
           Card card = model.getCurrentCard();
         //  model.getCurrentPlayer().playCard(card);
         //  view.updateCards();

    }
}
