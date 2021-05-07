package vip.creatio.ChatBridge;

import net.md_5.bungee.api.plugin.Plugin;
import vip.creatio.ChatBridge.manager.OnlinePlayerListManager;
import vip.creatio.ChatBridge.server.Server;
import vip.creatio.ChatBridge.config.ConfigManager;
import vip.creatio.ChatBridge.event.BungeeEventListener;
import vip.creatio.ChatBridge.command.ChatCommand;

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
