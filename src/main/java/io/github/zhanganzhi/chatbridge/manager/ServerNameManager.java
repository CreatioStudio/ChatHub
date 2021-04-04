package io.github.zhanganzhi.chatbridge.manager;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.io.IOException;

import io.github.zhanganzhi.chatbridge.ChatBridge;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class ServerNameManager {
    private final HashMap<String, String> ServerNameData = new HashMap<>();

    public ServerNameManager(ChatBridge chatBridge) {
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
            Configuration serverNameConfiguration = (Configuration) config.get("server_name");
            for (String serverId : serverNameConfiguration.getKeys()) {
                ServerNameData.put(serverId, serverNameConfiguration.get(serverId).toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getServerName(String serverId) {
        String serverName = ServerNameData.get(serverId);
        return serverName == null ? serverId : serverName;
    }
}
