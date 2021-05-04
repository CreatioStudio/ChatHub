package vip.creatio.ChatBridge;

import vip.creatio.ChatBridge.manager.BroadcastManager;
import net.md_5.bungee.api.plugin.Plugin;
import vip.creatio.ChatBridge.event.EventListener;
import vip.creatio.ChatBridge.manager.ConfigManager;
import vip.creatio.ChatBridge.manager.MessageManager;
import vip.creatio.ChatBridge.advancedban.AdvancedBan;

public final class ChatBridge extends Plugin {
    private BroadcastManager broadcastManager;
    private ConfigManager configManager;
    private MessageManager messageManager;
    private boolean loadAdvancedBan = false;
    private AdvancedBan advancedBan;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        messageManager = new MessageManager(this);
        broadcastManager = new BroadcastManager(this);
        if (getProxy().getPluginManager().getPlugin("AdvancedBan") != null) {
            this.loadAdvancedBan = true;
            this.advancedBan = new AdvancedBan();
        }
        getProxy().getPluginManager().registerListener(this, new EventListener(this));
    }

    @Override
    public void onDisable() {
        broadcastManager.stopServer();
    }

    public BroadcastManager getBroadcastManager() {
        return broadcastManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public boolean isLoadAdvancedBan() {
        return loadAdvancedBan;
    }

    public AdvancedBan getAdvancedBan() {
        return advancedBan;
    }
}
