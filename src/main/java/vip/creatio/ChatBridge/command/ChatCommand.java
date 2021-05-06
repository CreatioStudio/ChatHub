package vip.creatio.ChatBridge.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import vip.creatio.ChatBridge.config.ConfigManager;
import vip.creatio.ChatBridge.qq.Bot;
import vip.creatio.ChatBridge.tool.Message;


public class ChatCommand extends Command {
    public ChatCommand() {
        super("chat");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent("§cYou cannot use this command"));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (args[0].equals("qq")) {
            String playerName = player.getName();
            String serverName = player.getServer().getInfo().getName();
            String rawMessage = String.join(" ", args).replaceAll("qq\\s*", "");
            String message = ConfigManager.getInstance().getChatMessage(playerName, serverName, rawMessage);
            message = message.replaceAll("[&|§].", "");
            Bot.getInstance().sendGroupMessageAll(ConfigManager.getInstance().getQqGroupId(), message);
        } else if (args[0].equals("list")) {
            Message.sendPrivateMessage(player, ConfigManager.getInstance().getListMessage());
        }
    }
}
