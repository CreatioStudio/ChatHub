package io.github.zhanganzhi.chatbridge.broadcast;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpHandler;
import io.github.zhanganzhi.chatbridge.ChatBridge;

public class SwitchHandler extends BaseHandler implements HttpHandler {
    public SwitchHandler(ChatBridge chatBridge) {
        super(chatBridge);
    }

    @Override
    public void handleRequest(JSONObject data) {
        String player = data.getString("player");
        String serverFrom = data.getString("serverFrom");
        String serverTo = data.getString("serverTo");
        chatBridge.getMessageManager().onSwitch(player, serverFrom, serverTo);
    }
}