import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class UnoFlipController implements ActionListener {
        UnoFlip model;
        UnoFlipEvent event;
        UnoFlipModelViewFrame view;
        public UnoFlipController(){

          /** UnoFlip model,UnoFlipEvent event, UnoFlipModelViewFrame view  this.model = model;
            this.event = event;
            this.view=view;*/
        }


        @Override
        public void actionPerformed(ActionEvent e) {
            /**if (model.getCurrentPlayer() != null) {
                UnoPlayer nextPlayer = model.getNextPlayer(); // Get the next player from the model

                if (nextPlayer != null) {
                    event.setPlayer(nextPlayer); // Update the event with the next player
                    view.updateSouthTextArea(event); // Update the view
                }

            }*/
        }

}

