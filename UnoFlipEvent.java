import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class UnoFlipEvent extends EventObject {
    private UnoFlip.Direction direction;


    public UnoFlipEvent(UnoFlip model, UnoFlip.Direction dir) {
        super(model);
        this.direction = dir;

    }

    public UnoPlayer getPlayer(){
        UnoFlip model = (UnoFlip) getSource(); // Retrieve the model from the EventObject
        if (model != null) {
            return model.getCurrentPlayer();
        }
        return null; // Return null if the model is null
    }

    /**public UnoPlayer getNextPlayer(UnoFlip.Direction direction) {
        if (player.isEmpty()) {
            return null; // No players in the list
        }

        int nextPlayerIndex;

        if (direction == UnoFlip.Direction.FORWARD) {
            nextPlayerIndex = currentPlayerIndex + 1;
            if (nextPlayerIndex >= player.size()) {
                nextPlayerIndex = 0; // Loop back to the first player
            }
        } else { // UnoFlip.Direction.BACKWARD
            nextPlayerIndex = currentPlayerIndex - 1;
            if (nextPlayerIndex < 0) {
                nextPlayerIndex = player.size() - 1; // Loop back to the last player
            }
        }

        currentPlayerIndex = nextPlayerIndex; // Update the current player index

        return player.get(currentPlayerIndex);
    }
    public void setPlayer(UnoPlayer pl){
        currentPlayerIndex = player.indexOf(pl);


    }*/

}





