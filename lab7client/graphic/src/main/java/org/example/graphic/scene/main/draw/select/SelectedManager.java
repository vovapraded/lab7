package org.example.graphic.scene.main.draw.select;

import lombok.Getter;
import lombok.Setter;
import org.example.graphic.scene.Application;
import org.example.graphic.scene.main.CreatorTable;
import org.example.graphic.scene.main.storage.TicketStorage;

public class SelectedManager {
    private final TicketStorage ticketStorage;
    private final CreatorTable creatorTable;
    @Getter
    private static Long selectedId;

    public static void setSelectedId(Long selectedId) {
        if (selectedId == null ||  !selectedId.equals(SelectedManager.selectedId)){
            SelectedManager.selectedId = selectedId;
        }else {
            SelectedManager.selectedId = null;
        }

    }

    public SelectedManager(TicketStorage ticketStorage, CreatorTable creatorTable) {
        this.ticketStorage = ticketStorage;
        this.creatorTable = creatorTable;
    }

    public void tryToSelectTicket(){
            ticketStorage.unmakeAllTicketsSelected();
            creatorTable.getTable().getSelectionModel().clearSelection();
            if (selectedId!=null) {
                ticketStorage.makeTicketSelected(selectedId);
                creatorTable.selectTicket(selectedId,true);
            }
        }
    }

