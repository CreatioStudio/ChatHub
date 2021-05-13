package vip.creatio.chathub.bukkit;

import org.bukkit.plugin.java.JavaPlugin;
import vip.creatio.chathub.bukkit.config.Config;
import vip.creatio.chathub.bukkit.server.Server;

public final class ChatHubBukkit extends JavaPlugin {

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
