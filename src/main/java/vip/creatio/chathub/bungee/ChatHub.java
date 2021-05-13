package vip.creatio.chathub.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import vip.creatio.chathub.bungee.manager.OnlinePlayerListManager;
import vip.creatio.chathub.bungee.server.Server;
import vip.creatio.chathub.bungee.config.ConfigManager;
import vip.creatio.chathub.bungee.event.BungeeEventListener;
import vip.creatio.chathub.bungee.command.ChatCommand;

public final class ChatHub extends Plugin {
    @Override
    public void onEnable() {
        ConfigManager.getInstance().loadConfig(this);
        OnlinePlayerListManager.getInstance().init();
        Server.getInstance().startServer();
        getProxy().getPluginManager().registerListener(this, new BungeeEventListener());
        getProxy().getPluginManager().registerCommand(this, new ChatCommand());
    }

    @Override
    public void onDisable() {
        Server.getInstance().stopServer();
    }
}
