package vip.creatio.chatbridge.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import vip.creatio.chatbridge.bungee.manager.OnlinePlayerListManager;
import vip.creatio.chatbridge.bungee.server.Server;
import vip.creatio.chatbridge.bungee.config.ConfigManager;
import vip.creatio.chatbridge.bungee.event.BungeeEventListener;
import vip.creatio.chatbridge.bungee.command.ChatCommand;

public final class ChatBridge extends Plugin {
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
