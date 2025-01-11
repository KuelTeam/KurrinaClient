package cn.kuelcancel.kurrina.management.mods.impl;

import java.util.Collection;

import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventFovUpdate;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import cn.kuelcancel.kurrina.management.mods.settings.impl.NumberSetting;
import cn.kuelcancel.kurrina.utils.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class FovModifierMod extends Mod {

	private NumberSetting sprintingSetting = new NumberSetting(TranslateText.SPRINTING, this, 1, -5, 5, false);
	private NumberSetting bowSetting = new NumberSetting(TranslateText.BOW, this, 1, -5, 5, false);
	private NumberSetting speedSetting = new NumberSetting(TranslateText.SPEED, this, 1, -5, 5, false);
	private NumberSetting slownessSetting = new NumberSetting(TranslateText.SLOWNESS, this, 1, -5, 5, false);
	
	public FovModifierMod() {
		super(TranslateText.FOV_MODIFIER, TranslateText.FOV_MODIFIER_DESCRIPTION, ModCategory.PLAYER);
	}

	@EventTarget
	public void onFovUpdate(EventFovUpdate event) {
		
		float base = 1.0F;
		EntityPlayer entity = event.getEntity();
		ItemStack item = entity.getItemInUse();
		int useDuration = entity.getItemInUseDuration();
		
		float sprintingFov = sprintingSetting.getValueFloat();
		float bowFov = bowSetting.getValueFloat();
		float speedFov = speedSetting.getValueFloat();
		float slownessFov = slownessSetting.getValueFloat();
		
		if(entity.isSprinting()) {
			base += 0.15000000596046448  * sprintingFov;
		}
		
		if(item != null && item.getItem() == Items.bow) {
			int duration = (int) Math.min(useDuration, 20.0F);
			float modifier = PlayerUtils.MODIFIER_BY_TICK.get(duration);
			base-= modifier * bowFov;
		}
		
        Collection<PotionEffect> effects = entity.getActivePotionEffects();
        if (!effects.isEmpty()) {
            for (PotionEffect effect : effects) {
                int potionID = effect.getPotionID();
                if (potionID == 1) {
                    base += 0.1F * (effect.getAmplifier() + 1) * speedFov;
                }

                if (potionID == 2) {
                    base += -0.075F * (effect.getAmplifier() + 1) * slownessFov;
                }
            }
        }
        
        event.setFov(base);
	}
}
