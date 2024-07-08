package org.example.connection;

import org.common.network.Response;
import org.example.utility.NoResponseException;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ResponseDistributor {
    private final UdpReceiver udpReceiver;
    private final BlockingQueue<Response> queue = new LinkedBlockingQueue<Response>();
    public ResponseDistributor(UdpReceiver udpReceiver) {
        this.udpReceiver = udpReceiver;
    }

    public Response getResponse(byte responseId){
        Iterator<Response> iterator = queue.iterator();
        while (iterator.hasNext()) {
            Response resp = iterator.next();
            if (resp.getRequestId().getId() == responseId) {
                iterator.remove();
                return resp;
            }
        }
        try {
            var newResp = udpReceiver.getResponse();
            if (newResp.getRequestId().getId() == responseId){
                return newResp;
            }
            queue.add(newResp);
        }catch (NoResponseException ignored){
        }
        throw new NoResponseException("NoResponse");

    }
}
