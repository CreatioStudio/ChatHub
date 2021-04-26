package io.github.zhanganzhi.chatbridge.manager;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

import io.github.zhanganzhi.chatbridge.ChatBridge;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;


public class ConfigManager {
    private Configuration config;
    private String ignorePrefix;
    private String token;
    private final HashMap<String, String> message = new HashMap<>();
    private final HashMap<String, String> serverNameMap = new HashMap<>();
    private int broadcastPort;
    private ArrayList<String> broadcastServers;

    public ConfigManager(ChatBridge chatBridge) {
        if (!chatBridge.getDataFolder().exists()) {
            if (!chatBridge.getDataFolder().mkdir()) {
                chatBridge.getLogger().warning("Cannot make data folder");
            }
        }
        File configFile = new File(chatBridge.getDataFolder(), "config.yml");
        try {
            if (!configFile.exists()) {
                Files.copy(chatBridge.getResourceAsStream("config.yml"), configFile.toPath());
            }
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadConfig();
    }

    private void loadConfig() {
        // ignore_prefix
        ignorePrefix = config.getString("ignore_prefix");

        // token
        token = config.getString("token");

        // message
        Configuration messageConfiguration = (Configuration) config.get("message");
        message.put("join", messageConfiguration.getString("join"));
        message.put("leave", messageConfiguration.getString("leave"));
        message.put("switch", messageConfiguration.getString("switch"));
        message.put("chat", messageConfiguration.getString("chat"));

        // server_name
        Configuration serverNameConfiguration = (Configuration) config.get("server_name");
        for (String serverId : serverNameConfiguration.getKeys()) {
            serverNameMap.put(serverId, serverNameConfiguration.get(serverId).toString());
        }

        // broadcast
        broadcastPort = config.getInt("broadcast.port");
        broadcastServers = (ArrayList<String>) config.getStringList("broadcast.servers");
        broadcastServers.remove("host:port");
    }

    public String getIgnorePrefix() {
        return ignorePrefix;
    }

    public String getToken() {
        return token;
    }

    public String getServerName(String serverId) {
        String serverName = serverNameMap.get(serverId);
        return serverName == null ? serverId : serverName;
    }

    public String getMessage(String key) {
        return message.get(key);
    }

    public int getBroadcastPort() {
        return broadcastPort;
    }

    public ArrayList<String> getBroadcastServers() {
        return broadcastServers;
    }
}
