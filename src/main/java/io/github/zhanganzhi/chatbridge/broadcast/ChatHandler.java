package io.github.zhanganzhi.chatbridge.broadcast;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpHandler;
import io.github.zhanganzhi.chatbridge.ChatBridge;

public class ChatHandler extends BaseHandler implements HttpHandler {
    public ChatHandler(ChatBridge chatBridge) {
        super(chatBridge);
    }

    @Override
    public void handleRequest(JSONObject data) {
        String player = data.getString("player");
        String serverOn = data.getString("serverOn");
        String message = data.getString("message");
        chatBridge.getMessageManager().onChat(player, serverOn, message);
    }
}