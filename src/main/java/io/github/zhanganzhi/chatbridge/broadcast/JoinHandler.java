package io.github.zhanganzhi.chatbridge.broadcast;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpHandler;
import io.github.zhanganzhi.chatbridge.ChatBridge;

public class JoinHandler extends BaseHandler implements HttpHandler {
    public JoinHandler(ChatBridge chatBridge) {
        super(chatBridge);
    }

    @Override
    public void handleRequest(JSONObject data) {
        String player = data.getString("player");
        chatBridge.getMessageManager().onJoin(player);
    }
}