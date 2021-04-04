package io.github.zhanganzhi.chatbridge.event;

import io.github.zhanganzhi.chatbridge.ChatBridge;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.chat.TextComponent;

public class Event implements Listener {
    private final ChatBridge chatBridge;

    public Event(ChatBridge plugin) {
        chatBridge = plugin;
    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        String serverName = chatBridge.getServerMapManager().getServerName(event.getTarget().getName());
        String playerName = event.getPlayer().getName();
        for (ProxiedPlayer target : chatBridge.getProxy().getPlayers()) {
            target.sendMessage(new TextComponent("§7[§3§l" + serverName + "§7]§e" + playerName + "§e: joined the game"));
        }
    }

    @EventHandler
    public void onServerDisconnect(ServerDisconnectEvent event) {
        String serverName = chatBridge.getServerMapManager().getServerName(event.getTarget().getName());
        String playerName = event.getPlayer().getName();
        for (ProxiedPlayer target : chatBridge.getProxy().getPlayers()) {
            target.sendMessage(new TextComponent("§7[§3§l" + serverName + "§7]§e" + playerName + "§e: left the game"));
        }
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        if (event.isCommand() || event.isProxyCommand()) {
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        if (this.chatBridge.getLoadAdvancedBan()) {
            String uuid = this.chatBridge.getAdvancedBan().getUUIDManager().getUUID(player.getName());
            if (this.chatBridge.getAdvancedBan().getPunishmentManager().isMuted(uuid)) {
                return;
            }
        }
        if (!event.getMessage().startsWith("!!")) {
            event.setCancelled(true);
            String serverName = chatBridge.getServerMapManager().getServerName(player.getServer().getInfo().getName());
            String playerName = player.getName();
            for (ProxiedPlayer target : chatBridge.getProxy().getPlayers()) {
                target.sendMessage(new TextComponent("§7[§3§l" + serverName + "§7]§e" + playerName + "§r: " + event.getMessage()));
            }
        }
    }
}
