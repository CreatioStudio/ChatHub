package vip.creatio.chatbridge.bungee.server;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpServer;
import vip.creatio.chatbridge.bungee.config.ConfigManager;
import vip.creatio.chatbridge.bungee.event.ChatBridgeEventHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

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
            server = HttpServer.create(new InetSocketAddress(ConfigManager.getInstance().getPort()), 0);
            server.createContext("/join", new BaseHandler() {
                @Override
                public String handleRequest(JSONObject data) {
                    if (this.checkToken(data)) {
                        ChatBridgeEventHandler.getInstance().onReceiveBroadcastJoin(data);
                        return super.handleRequest(data);
                    } else {
                        return "Error Token";
                    }
                }
            });
            server.createContext("/leave", new BaseHandler() {
                @Override
                public String handleRequest(JSONObject data) {
                    if (this.checkToken(data)) {
                        ChatBridgeEventHandler.getInstance().onReceiveBroadcastLeave(data);
                        return super.handleRequest(data);
                    } else {
                        return "Error Token";
                    }
                }
            });
            server.createContext("/switch", new BaseHandler() {
                @Override
                public String handleRequest(JSONObject data) {
                    if (this.checkToken(data)) {
                        ChatBridgeEventHandler.getInstance().onReceiveBroadcastSwitch(data);
                        return super.handleRequest(data);
                    } else {
                        return "Error Token";
                    }
                }
            });
            server.createContext("/chatTry", new BaseHandler());
            server.createContext("/chat", new BaseHandler() {
                @Override
                public String handleRequest(JSONObject data) {
                    if (this.checkToken(data)) {
                        ChatBridgeEventHandler.getInstance().onReceiveBroadcastChat(data);
                        return super.handleRequest(data);
                    } else {
                        return "Error Token";
                    }
                }
            });
            server.createContext("/msg", new BaseHandler() {
                @Override
                public String handleRequest(JSONObject data) {
                    if (this.checkToken(data)) {
                        ChatBridgeEventHandler.getInstance().onReceiveBroadcastMsg(data);
                        return super.handleRequest(data);
                    } else {
                        return "Error Token";
                    }
                }
            });
            server.createContext(ConfigManager.getInstance().getQqPath(), new BaseHandler() {
                @Override
                public String handleRequest(JSONObject data) {
                    ChatBridgeEventHandler.getInstance().onReceiveQQPost(data);
                    return super.handleRequest(data);
                }
            });
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        server.stop(0);
    }
}
