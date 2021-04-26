package io.github.zhanganzhi.chatbridge.broadcast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.github.zhanganzhi.chatbridge.ChatBridge;

class BaseHandler implements HttpHandler {
    protected final ChatBridge chatBridge;

    public BaseHandler(ChatBridge chatBridge) {
        this.chatBridge = chatBridge;
    }

    protected JSONObject parseData(InputStream inputStream) {
        StringBuilder dataString = new StringBuilder();
        try {
            Scanner scanner = new Scanner(inputStream);
            String temp = "";
            while (temp != null) {
                temp = scanner.nextLine();
                dataString.append(temp);
            }
        } catch (NoSuchElementException ignored) {
        }
        return JSON.parseObject(dataString.toString());
    }

    protected void sendResponse(HttpExchange exchange, String response) throws IOException {
        JSONObject responseData = new JSONObject();
        responseData.put("message", response);
        response = responseData.toJSONString();
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            JSONObject data = parseData(exchange.getRequestBody());
            String token = data.getString("token");
            if (token != null && token.equals(chatBridge.getConfigManager().getToken())) {
                handleRequest(data);
                sendResponse(exchange, "OK");
            } else {
                sendResponse(exchange, "Error Token");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleRequest(JSONObject data) {
    }
}