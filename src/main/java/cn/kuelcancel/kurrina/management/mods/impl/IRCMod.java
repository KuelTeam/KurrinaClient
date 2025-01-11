package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.irc.ChatClient;
import cn.kuelcancel.kurrina.irc.ChatClientManager;
import cn.kuelcancel.kurrina.logger.KurrinaLogger;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;

public class IRCMod extends Mod {

    public IRCMod() {
        super(TranslateText.IRC, TranslateText.IRC_DESCRIPTION, ModCategory.OTHER);
    }

    public ChatClient chatClient = null;
    @Override
    public void onEnable() {
        super.onEnable();
        try {
            chatClient = ChatClientManager.getChatClient();
        } catch (Exception e) {
            KurrinaLogger.error("Failed to start IRC bot", e);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (chatClient != null) {
            chatClient.stopIRC();
        }
    }
}
