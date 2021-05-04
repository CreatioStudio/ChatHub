package vip.creatio.ChatBridge.broadcast;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpHandler;
import vip.creatio.ChatBridge.ChatBridge;

public class LeaveHandler extends BaseHandler implements HttpHandler {
    public LeaveHandler(ChatBridge chatBridge) {
        super(chatBridge);
    }

    @Override
    public void handleRequest(JSONObject data) {
        String player = data.getString("player");
        chatBridge.getMessageManager().onLeave(player);
    }
}