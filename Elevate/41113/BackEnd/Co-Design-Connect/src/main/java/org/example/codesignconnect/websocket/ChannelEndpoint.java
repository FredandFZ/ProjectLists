package org.example.codesignconnect.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.example.codesignconnect.config.GetHTTPSessionConfig;
import org.example.codesignconnect.model.Message;
import org.example.codesignconnect.utils.MessageUtils;
import org.example.codesignconnect.utils.WebsocketUtils;
import org.springframework.stereotype.Component;

@Slf4j
@ServerEndpoint(value = "/projects/{projectId}/channel", configurator = GetHTTPSessionConfig.class)
@Component
public class ChannelEndpoint {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private String path;
    private String user;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        try {
            HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
            this.user = (String) httpSession.getAttribute("user");
            this.path = session.getRequestURI().getPath();
            WebsocketUtils.onUserConnected(this.path, this.user, session);
        } catch (Exception e) {
            WebsocketUtils.printErrorMsg(e);
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        try {
            Message msgFrom = objectMapper.readValue(message, Message.class);
            String broadcastMsg = MessageUtils.getMessage(true, null, msgFrom.getMessage());
            WebsocketUtils.broadcast(this.path, broadcastMsg);
        } catch (Exception e) {
            WebsocketUtils.printErrorMsg(e);
        }
    }

    @OnClose
    public void onClose(Session session) {
        try {
            WebsocketUtils.onUserDisconnected(this.path, this.user);
        } catch (Exception e) {
            WebsocketUtils.printErrorMsg(e);
        }
    }
}
