package vip.creatio.chatbridge.bukkit.server;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import vip.creatio.chatbridge.bukkit.config.Config;
import vip.creatio.chatbridge.tool.Net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

public class Server {
    private static final Server instance = new Server();
    private HttpServer server;

    private Server() {
    }

    public static Server getInstance() {
        return instance;
    }

    public void startServer() {
        try {
            server = HttpServer.create(new InetSocketAddress(Config.getInstance().getPort()), 0);
            server.createContext("/join", new BaseHandler());
            server.createContext("/leave", new BaseHandler());
            server.createContext("/switch", new BaseHandler());
            server.createContext("/chatTry", new BaseHandler() {
                @Override
                public boolean handleRequest(HttpExchange exchange, JSONObject data) {
                    JSONObject response = new JSONObject();
                    response.put("cancel", false);

                    Player player = Bukkit.getPlayer(data.getString("player"));
                    if (player != null) {
                        String message = data.getString("message");
                        Set<Player> players = new HashSet<>(Bukkit.getOnlinePlayers());
                        AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(true, player, message, players);
                        Bukkit.getPluginManager().callEvent(event);
                        response.put("cancel", event.isCancelled());
                    }
                    Net.sendResponse(exchange, response);
                    return true;
                }
            });
            server.createContext("/chat", new BaseHandler());
            server.createContext("/msg", new BaseHandler());
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        server.stop(0);
    }
}
