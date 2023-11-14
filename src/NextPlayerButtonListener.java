import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//This class is the controller for the Next player button.
    public class NextPlayerButtonListener implements ActionListener {
        private UnoFlipModel model;
        UnoFlipModelViewFrame view;
        UnoFlipEvent event;
        private UnoFlipModel.Direction direction;

        public NextPlayerButtonListener(UnoFlipModel model, UnoFlipEvent event, UnoFlipModelViewFrame view) {
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

