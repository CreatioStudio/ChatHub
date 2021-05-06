package vip.creatio.ChatBridge.broadcast;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import vip.creatio.ChatBridge.manager.ConfigManager;
import vip.creatio.ChatBridge.qq.Handler;
import vip.creatio.ChatBridge.qq.Info;
import vip.creatio.ChatBridge.tool.Net;

public class QQHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        try {
            if (!ConfigManager.getInstance().isQqEnable()) {
                return;
            }
            JSONObject data = Net.getInstance().parseData(exchange.getRequestBody());
            if (data.getString("post_type").equals("message")) {
                Info info = new Info(data);
                Handler.getInstance().onMessage(info);
            }
            Net.getInstance().sendResponse(exchange, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
