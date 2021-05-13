package vip.creatio.chathub.bukkit.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import vip.creatio.chathub.bukkit.ChatHubBukkit;

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

    public void loadConfig(ChatHubBukkit chatHubBukkit) {
        File biuConfigFile = new File(chatHubBukkit.getDataFolder(), "config_bukkit.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(biuConfigFile);
        port = config.getInt("port");
        chatHubBukkit.getLogger().info("Set http server port to " + port);

        broadcastToken = config.getString("broadcast_token");
        chatHubBukkit.getLogger().info("Set broadcast token to \"" + broadcastToken + "\"");
    }


    public int getPort() {
        return port;
    }

    public String getBroadcastToken() {
        return broadcastToken;
    }
}
