import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DrawButtonListener implements ActionListener {
    private UnoFlipModel model;
    UnoFlipModelViewFrame view;
    UnoFlipEvent event;
    private UnoFlipModel.Direction direction;

    public DrawButtonListener(UnoFlipModel model, UnoFlipEvent event, UnoFlipModelViewFrame view) {
        this.model = model;
        this.event = event;
        this.view = view;
        this.direction = UnoFlipModel.Direction.FORWARD;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        UnoPlayer nextPlayer = model.getNextPlayer(direction); // Implement this method in your model
        if (nextPlayer != null) {
            view.updateSouthTextArea(event);
        }
        view.updateCards();
    }
}
