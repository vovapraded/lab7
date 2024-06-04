package org.example.graphic.scene.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import org.common.dto.Ticket;
import org.controller.MyController;

@Getter @Setter
public class TicketStorage {
    private ObservableList<Ticket> data;

    public TicketStorage() {
        try {
            data = FXCollections.observableArrayList(MyController.getInstance().show());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
