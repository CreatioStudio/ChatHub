package vip.creatio.chatbridge.bungee.server;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import vip.creatio.chatbridge.bungee.config.ConfigManager;
import vip.creatio.chatbridge.tool.Net;

class BaseHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        JSONObject data = Net.parseData(exchange.getRequestBody());
        String token = data.getString("token");
        if (token != null && token.equals(ConfigManager.getInstance().getBroadcastToken())) {
            if (!handleRequest(data)) {
                Net.sendResponse(exchange, "OK");
            }
        } else {
            Net.sendResponse(exchange, "Error Token");
        }
    }

    public boolean handleRequest(JSONObject data) {
        return false;
    }
}
