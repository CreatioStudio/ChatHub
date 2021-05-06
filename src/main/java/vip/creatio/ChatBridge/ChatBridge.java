package vip.creatio.ChatBridge;

import net.md_5.bungee.api.plugin.Plugin;
import vip.creatio.ChatBridge.manager.ConfigManager;
import vip.creatio.ChatBridge.manager.BroadcastManager;
import vip.creatio.ChatBridge.event.EventListener;
import vip.creatio.ChatBridge.command.ChatCommand;

public final class ChatBridge extends Plugin {
    @Override
    public void onEnable() {
        ConfigManager.getInstance().loadConfig(this);
        BroadcastManager.getInstance().startServer();
        getProxy().getPluginManager().registerListener(this, new EventListener());
        getProxy().getPluginManager().registerCommand(this, new ChatCommand());
    }

    @Override
    public void onDisable() {
        BroadcastManager.getInstance().stopServer();
    }
}