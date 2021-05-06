package vip.creatio.ChatBridge.event;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import vip.creatio.ChatBridge.advancedban.AdvancedBan;
import vip.creatio.ChatBridge.manager.BroadcastManager;
import vip.creatio.ChatBridge.manager.ConfigManager;
import vip.creatio.ChatBridge.manager.MessageManager;
import vip.creatio.ChatBridge.manager.PlayerListManager;
import vip.creatio.ChatBridge.qq.Bot;
import vip.creatio.ChatBridge.qq.QQManager;

import java.util.regex.Pattern;

public class EventListener implements Listener {
    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        String player = event.getPlayer().getName();
        PlayerListManager.getInstance().join(player);
        MessageManager.getInstance().onJoin(player);
        BroadcastManager.getInstance().onJoin(player);
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        String player = event.getPlayer().getName();
        PlayerListManager.getInstance().leave(player);
        MessageManager.getInstance().onLeave(player);
        BroadcastManager.getInstance().onLeave(player);
    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
        ProxiedPlayer player = event.getPlayer();
        String playerName = player.getName();
        String serverFrom = event.getFrom() == null ? "" : event.getFrom().getName();
        String serverTo = player.getServer().getInfo().getName();
        MessageManager.getInstance().onSwitch(playerName, serverFrom, serverTo);
        BroadcastManager.getInstance().onSwitch(playerName, serverFrom, serverTo);
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        String message = event.getMessage();
        boolean isCommand = event.isCommand() || event.isProxyCommand();
        boolean isMuted = ProxyServer.getInstance().getPluginManager().getPlugin("AdvancedBan") != null && AdvancedBan.isMuted(player);
        if (isCommand || isMuted) {
            return;
        }
        for (String ignoreRule : ConfigManager.getInstance().getIgnoreRules()) {
            if (Pattern.matches(ignoreRule, message)) {
                return;
            }
        }
        for (String blockWord : ConfigManager.getInstance().getBlockWords()) {
            if (message.contains(blockWord)) {
                MessageManager.getInstance().onBlockWord(player, message, blockWord);
                event.setCancelled(true);
                return;
            }
        }
        event.setCancelled(true);
        String playerName = player.getName();
        String serverOn = player.getServer().getInfo().getName();
        MessageManager.getInstance().onChat(playerName, serverOn, message);
        BroadcastManager.getInstance().onChat(playerName, serverOn, message);
        if (ConfigManager.getInstance().isQqMessage()) {
            QQManager.getInstance().onChat(playerName, serverOn, message);
        }
    }
}
