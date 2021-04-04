package io.github.zhanganzhi.chatbridge;

import net.md_5.bungee.api.plugin.Plugin;
import io.github.zhanganzhi.chatbridge.event.Event;
import io.github.zhanganzhi.chatbridge.manager.ServerNameManager;
import io.github.zhanganzhi.chatbridge.advancedban.AdvancedBan;

public final class ChatBridge extends Plugin {
    private ServerNameManager serverNameManager;
    private boolean loadAdvancedBan = false;
    private AdvancedBan advancedBan;

    @Override
    public void onEnable() {
        serverNameManager = new ServerNameManager(this);
        if (getProxy().getPluginManager().getPlugin("AdvancedBan") != null) {
            this.loadAdvancedBan = true;
            this.advancedBan = new AdvancedBan();
        }
        getProxy().getPluginManager().registerListener(this, new Event(this));
    }

    public ServerNameManager getServerMapManager() {
        return this.serverNameManager;
    }

    public boolean getLoadAdvancedBan() {
        return this.loadAdvancedBan;
    }

    public AdvancedBan getAdvancedBan() {
        return this.advancedBan;
    }
}
