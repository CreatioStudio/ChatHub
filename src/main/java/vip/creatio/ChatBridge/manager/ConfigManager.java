package vip.creatio.ChatBridge.manager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

import vip.creatio.ChatBridge.ChatBridge;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;


public class ConfigManager {
    private static final ConfigManager instance = new ConfigManager();
    private HashMap<String, String> message;
    private HashMap<String, String> serverNameMap;
    private int broadcastPort;
    private String broadcastToken;
    private List<String> broadcastServers;
    private List<String> ignoreRules;
    private List<String> blockWords;

    private ConfigManager() {
    }

    public static ConfigManager getInstance() {
        return instance;
    }

    public void loadConfig(ChatBridge chatBridge) {
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
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);

            // message
            Configuration messageConfiguration = (Configuration) config.get("message");
            message = new HashMap<>();
            chatBridge.getLogger().info("Set message:");
            for (String event : messageConfiguration.getKeys()) {
                String messageString = messageConfiguration.get(event).toString();
                message.put(event, messageString);
                chatBridge.getLogger().info("  - " + event + " : " + messageString);
            }

            // server_name
            Configuration serverNameConfiguration = (Configuration) config.get("server_name");
            serverNameMap = new HashMap<>();
            chatBridge.getLogger().info("Set server name mapping:");
            for (String serverId : serverNameConfiguration.getKeys()) {
                String serverName = serverNameConfiguration.get(serverId).toString();
                serverNameMap.put(serverId, serverName);
                chatBridge.getLogger().info("  - " + serverId + " : " + serverName);
            }

            // broadcast
            broadcastPort = config.getInt("broadcast.port");
            chatBridge.getLogger().info("Set broadcast port to " + broadcastPort);
            broadcastToken = config.getString("broadcast.token");
            chatBridge.getLogger().info("Set broadcast token to " + broadcastToken);
            broadcastServers = config.getStringList("broadcast.servers");
            broadcastServers.remove("host:port");
            displayStringList(chatBridge, "Set broadcast servers:", broadcastServers);

            // ignore_rules
            ignoreRules = config.getStringList("ignore_rules");
            displayStringList(chatBridge, "Set ignore rules:", ignoreRules);

            // block_words
            blockWords = config.getStringList("block_words");
            displayStringList(chatBridge, "Set block words:", blockWords);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayStringList(ChatBridge chatBridge, String title, List<String> list) {
        chatBridge.getLogger().info(title);
        for (String i : list) {
            chatBridge.getLogger().info("  - " + i);
        }
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

    public String getBroadcastToken() {
        return broadcastToken;
    }

    public List<String> getBroadcastServers() {
        return broadcastServers;
    }

    public List<String> getIgnoreRules() {
        return ignoreRules;
    }

    public List<String> getBlockWords() {
        return blockWords;
    }
}
