package vip.creatio.ChatBridge.broadcast;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpHandler;
import vip.creatio.ChatBridge.manager.MessageManager;

public class JoinHandler extends BaseHandler implements HttpHandler {
    @Override
    public void handleRequest(JSONObject data) {
        String player = data.getString("player");
        MessageManager.getInstance().onJoin(player);
    }
}