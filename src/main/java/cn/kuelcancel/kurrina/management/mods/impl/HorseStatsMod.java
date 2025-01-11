package cn.kuelcancel.kurrina.management.mods.impl;

import java.text.DecimalFormat;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventRender2D;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.HUDMod;
import cn.kuelcancel.kurrina.management.nanovg.NanoVGManager;
import cn.kuelcancel.kurrina.management.nanovg.font.Fonts;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityHorse;

public class HorseStatsMod extends HUDMod {

	private DecimalFormat df = new DecimalFormat("0.0");
	
	public HorseStatsMod() {
		super(TranslateText.HORSE_STATS, TranslateText.HORSE_STATS_DESCRIPTION);
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		
		NanoVGManager nvg = Kurrina.getInstance().getNanoVGManager();
		
		nvg.setupAndDraw(() -> drawNanoVG());
	}
	
	private void drawNanoVG() {
		
		String speed = "Speed: 0.0 b/s";
		String jump = "Jump: 0.0 Blocks";
		
		if(mc.objectMouseOver.entityHit instanceof EntityHorse) {
			
			EntityHorse horse = (EntityHorse) mc.objectMouseOver.entityHit;
			
			if(!mc.thePlayer.isRidingHorse()) {
				speed = "Speed: " + this.getHorseSpeedRounded(horse.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()) + " b/s";
				jump = "Jump: " + df.format(horse.getHorseJumpStrength() * 5.5) + " Blocks";
			}
		}
		
		this.drawBackground(95, 28);
		
		this.drawText(speed, 5.5F, 5.5F, 9, Fonts.REGULAR);
		this.drawText(jump, 5.5F, 15.5F, 9, Fonts.REGULAR);
		
		this.setWidth(95);
		this.setHeight(28);
	}
	
    private String getHorseSpeedRounded(double baseSpeed) {
    	
        final float factor = 43.1703703704f;
        
        float speed = (float) (baseSpeed * factor);
        
        return df.format(speed);
    }
}
