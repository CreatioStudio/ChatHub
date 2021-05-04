package vip.creatio.ChatBridge.broadcast;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpHandler;
import vip.creatio.ChatBridge.manager.MessageManager;

public class ChatHandler extends BaseHandler implements HttpHandler {
    @Override
    public void handleRequest(JSONObject data) {
        String player = data.getString("player");
        String serverOn = data.getString("serverOn");
        String message = data.getString("message");
        MessageManager.getInstance().onChat(player, serverOn, message);
    }
}