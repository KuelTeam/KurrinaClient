package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventRenderCrosshair;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import cn.kuelcancel.kurrina.management.mods.settings.impl.BooleanSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.NumberSetting;
import cn.kuelcancel.kurrina.utils.GlUtils;
import cn.kuelcancel.kurrina.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class CrosshairMod extends Mod {

	private BooleanSetting hideThirdPersonViewSetting = new BooleanSetting(TranslateText.HIDE_THIRD_PERSON_VIEW, this, false);
	private BooleanSetting customCrosshairSetting = new BooleanSetting(TranslateText.CUSTOM_CROSSHAIR, this, false);
	private NumberSetting typeSetting = new NumberSetting(TranslateText.TYPE, this, 1, 0, 5, true);
	private NumberSetting scaleSetting = new NumberSetting(TranslateText.SCALE, this, 1, 0.3, 2, false);
	
	public CrosshairMod() {
		super(TranslateText.CROSSHAIR, TranslateText.CROSSHAIR_DESCRIPTION, ModCategory.RENDER);
	}

	@EventTarget
	public void onRenderCrosshair(EventRenderCrosshair event) {
		
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        
        if((hideThirdPersonViewSetting.isToggled() && mc.gameSettings.thirdPersonView != 0)) {
        	event.setCancelled(true);
        }
        
        if(customCrosshairSetting.isToggled()) {
        	
        	event.setCancelled(true);
        	
        	if((!hideThirdPersonViewSetting.isToggled()) || (hideThirdPersonViewSetting.isToggled() && mc.gameSettings.thirdPersonView == 0)) {
        		
        		int index = typeSetting.getValueInt();
        		
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(775, 769, 1, 0);
                GlStateManager.enableAlpha();
                GlStateManager.enableDepth();
            	mc.getTextureManager().bindTexture(new ResourceLocation("kurrina/crosshair.png"));
            	GlUtils.startScale(sr.getScaledWidth() / 2 - 7, sr.getScaledHeight() / 2 - 7, 16, 16, scaleSetting.getValueFloat());
            	RenderUtils.drawTexturedModalRect(sr.getScaledWidth() / 2 - 7, sr.getScaledHeight() / 2 - 7, index * 16, 0, 16, 16);
            	GlUtils.stopScale();
        	}
        }
	}
}
