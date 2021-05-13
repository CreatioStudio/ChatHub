package vip.creatio.chathub.bungee.event;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.api.event.*;

public class BungeeEventListener implements Listener {
    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        ChatHubEventHandler.getInstance().onJoin(event);
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        ChatHubEventHandler.getInstance().onLeave(event);
    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
        ChatHubEventHandler.getInstance().onSwitch(event);
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        ChatHubEventHandler.getInstance().onChat(event);
    }

    @EventHandler
    public void onTabComplete(TabCompleteEvent event) {
        ChatHubEventHandler.getInstance().onTabComplete(event);
    }
}
