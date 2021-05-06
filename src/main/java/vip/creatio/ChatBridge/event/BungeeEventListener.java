package vip.creatio.ChatBridge.event;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.api.event.*;

public class BungeeEventListener implements Listener {
    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        ChatBridgeEventHandler.getInstance().onJoin(event);
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        ChatBridgeEventHandler.getInstance().onLeave(event);
    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
        ChatBridgeEventHandler.getInstance().onSwitch(event);
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        ChatBridgeEventHandler.getInstance().onChat(event);
    }

    @EventHandler
    public void onTabComplete(TabCompleteEvent event) {
        ChatBridgeEventHandler.getInstance().onTabComplete(event);
    }
}
