package vip.creatio.chathub.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import vip.creatio.chathub.bungee.manager.OnlinePlayerListManager;
import vip.creatio.chathub.bungee.server.Server;
import vip.creatio.chathub.bungee.event.BungeeEventListener;
import vip.creatio.chathub.bungee.command.ChatCommand;

public final class ChatHub extends Plugin {
    private static ChatHub instance;

    public ChatHub() {
        instance = this;
    }

    public static ChatHub getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
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
