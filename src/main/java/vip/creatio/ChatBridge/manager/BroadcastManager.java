package vip.creatio.ChatBridge.manager;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpServer;
import net.md_5.bungee.api.ProxyServer;
import vip.creatio.ChatBridge.broadcast.ChatHandler;
import vip.creatio.ChatBridge.broadcast.JoinHandler;
import vip.creatio.ChatBridge.broadcast.LeaveHandler;
import vip.creatio.ChatBridge.broadcast.SwitchHandler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

public class BroadcastManager {
    private static final BroadcastManager instance = new BroadcastManager();
    private HttpServer server;

    private BroadcastManager() {
    }

    public static BroadcastManager getInstance() {
        return instance;
    }

    public void startServer() {
        try {
            server = HttpServer.create(new InetSocketAddress(ConfigManager.getInstance().getBroadcastPort()), 0);
            server.createContext("/join", new JoinHandler());
            server.createContext("/leave", new LeaveHandler());
            server.createContext("/switch", new SwitchHandler());
            server.createContext("/chat", new ChatHandler());
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        server.stop(0);
    }

    private void sendRequest(String url, byte[] data) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.connect();
        connection.setConnectTimeout(1000);
        connection.setReadTimeout(1000);
        DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
        dataOutputStream.write(data);
        dataOutputStream.flush();
        dataOutputStream.close();
        connection.getInputStream();
    }

    private byte[] getPostData(String player, String serverFrom, String serverTo, String serverOn, String message) {
        JSONObject data = new JSONObject();
        data.put("player", player);
        data.put("serverFrom", serverFrom);
        data.put("serverTo", serverTo);
        data.put("serverOn", serverOn);
        data.put("message", message);
        data.put("token", ConfigManager.getInstance().getBroadcastToken());
        return data.toString().getBytes();
    }

    private void broadcast(byte[] data, String path) {
        for (String addr : ConfigManager.getInstance().getBroadcastServers()) {
            Thread thread = new Thread(() -> {
                try {
                    sendRequest("http://" + addr + path, data);
                } catch (IOException e) {
                    ProxyServer.getInstance().getLogger().severe("Fail connect to " + addr + path);
                }
            });
            thread.start();
        }
    }

    public void onJoin(String player) {
        broadcast(getPostData(player, "", "", "", ""), "/join");
    }

    public void onLeave(String player) {
        broadcast(getPostData(player, "", "", "", ""), "/leave");
    }

    public void onSwitch(String player, String serverFrom, String serverTo) {
        broadcast(getPostData(player, serverFrom, serverTo, "", ""), "/switch");
    }

    public void onChat(String player, String serverOn, String message) {
        broadcast(getPostData(player, "", "", serverOn, message), "/chat");
    }
}