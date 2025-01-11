package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventRenderItemInFirstPerson;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import cn.kuelcancel.kurrina.management.mods.settings.impl.NumberSetting;
import net.minecraft.client.renderer.GlStateManager;

public class CustomHeldItemsMod extends Mod {

	private NumberSetting xSetting = new NumberSetting(TranslateText.X, this, 0.75, -1, 1, false);
	private NumberSetting ySetting = new NumberSetting(TranslateText.Y, this, -0.15, -1, 1, false);
	private NumberSetting zSetting = new NumberSetting(TranslateText.Z, this, -1, -1, 1, false);
	
	private NumberSetting xScaleSetting = new NumberSetting(TranslateText.X_SCALE, this, 1, 0, 1, false);
	private NumberSetting yScaleSetting = new NumberSetting(TranslateText.Y_SCALE, this, 1, 0, 1, false);
	private NumberSetting zScaleSetting = new NumberSetting(TranslateText.Z_SCALE, this, 1, 0, 1, false);
	
	public CustomHeldItemsMod() {
		super(TranslateText.CUSTOM_HELD_ITEMS, TranslateText.CUSTOM_HELD_ITEMS_DESCRIPTION, ModCategory.RENDER);
	}

	@EventTarget
	public void onRenderItemInFirstPerson(EventRenderItemInFirstPerson event) {
		GlStateManager.translate(xSetting.getValueFloat(), ySetting.getValueFloat(), zSetting.getValueFloat());
		GlStateManager.scale(xScaleSetting.getValueFloat(), yScaleSetting.getValueFloat(), zScaleSetting.getValueFloat());
	}
}
