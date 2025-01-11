package cn.kuelcancel.kurrina.management.command.impl;

import cn.kuelcancel.kurrina.logger.KurrinaLogger;
import cn.kuelcancel.kurrina.management.command.Command;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.impl.ChatTranslateMod;
import cn.kuelcancel.kurrina.management.mods.settings.impl.ComboSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.combo.Option;
import cn.kuelcancel.kurrina.utils.Multithreading;
import cn.kuelcancel.kurrina.utils.translate.Translator;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class TranslateCommand extends Command {

	private String to = Translator.ENGLISH;

	public TranslateCommand() {
		super("translate");
	}

	@Override
	public void onCommand(String message) {

		ComboSetting setting = ChatTranslateMod.getInstance().getLanguageSetting();
		Option option = setting.getOption();

		if(option.getTranslate().equals(TranslateText.ENGLISH)) {
			to = Translator.ENGLISH;
		} else if(option.getTranslate().equals(TranslateText.CHINESE)) {
			to = Translator.CHINESE_SIMPLIFIED;
		} else if(option.getTranslate().equals(TranslateText.POLISH)) {
			to = Translator.POLISH;
		}

		String text = message;

		Multithreading.runAsync(() -> {
			try {
				mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "[Translate] " + EnumChatFormatting.WHITE + Translator.translate(text, Translator.AUTO_DETECT, to)));
			} catch (Exception e) {
				KurrinaLogger.error("Failed to translate", e);
			}
		});
	}
}
