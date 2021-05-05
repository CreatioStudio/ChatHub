package vip.creatio.ChatBridge.manager;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpServer;
import net.md_5.bungee.api.ProxyServer;
import vip.creatio.ChatBridge.broadcast.*;
import vip.creatio.ChatBridge.tool.Net;

import java.io.IOException;
import java.net.InetSocketAddress;

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

    private JSONObject getPostData(String player, String serverFrom, String serverTo, String serverOn, String message) {
        JSONObject data = new JSONObject();
        data.put("player", player);
        data.put("serverFrom", serverFrom);
        data.put("serverTo", serverTo);
        data.put("serverOn", serverOn);
        data.put("message", message);
        data.put("token", ConfigManager.getInstance().getBroadcastToken());
        return data;
    }

    private void broadcast(JSONObject data, String path) {
        for (String addr : ConfigManager.getInstance().getBroadcastServers()) {
            Thread thread = new Thread(() -> {
                try {
                    Net.getInstance().postJSONRequest("http://" + addr + path, data);
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
