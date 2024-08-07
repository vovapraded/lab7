package org.example.graphic.scene.main.command.remove;

import org.controller.MyController;
import org.example.graphic.localizator.Localizator;
import org.example.graphic.scene.Application;
import org.example.graphic.scene.Popup;
import org.example.graphic.scene.main.command.MyButton;
import org.example.graphic.scene.main.storage.TicketUpdater;

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
                var messageAndTickets = controller.remove(ticket.getId());
                var message = messageAndTickets.getLeft();
                var tickets = messageAndTickets.getRight();
                TicketUpdater.update(tickets);



            } catch (Exception e) {
                Popup.showError(e);
            }
        }
    }
}
