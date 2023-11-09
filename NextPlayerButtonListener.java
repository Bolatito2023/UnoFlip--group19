import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

    public class NextPlayerButtonListener implements ActionListener {
        private UnoFlip model;
        UnoFlipModelViewFrame view;
        UnoFlipEvent event;
        private UnoFlip.Direction direction;

        public NextPlayerButtonListener(UnoFlip model,UnoFlipEvent event, UnoFlipModelViewFrame view) {
            this.model = model;
            this.event = event;
            this.view = view;
            this.direction = UnoFlip.Direction.FORWARD;
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

