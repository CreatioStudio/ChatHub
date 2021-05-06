package vip.creatio.ChatBridge.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import vip.creatio.ChatBridge.qq.QQManager;


public class ChatCommand extends Command {
    public ChatCommand() {
        super("chat");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args[0].equals("qq")) {
            String message = String.join(" ", args).replaceAll("qq\\s*", "");
            QQManager.getInstance().onChat(sender.getName(), ((ProxiedPlayer) sender).getServer().getInfo().getName(), message);
        }
    }
}
