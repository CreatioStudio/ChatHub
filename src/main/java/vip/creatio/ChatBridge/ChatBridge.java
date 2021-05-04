package vip.creatio.ChatBridge;

import vip.creatio.ChatBridge.manager.BroadcastManager;
import net.md_5.bungee.api.plugin.Plugin;
import vip.creatio.ChatBridge.event.EventListener;
import vip.creatio.ChatBridge.manager.ConfigManager;

public final class ChatBridge extends Plugin {
    @Override
    public void onEnable() {
        ConfigManager.getInstance().init(this);
        BroadcastManager.getInstance().startServer();
        getProxy().getPluginManager().registerListener(this, new EventListener());
    }

    @Override
    public void onDisable() {
        BroadcastManager.getInstance().stopServer();
    }
}