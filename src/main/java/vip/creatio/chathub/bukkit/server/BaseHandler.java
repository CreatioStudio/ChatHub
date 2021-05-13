package vip.creatio.chathub.bukkit.server;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import vip.creatio.chathub.bukkit.config.Config;
import vip.creatio.chathub.tool.Net;

class BaseHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        try {
            JSONObject data = Net.parseData(exchange.getRequestBody());
            String token = data.getString("token");
            if (token != null && token.equals(Config.getInstance().getBroadcastToken())) {
                if (!handleRequest(exchange, data)) {
                    Net.sendResponse(exchange, "OK");
                }
            } else {
                Net.sendResponse(exchange, "Error Token");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean handleRequest(HttpExchange exchange, JSONObject data) {
        return false;
    }
}
