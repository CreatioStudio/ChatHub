package vip.creatio.chathub.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import net.md_5.bungee.api.ProxyServer;
import vip.creatio.chathub.bungee.config.ConfigManager;
import vip.creatio.chathub.bungee.manager.MessageManager;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Net {
    public static JSONObject parseData(InputStream inputStream) {
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

    public static HttpURLConnection getConnection(String url, String method) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(method);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        connection.setConnectTimeout(1000);
        connection.setReadTimeout(1000);
        return connection;
    }

    public static JSONObject post(String url, Map<String, String> headers, byte[] data) throws IOException {
        HttpURLConnection connection = getConnection(url, "POST");
        for (String key : headers.keySet()) {
            connection.setRequestProperty(key, headers.get(key));
        }
        connection.connect();
        DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
        dataOutputStream.write(data);
        dataOutputStream.flush();
        dataOutputStream.close();
        return parseData(connection.getInputStream());
    }

    public static JSONObject postJSONRequest(String url, JSONObject data) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            return post(url, headers, data.toString().getBytes());
        } catch (IOException e) {
            ProxyServer.getInstance().getLogger().severe("Fail post data to " + url);
            return null;
        }
    }

    public static void sendResponse(HttpExchange exchange, JSONObject responseData) {
        try {
            String response = responseData.toString();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendResponse(HttpExchange exchange, String responseString) {
        JSONObject response = new JSONObject();
        response.put("message", responseString);
        sendResponse(exchange, response);
    }

    public static void broadcastEvent(String path, String player, String serverFrom, String serverTo, String serverOn, String message, int messageId, String target) {
        JSONObject data = new JSONObject();
        data.put("path", path);
        data.put("player", player);
        data.put("serverFrom", serverFrom);
        data.put("serverTo", serverTo);
        data.put("serverOn", serverOn);
        data.put("message", message);
        data.put("messageId", messageId);
        data.put("target", target);
        data.put("token", ConfigManager.getInstance().getBroadcastToken());
        new Thread(() -> {
            for (String addr : ConfigManager.getInstance().getBroadcastServers()) {
                JSONObject response = postJSONRequest("http://" + addr + path, data);
                if (path.equals("/chatTry") && response != null && response.containsKey("cancel") && response.getBoolean("cancel")) {
                    MessageManager.getInstance().cancelMessage(messageId);
                }
            }
        }).start();
    }

    public static void broadcastJoin(String player) {
        broadcastEvent("/join", player, "", "", "", "", -1, "");
    }

    public static void broadcastLeave(String player) {
        broadcastEvent("/leave", player, "", "", "", "", -1, "");
    }

    public static void broadcastSwitch(String player, String serverFrom, String serverTo) {
        broadcastEvent("/switch", player, serverFrom, serverTo, "", "", -1, "");
    }

    public static void broadcastChatTry(String player, String serverOn, String message, int messageId) {
        broadcastEvent("/chatTry", player, "", "", serverOn, message, messageId, "");
    }

    public static void broadcastChat(String player, String serverOn, String message) {
        broadcastEvent("/chat", player, "", "", serverOn, message, -1, "");
    }

    public static void broadcastMsg(String player, String message, String target) {
        broadcastEvent("/msg", player, "", "", "", message, -1, target);
    }
}
