package vip.creatio.chatbridge.bukkit;

import org.bukkit.plugin.java.JavaPlugin;
import vip.creatio.chatbridge.bukkit.config.Config;
import vip.creatio.chatbridge.bukkit.server.Server;

public final class ChatBridgeBukkit extends JavaPlugin {

    @Override
    public void onEnable() {
        saveResource("config_bukkit.yml", false);
        Config.getInstance().loadConfig(this);
        Server.getInstance().startServer();
    }

    @Override
    public void onDisable() {
        Server.getInstance().stopServer();
    }
}
