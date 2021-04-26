package io.github.zhanganzhi.chatbridge.event;

import io.github.zhanganzhi.chatbridge.ChatBridge;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;

public class EventListener implements Listener {
    private final ChatBridge chatBridge;

    public EventListener(ChatBridge chatBridge) {
        this.chatBridge = chatBridge;
    }


    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        String player = event.getPlayer().getName();
        chatBridge.getMessageManager().onJoin(player);
        chatBridge.getBroadcastManager().onJoin(player);
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        String player = event.getPlayer().getName();
        chatBridge.getMessageManager().onLeave(player);
        chatBridge.getBroadcastManager().onLeave(player);
    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
        ProxiedPlayer player = event.getPlayer();
        String playerName = player.getName();
        String serverFrom = event.getFrom() == null ? "" : event.getFrom().getName();
        String serverTo = player.getServer().getInfo().getName();
        chatBridge.getMessageManager().onSwitch(playerName, serverFrom, serverTo);
        chatBridge.getBroadcastManager().onSwitch(playerName, serverFrom, serverTo);
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        if (event.isCommand() || event.isProxyCommand()) {
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        if (this.chatBridge.isLoadAdvancedBan()) {
            if (this.chatBridge.getAdvancedBan().isMuted(player)) {
                return;
            }
        }
        String message = event.getMessage();
        if (message.startsWith(chatBridge.getConfigManager().getIgnorePrefix())) {
            return;
        }
        event.setCancelled(true);
        String playerName = player.getName();
        String serverOn = player.getServer().getInfo().getName();
        chatBridge.getMessageManager().onChat(playerName, serverOn, message);
        chatBridge.getBroadcastManager().onChat(playerName, serverOn, message);
    }
}