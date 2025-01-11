package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventReceivePacket;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import cn.kuelcancel.kurrina.management.mods.settings.impl.BooleanSetting;
import net.minecraft.network.play.server.S02PacketChat;

public class MinemenMod extends Mod {

	private BooleanSetting autoPlaySetting = new BooleanSetting(TranslateText.AUTO_PLAY, this, false);
	
	public MinemenMod() {
		super(TranslateText.MINEMEN, TranslateText.MINEMEN_DESCRIPTION, ModCategory.OTHER);
	}

	@EventTarget
	public void onReceivePacket(EventReceivePacket event) {
		
		if(autoPlaySetting.isToggled() && event.getPacket() instanceof S02PacketChat) {
			
			S02PacketChat chatPacket = (S02PacketChat) event.getPacket();
			String raw = chatPacket.getChatComponent().toString();
			
			if (raw.contains("clickEvent=ClickEvent{action=RUN_COMMAND, value='/requeue")) {
				mc.thePlayer.sendChatMessage("/requeue");
			}
		}
	}
}
