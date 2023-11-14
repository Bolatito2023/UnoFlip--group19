import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

//The event class tells the controller than an event has occurred.
// The controller will then update the view Frame.

public class UnoFlipEvent extends EventObject {
    private UnoFlipModel.Direction direction;


    public UnoFlipEvent(UnoFlipModel model, UnoFlipModel.Direction dir) {
        super(model);
        this.direction = dir;

    }

    public UnoPlayer getPlayer(){
        UnoFlipModel model = (UnoFlipModel) getSource(); // Retrieve the model from the EventObject
        if (model != null) {
            return model.getCurrentPlayer();
        }
        return null; // Return null if the model is null
    }


}





