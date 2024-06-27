package org.example.graphic.scene.main.storage;

import javafx.application.Platform;
import lombok.Getter;
import lombok.SneakyThrows;
import org.common.dto.Ticket;
import org.common.network.ConnectionException;
import org.common.network.SendException;
import org.controller.MyController;

import java.util.List;


public class TicketUpdater  extends Thread {

        @Getter
        private static final int TIMEOUT = 5 * 1000;

    public TicketUpdater(TicketStorage ticketStorage) {
        this.ticketStorage = ticketStorage;
    }
    private  List<Ticket> newData = null;

    public void run(){
            update();
        }
        private  final TicketStorage ticketStorage;
        private final MyController controller = MyController.getInstance();
        private void update(){

          var data = ticketStorage.getData();
               while (!updateNewData()){
                   System.out.println("Ошибка обновления данных");
               }

            Platform.runLater(() -> {
                data.removeIf(ticket -> {
                    var res = !newData.contains(ticket);
                    System.out.println(res);
                    return res;
                });
                var addedData = newData.stream().filter(ticket -> !data.contains(ticket)).toList();
                data.addAll(addedData);
            });
        }
        private boolean updateNewData()  {
            try {
                newData = controller.show();
                return true;
            }catch (ConnectionException e){
                System.out.println("ошибка отправки");
                return false;
            }catch (Exception e){
                return false;
            }

        }

}
