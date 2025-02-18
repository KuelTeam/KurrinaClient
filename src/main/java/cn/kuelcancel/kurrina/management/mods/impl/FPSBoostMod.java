package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventRendererLivingEntity;
import cn.kuelcancel.kurrina.management.event.impl.EventUpdate;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import cn.kuelcancel.kurrina.management.mods.settings.impl.BooleanSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.NumberSetting;
import cn.kuelcancel.kurrina.utils.ServerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

public class FPSBoostMod extends Mod {

	private static FPSBoostMod instance;

	private BooleanSetting chunkDelaySetting = new BooleanSetting(TranslateText.CHUNK_DELAY, this, false);
	private NumberSetting delaySetting = new NumberSetting(TranslateText.DELAY, this, 5, 1, 12, true);
	private BooleanSetting hideArmorStandSetting = new BooleanSetting(TranslateText.HIDE_ARMOR_STAND, this, false);
	private BooleanSetting removeBotSetting = new BooleanSetting(TranslateText.REMOVE_BOT, this, false);
	
	public FPSBoostMod() {
		super(TranslateText.FPS_BOOST, TranslateText.FPS_BOOST_DESCRIPTION, ModCategory.OTHER);
		
		instance = this;
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		
		if(removeBotSetting.isToggled()) {
			for(Entity entity : mc.theWorld.loadedEntityList) {
				if(entity.isInvisible() && !ServerUtils.isInTablist(entity)) {
					mc.theWorld.removeEntity(entity);
				}
			}
		}
	}
	
    @EventTarget
    public void onRendererLivingEntity(EventRendererLivingEntity event) {
    	
    	if(hideArmorStandSetting.isToggled()) {
    		
        	Entity entity = event.getEntity();
        	
        	if(entity instanceof EntityArmorStand) {
        		event.setCancelled(true);
        	}
    	}
    }
    
	public static FPSBoostMod getInstance() {
		return instance;
	}

	public BooleanSetting getChunkDelaySetting() {
		return chunkDelaySetting;
	}

	public NumberSetting getDelaySetting() {
		return delaySetting;
	}
}
