package org.example.graphic.scene.main.command.remove;

import org.controller.MyController;
import org.example.graphic.localizator.Localizator;
import org.example.graphic.scene.Application;
import org.example.graphic.scene.Popup;
import org.example.graphic.scene.main.command.MyButton;

public class RemoveButton extends MyButton {
    private MyController controller = MyController.getInstance();
    public RemoveButton() {
        super("Remove");
    }
    private final Localizator localizator = Localizator.getInstance();
    @Override
    public void onClick() {
        var creatorTable = Application.getMainSceneObj().getCreatorTable();
        var table = creatorTable.getTable();
        var ticketStorage =         Application.getMainSceneObj().getTicketStorage();
        var ticket=table.getSelectionModel().getSelectedItem();
        if (ticket!=null){
            try {
                var message = controller.remove(ticket.getId());
                Popup.showDialog(message);

                ticketStorage.getData().removeIf(ticket1 -> ticket1.getId().equals(ticket.getId()));
                Application.getMainSceneObj().getCreatorTable().updatePagination();
                Application.getMainSceneObj().getZoomableCartesianPlot().updateMap();

            } catch (Exception e) {
                Popup.showError(localizator.getKeyString(e.getMessage()));
            }
        }
    }
}
