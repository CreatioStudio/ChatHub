package vip.creatio.chathub.bungee.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import vip.creatio.chathub.bungee.config.ConfigManager;
import vip.creatio.chathub.bungee.manager.OnlinePlayerListManager;
import vip.creatio.chathub.bungee.qq.Bot;
import vip.creatio.chathub.bungee.server.Server;
import vip.creatio.chathub.tool.Net;

import java.util.Arrays;


public class ChatCommand extends Command {
    public ChatCommand() {
        super("chat");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(new TextComponent("§8§l» §cUnknown command"));
        } else if ("reload".equals(args[0])) {
            Server.getInstance().stopServer();
            ConfigManager.getInstance().loadConfig();
            sender.sendMessage(new TextComponent("§8§l» §7Reload config success"));
            Server.getInstance().startServer();
            sender.sendMessage(new TextComponent("§8§l» §7Reload server success"));
        } else if ("list".equals(args[0])) {
            for (String line : OnlinePlayerListManager.getInstance().getStringServerPlayerList().split("\n")) {
                sender.sendMessage(new TextComponent(line));
            }
        } else if ("msg".equals(args[0])) {
            if (isProxiedPlayer(sender)) {
                if (args.length < 3) {
                    sender.sendMessage(new TextComponent("§8§l» §cUsage /chat <name> <msg>"));
                } else if (!OnlinePlayerListManager.getInstance().isOnline(args[1])) {
                    sender.sendMessage(new TextComponent("§8§l» §cPlayer " + args[1] + " is not online"));
                } else {
                    String message = String.join(" ", Arrays.asList(args).subList(2, args.length));
                    sender.sendMessage(new TextComponent(ConfigManager.getInstance().getMsgSenderMessage(args[1], message)));

                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                    if (target != null) {
                        target.sendMessage(new TextComponent(ConfigManager.getInstance().getMsgTargetMessage(sender.getName(), message)));
                    } else {
                        Net.broadcastMsg(sender.getName(), message, args[1]);
                    }
                }
            }
        } else if ("qq".equals(args[0])) {
            if (isProxiedPlayer(sender)) {
                if (args.length < 2) {
                    sender.sendMessage(new TextComponent("§8§l» §cUsage /qq <msg>"));
                } else if (!ConfigManager.getInstance().isQqEnable()) {
                    sender.sendMessage(new TextComponent("§8§l» §cQQ is not enabled"));
                } else {
                    ProxiedPlayer player = (ProxiedPlayer) sender;
                    String playerName = player.getName();
                    String serverName = player.getServer().getInfo().getName();
                    String rawMessage = String.join(" ", Arrays.asList(args).subList(1, args.length));
                    String message = ConfigManager.getInstance().getChatMessage(playerName, serverName, rawMessage);
                    message = message.replaceAll("[&|§].", "");
                    Bot.getInstance().sendGroupMessageAll(ConfigManager.getInstance().getQqGroupId(), message);
                }
            }
        }
    }

    public boolean isProxiedPlayer(CommandSender sender) {
        if (sender instanceof ProxiedPlayer) {
            return true;
        } else {
            sender.sendMessage(new TextComponent("§cYou cannot use this command"));
            return false;
        }
    }
}
