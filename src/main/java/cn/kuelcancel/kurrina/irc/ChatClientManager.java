package cn.kuelcancel.kurrina.irc;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.logger.KurrinaLogger;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.impl.IRCMod;

public class ChatClientManager {

    private static ChatClient chatClient;
    private static IRCMod ircMod = (IRCMod) Kurrina.getInstance().getModManager().getModByTranslateKey(TranslateText.IRC.getKey());

    public static void refreshChatClient() {
        if (ircMod.isToggled()) {
            chatClient = new ChatClient();
            ChatClient.retryCount = 3;
            KurrinaLogger.info("Restarting chat client...");
            chatClient.start();
        }
    }

    public static boolean serverAvailable() {
        return ChatClient.isServerAvailable;
    }

    public static ChatClient getChatClient() {
        if(chatClient == null || !chatClient.isAlive()) refreshChatClient();
        return chatClient;
    }
}
