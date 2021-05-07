package vip.creatio.chatbridge.server;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpServer;
import vip.creatio.chatbridge.config.ConfigManager;
import vip.creatio.chatbridge.event.ChatBridgeEventHandler;
import vip.creatio.chatbridge.manager.MessageManager;
import vip.creatio.chatbridge.tool.Net;

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
                public void handleRequest(JSONObject data) {
                    ChatBridgeEventHandler.getInstance().onReceiveBroadcastJoin(data);
                }
            });
            server.createContext("/leave", new BaseHandler() {
                @Override
                public void handleRequest(JSONObject data) {
                    ChatBridgeEventHandler.getInstance().onReceiveBroadcastLeave(data);
                }
            });
            server.createContext("/switch", new BaseHandler() {
                @Override
                public void handleRequest(JSONObject data) {
                    ChatBridgeEventHandler.getInstance().onReceiveBroadcastSwitch(data);
                }
            });
            server.createContext("/chatTry", new BaseHandler());
            server.createContext("/chat", new BaseHandler() {
                @Override
                public void handleRequest(JSONObject data) {
                    ChatBridgeEventHandler.getInstance().onReceiveBroadcastChat(data);
                }
            });
            server.createContext("/chatCancel", new BaseHandler() {
                @Override
                public void handleRequest(JSONObject data) {
                    MessageManager.getInstance().cancelMessage(data.getInteger("messageId"));
                }
            });
            server.createContext("/msg", new BaseHandler() {
                @Override
                public void handleRequest(JSONObject data) {
                    ChatBridgeEventHandler.getInstance().onReceiveBroadcastMsg(data);
                }
            });
            server.createContext(ConfigManager.getInstance().getQqPath(), exchange -> {
                try {
                    ChatBridgeEventHandler.getInstance().onReceiveQQPost(Net.parseData(exchange.getRequestBody()));
                    Net.sendResponse(exchange, "");
                } catch (IOException e) {
                    e.printStackTrace();
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