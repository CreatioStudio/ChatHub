package vip.creatio.ChatBridge.server;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import vip.creatio.ChatBridge.config.ConfigManager;
import vip.creatio.ChatBridge.tool.Net;

class BaseHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        try {
            JSONObject data = Net.parseData(exchange.getRequestBody());
            String token = data.getString("token");
            if (token != null && token.equals(ConfigManager.getInstance().getBroadcastToken())) {
                handleRequest(data);
                Net.sendResponse(exchange, "OK");
            } else {
                Net.sendResponse(exchange, "Error Token");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleRequest(JSONObject data) {
    }
}
