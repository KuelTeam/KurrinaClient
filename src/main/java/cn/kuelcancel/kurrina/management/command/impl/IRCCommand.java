package cn.kuelcancel.kurrina.management.command.impl;

import cn.kuelcancel.kurrina.irc.ChatClient;
import cn.kuelcancel.kurrina.irc.ChatClientManager;
import cn.kuelcancel.kurrina.irc.ChatClientSender;
import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.logger.KurrinaLogger;
import cn.kuelcancel.kurrina.management.command.Command;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.impl.IRCMod;
import cn.kuelcancel.kurrina.utils.Multithreading;
import net.minecraft.util.ChatComponentText;

public class IRCCommand extends Command {

    private ChatClient chatClient;
    IRCMod ircMod = (IRCMod) Kurrina.getInstance().getModManager().getModByTranslateKey(TranslateText.IRC.getKey());


    public IRCCommand() {
        super("irc");
    }

    @Override
    public void onCommand(String message) {
        String text = message;

        Multithreading.runAsync(() -> {
            if (ircMod.isToggled()) {
                try {
                    chatClient = ChatClientManager.getChatClient();
                    ChatClientSender sender = chatClient.getSender();
                    if (sender != null) {
                        sender.sendMessage(text);
                    } else {
                        KurrinaLogger.error("Sender is not initialized.");
                    }
                } catch (Exception e) {
                    KurrinaLogger.error("Failed to send message to IRC.", e);
                }
            } else {
                mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§a[KurrinaChat] 未开启IRC功能，请在功能菜单.其他中打开!"));
            }
        });
    }
}