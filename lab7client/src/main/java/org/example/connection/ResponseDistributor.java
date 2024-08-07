package org.example.connection;

import org.common.commands.Command;
import org.example.exception.ConnectionException;
import org.common.network.Response;
import org.example.exception.NoResponseException;
import org.example.exception.ReceivingException;

import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ResponseDistributor {
    private final UdpReceiver udpReceiver;
    private final BlockingQueue<Response> queue = new LinkedBlockingQueue<Response>();
    public ResponseDistributor(UdpReceiver udpReceiver) {
        this.udpReceiver = udpReceiver;
    }

    public Response getResponse(Byte responseId, Command command) throws ReceivingException{
        Iterator<Response> iterator = queue.iterator();
        while (iterator.hasNext()) {
            Response resp = iterator.next();
            if (Objects.equals(resp.getRequestId().getId(), responseId)) {
                iterator.remove();
                return resp;
            }
        }
            var newResp = udpReceiver.getResponse();
            if (newResp.getRequestId().getId() ==  responseId){
                return newResp;
            }
            if (newResp.getException() instanceof ReceivingException){
                var e =(ReceivingException)newResp.getException();
                e.setCommand(command);
                throw e;
            }
            if (newResp.getRequestId().getId()!=null)
                queue.add(newResp);

        throw  new NoResponseException("NoResponse",command);

    }
}
