package cn.kuelcancel.kurrina.management.mods.impl;

import com.mojang.util.UUIDTypeAdapter;

import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventLocationSkin;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import net.minecraft.util.ResourceLocation;

public class SkinProtectMod extends Mod {

	public SkinProtectMod() {
		super(TranslateText.SKIN_PROTECT, TranslateText.SKIN_PROTECT_DESCRIPTION, ModCategory.PLAYER);
	}
	
	@EventTarget
	public void onLocationSkin(EventLocationSkin event) {
		
		String uuid = UUIDTypeAdapter.fromUUID(event.getPlayerInfo().getGameProfile().getId());
		String pUuid = UUIDTypeAdapter.fromUUID(mc.thePlayer.getGameProfile().getId());
		
		if(uuid.equals(pUuid)) {
			event.setCancelled(true);
			event.setSkin(new ResourceLocation("textures/entity/steve.png"));
		}
	}
}
