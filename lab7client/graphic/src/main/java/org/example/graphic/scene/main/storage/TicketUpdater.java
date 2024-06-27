package org.example.graphic.scene.main.storage;

import javafx.application.Platform;
import lombok.Getter;
import lombok.Setter;
import org.common.dto.Ticket;
import org.controller.MyController;
import org.example.graphic.scene.Application;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class TicketUpdater  extends Thread {

        @Getter
        private static final int TIMEOUT = 5 * 1000;

    public TicketUpdater(TicketStorage ticketStorage) {
        TicketUpdater.ticketStorage = ticketStorage;
    }

    public void run(){
            update(getNewData());
        }
        private static  TicketStorage ticketStorage = null;
        private final MyController controller = MyController.getInstance();
        public static void update(List<Ticket> newData){
            if (ticketStorage!=null) {
                var data = ticketStorage.getData();
                Platform.runLater(() -> {
                    AtomicBoolean flag = new AtomicBoolean(false);
                    data.removeIf(ticket -> {
                        var res = !newData.contains(ticket);
                        System.out.println(res);
                        flag.set(true);
                        return res;
                    });
                    var addedData = newData.stream().filter(ticket -> !data.contains(ticket)).toList();
                    data.addAll(addedData);
                    if (!addedData.isEmpty()) {
                        flag.set(true);
                    }
                    if (flag.get()) {
                        Application.getMainSceneObj().getZoomableCartesianPlot().updateMap();
                    }
                });
            }
        }
        private List<Ticket> getNewData() {

            while (true) {
                try {
                    return controller.show();
                }
                catch (Exception e) {
                }

            }
        }


}
