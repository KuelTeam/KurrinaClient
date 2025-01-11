package cn.kuelcancel.kurrina.management.mods.impl;

import java.awt.Color;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.management.color.AccentColor;
import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventRender3D;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import cn.kuelcancel.kurrina.management.mods.settings.impl.BooleanSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.ColorSetting;
import cn.kuelcancel.kurrina.utils.ColorUtils;
import cn.kuelcancel.kurrina.utils.Render3DUtils;
import cn.kuelcancel.kurrina.utils.TargetUtils;
import net.minecraft.client.renderer.GlStateManager;

public class TargetIndicatorMod extends Mod {

	private BooleanSetting customColorSetting = new BooleanSetting(TranslateText.CUSTOM_COLOR, this, false);
	private ColorSetting colorSetting = new ColorSetting(TranslateText.COLOR, this, Color.RED, false);
    
	public TargetIndicatorMod() {
		super(TranslateText.TARGET_INDICATOR, TranslateText.TARGET_INDICATOR_DESCRIPTION, ModCategory.RENDER);
	}

	@EventTarget
	public void onRender3D(EventRender3D event) {
		
		AccentColor currentColor = Kurrina.getInstance().getColorManager().getCurrentColor();
		
		if(TargetUtils.getTarget() != null && !TargetUtils.getTarget().equals(mc.thePlayer)) {
			Render3DUtils.drawTargetIndicator(TargetUtils.getTarget(), 0.67, customColorSetting.isToggled() ? ColorUtils.applyAlpha(colorSetting.getColor(), 255) : currentColor.getInterpolateColor());
			GlStateManager.enableBlend();
		}
	}
}
