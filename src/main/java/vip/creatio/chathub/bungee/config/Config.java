package vip.creatio.chathub.bungee.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

import vip.creatio.chathub.bungee.ChatHub;
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
        loadConfig();
    }

    public void loadConfig() {
        if (!ChatHub.getInstance().getDataFolder().exists()) {
            if (!ChatHub.getInstance().getDataFolder().mkdir()) {
                ChatHub.getInstance().getLogger().warning("Cannot make data folder");
            }
        }
        File configFile = new File(ChatHub.getInstance().getDataFolder(), "config.yml");
        try {
            if (!configFile.exists()) {
                Files.copy(ChatHub.getInstance().getResourceAsStream("config.yml"), configFile.toPath());
            }
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);

            // message
            Configuration messageConfiguration = (Configuration) config.get("message");
            messageMap = new HashMap<>();
            ChatHub.getInstance().getLogger().info("Set message:");
            for (String event : messageConfiguration.getKeys()) {
                String messageString = messageConfiguration.get(event).toString();
                messageMap.put(event, messageString);
                ChatHub.getInstance().getLogger().info("  - " + event + " : " + messageString);
            }

            // server_name
            Configuration serverNameConfiguration = (Configuration) config.get("server_name");
            serverNameMap = new HashMap<>();
            ChatHub.getInstance().getLogger().info("Set server name mapping:");
            for (String serverId : serverNameConfiguration.getKeys()) {
                String serverName = serverNameConfiguration.get(serverId).toString();
                serverNameMap.put(serverId, serverName);
                ChatHub.getInstance().getLogger().info("  - " + serverId + " : " + serverName);
            }

            // port
            port = config.getInt("port");
            ChatHub.getInstance().getLogger().info("Set http server port to " + port);

            // broadcast
            broadcastToken = config.getString("broadcast.token");
            ChatHub.getInstance().getLogger().info("Set broadcast token to \"" + broadcastToken + "\"");
            broadcastCancelTimeout = config.getInt("broadcast.cancel_timeout");
            ChatHub.getInstance().getLogger().info("Set broadcast cancel timeout to " + broadcastCancelTimeout + "ms");
            broadcastServers = config.getStringList("broadcast.servers");
            broadcastServers.remove("host:port");
            displayList("Set broadcast servers:", broadcastServers);

            // ignore_rules
            ignoreRules = config.getStringList("ignore_rules");
            displayList("Set ignore rules:", ignoreRules);

            // block_words
            blockWords = config.getStringList("block_words");
            displayList("Set block words:", blockWords);

            // qq
            qqEnable = config.getBoolean("qq.enable");
            ChatHub.getInstance().getLogger().info("Set qq enable to " + qqEnable);
            qqPath = config.getString("qq.path");
            ChatHub.getInstance().getLogger().info("Set qq path to " + qqPath);
            qqApiURL = config.getString("qq.api_url");
            ChatHub.getInstance().getLogger().info("Set qq api url to " + qqApiURL);
            qqMessage = config.getBoolean("qq.message");
            ChatHub.getInstance().getLogger().info("Set qq message to " + qqMessage);
            qqGroupId = config.getIntList("qq.group_id");
            displayList("Set qq group id:", qqGroupId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public <E> void displayList(String title, List<E> list) {
        ChatHub.getInstance().getLogger().info(title);
        for (E i : list) {
            ChatHub.getInstance().getLogger().info("  - " + i);
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
