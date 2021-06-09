package com.duan.imserver;

import com.alibaba.fastjson.JSON;
import com.duan.imserver.service.GroupService;
import com.duan.imserver.vo.MessageVO;
import com.duan.imserver.vo.UserVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Log4j2
@ServerEndpoint(value = "/websocket/{name}")
public class WebSocketServer {

    private String name;
    private Session session;
    private static final ConcurrentHashMap<String, WebSocketServer> webSocketServers = new ConcurrentHashMap<>();

    private static GroupService groupService;

    @Autowired
    public void setGroupService(GroupService groupService) {
        WebSocketServer.groupService = groupService;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("name") String name) {
        this.session = session;
        this.name = name;
        this.webSocketServers.put(name, this);
        log.info("[WebSocket] 连接成功，name = " + this.name);
    }

    @OnClose
    public void onClose() {
        this.webSocketServers.remove(this.name);
        log.info("[WebSocket] 关闭连接，uid = " + this.name);
    }

    @OnMessage
    public void onMessage(String data) {
        log.info("[WebSocket] 收到消息，name = " + this.name + ", data = " + data);
        sendMessage(data);
    }

    public void sendMessage(String data) {
        MessageVO messageVO = JSON.parseObject(data, MessageVO.class);
        if (messageVO.getType() == '0') {
            sendMessageToSingle(messageVO);
        } else {
            sendMessageToGroup(messageVO);
        }
    }

    private void sendMessageToSingle(MessageVO messageVO) {
        sendMessageToUser(messageVO, null);
    }

    private void sendMessageToGroup(MessageVO messageVO) {
        List<UserVO> users = groupService.getMembers(messageVO.getTo());
        users.forEach(user -> {
            sendMessageToUser(messageVO, user);
        });
    }

    private void sendMessageToUser(MessageVO messageVO, UserVO userVO) {
        String to = userVO == null ? messageVO.getTo() : userVO.getUserId();
        // 对方在线才发送消息
        if (webSocketServers.containsKey(to)) {
            try {
                String data = JSON.toJSONString(messageVO);
                webSocketServers.get(to).session.getBasicRemote().sendText(data);
                log.info("[WebSocket] 转发消息，from = " + messageVO.getFrom() + ", to = " + to + ", data = " + data);
            } catch (IOException e) {
                log.error(e);
                e.printStackTrace();
            }
        }
    }

}
