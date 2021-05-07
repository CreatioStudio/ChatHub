package vip.creatio.chatbridge.bungee.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

import vip.creatio.chatbridge.bungee.ChatBridge;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Config {
    private HashMap<String, String> messageMap;
    private HashMap<String, String> serverNameMap;
    private int port;
    private String broadcastToken;
    private int broadcastCancelTimeout;
    private List<String> broadcastServers;
    private List<String> ignoreRules;
    private List<String> blockWords;
    private boolean qqEnable;
    private String qqPath;
    private String qqApiURL;
    private boolean qqMessage;
    private List<Integer> qqGroupId;

    protected Config() {
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
            messageMap = new HashMap<>();
            chatBridge.getLogger().info("Set message:");
            for (String event : messageConfiguration.getKeys()) {
                String messageString = messageConfiguration.get(event).toString();
                messageMap.put(event, messageString);
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

            // port
            port = config.getInt("port");
            chatBridge.getLogger().info("Set http server port to " + port);

            // broadcast
            broadcastToken = config.getString("broadcast.token");
            chatBridge.getLogger().info("Set broadcast token to \"" + broadcastToken + "\"");
            broadcastCancelTimeout = config.getInt("broadcast.cancel_timeout");
            chatBridge.getLogger().info("Set broadcast cancel timeout to " + broadcastCancelTimeout + "ms");
            broadcastServers = config.getStringList("broadcast.servers");
            broadcastServers.remove("host:port");
            displayList(chatBridge, "Set broadcast servers:", broadcastServers);

            // ignore_rules
            ignoreRules = config.getStringList("ignore_rules");
            displayList(chatBridge, "Set ignore rules:", ignoreRules);

            // block_words
            blockWords = config.getStringList("block_words");
            displayList(chatBridge, "Set block words:", blockWords);

            // qq
            qqEnable = config.getBoolean("qq.enable");
            chatBridge.getLogger().info("Set qq enable to " + qqEnable);
            qqPath = config.getString("qq.path");
            chatBridge.getLogger().info("Set qq path to " + qqPath);
            qqApiURL = config.getString("qq.api_url");
            chatBridge.getLogger().info("Set qq api url to " + qqApiURL);
            qqMessage = config.getBoolean("qq.message");
            chatBridge.getLogger().info("Set qq message to " + qqMessage);
            qqGroupId = config.getIntList("qq.group_id");
            displayList(chatBridge, "Set qq group id:", qqGroupId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <E> void displayList(ChatBridge chatBridge, String title, List<E> list) {
        chatBridge.getLogger().info(title);
        for (E i : list) {
            chatBridge.getLogger().info("  - " + i);
        }
    }

    public HashMap<String, String> getMessageMap() {
        return messageMap;
    }

    public HashMap<String, String> getServerNameMap() {
        return serverNameMap;
    }

    public int getPort() {
        return port;
    }

    public String getBroadcastToken() {
        return broadcastToken;
    }

    public int getBroadcastCancelTimeout() {
        return broadcastCancelTimeout;
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

    public boolean isQqEnable() {
        return qqEnable;
    }

    public String getQqPath() {
        return qqPath;
    }

    public String getQqApiURL() {
        return qqApiURL;
    }

    public boolean isQqMessage() {
        return qqMessage;
    }

    public List<Integer> getQqGroupId() {
        return qqGroupId;
    }
}
