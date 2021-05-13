package vip.creatio.chatbridge.bungee.server;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import vip.creatio.chatbridge.bungee.config.ConfigManager;
import vip.creatio.chatbridge.tool.Net;

class BaseHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        try {
            JSONObject data = Net.parseData(exchange.getRequestBody());
            Net.sendResponse(exchange, handleRequest(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkToken(JSONObject data) {
        String token = data.getString("token");
        return token != null && token.equals(ConfigManager.getInstance().getBroadcastToken());
    }

    public String handleRequest(JSONObject data) {
        return "OK";
    }
}
