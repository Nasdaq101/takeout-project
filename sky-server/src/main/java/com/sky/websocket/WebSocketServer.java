package com.sky.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket server
 */
@Component
@ServerEndpoint("/ws/{sid}")
public class WebSocketServer {

    // session object storage
    private static Map<String, Session> sessionMap = new HashMap<>();

    /**
     * callback method on successful connection
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        System.out.println("client server：" + sid + "connection established");
        sessionMap.put(sid, session);
    }

    /**
     * Callback method on receiving client message
     *
     * @param message client's message
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        System.out.println("from client server：" + sid + "received message:" + message);
    }

    /**
     * callback method on termination of connection
     *
     * @param sid
     */
    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        System.out.println("connection from:" + sid + "terminated.");
        sessionMap.remove(sid);
    }

    /**
     * send text to all clients
     *
     * @param message
     */
    public void sendToAllClient(String message) {
        Collection<Session> sessions = sessionMap.values();
        for (Session session : sessions) {
            try {
                // Message Sent from Server to Client
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}