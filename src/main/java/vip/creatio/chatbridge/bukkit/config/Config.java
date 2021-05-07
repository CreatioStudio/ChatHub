package vip.creatio.chatbridge.bukkit.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import vip.creatio.chatbridge.bukkit.ChatBridgeBukkit;

import java.io.File;

public class Config {
    private static final Config instance = new Config();
    private int port;
    private String broadcastToken;

    private Config() {
    }

    public static Config getInstance() {
        return instance;
    }

    public void loadConfig(ChatBridgeBukkit chatBridgeBukkit) {
        File biuConfigFile = new File(chatBridgeBukkit.getDataFolder(), "config_bukkit.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(biuConfigFile);
        port = config.getInt("port");
        chatBridgeBukkit.getLogger().info("Set http server port to " + port);

        broadcastToken = config.getString("broadcast_token");
        chatBridgeBukkit.getLogger().info("Set broadcast token to \"" + broadcastToken + "\"");
    }


    public int getPort() {
        return port;
    }

    public String getBroadcastToken() {
        return broadcastToken;
    }
}
