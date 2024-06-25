package org.example.graphic.scene.main.draw.select;

import org.example.graphic.scene.Application;
import org.example.graphic.scene.main.CreatorTable;
import org.example.graphic.scene.main.TicketStorage;

public class SelectedManager {
    private final TicketStorage ticketStorage;
    private final CreatorTable creatorTable;

    public SelectedManager(TicketStorage ticketStorage, CreatorTable creatorTable) {
        this.ticketStorage = ticketStorage;
        this.creatorTable = creatorTable;
    }

    public void tryToSelectTicket(Long id){
        var selectedTicketOpt = ticketStorage.findSelectedTicket();
        var newSelectionOpt = ticketStorage.getFilteredData().stream().filter(ticket -> ticket.getId().equals(id))
                .findAny();
        if (newSelectionOpt.isPresent()) {
            var newSelection = newSelectionOpt.get();
            ticketStorage.unmakeAllTicketsSelected();
            creatorTable.getTable().getSelectionModel().clearSelection();
            if (selectedTicketOpt.isEmpty() || !selectedTicketOpt.get().getTicket().equals(newSelection)) {
                ticketStorage.makeTicketSelected(newSelection.getId());
                creatorTable.selectTicket(newSelection);
            }
            Application.getMainSceneObj().getZoomableCartesianPlot().updateMap();
        }
    }
}
