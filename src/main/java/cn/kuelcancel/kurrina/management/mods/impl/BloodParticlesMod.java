package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventAttackEntity;
import cn.kuelcancel.kurrina.management.event.impl.EventUpdate;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import cn.kuelcancel.kurrina.management.mods.settings.impl.BooleanSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.NumberSetting;
import net.minecraft.block.Block;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;

public class BloodParticlesMod extends Mod {

	private EntityLivingBase target;
	
	private NumberSetting amountSetting = new NumberSetting(TranslateText.AMOUNT, this, 2, 1, 10, true);
	private BooleanSetting soundSetting = new BooleanSetting(TranslateText.SOUND, this, true);
	
	public BloodParticlesMod() {
		super(TranslateText.BLOOD_PARTICLES, TranslateText.BLOOD_PARTICLES_DESCRIPTION, ModCategory.RENDER);
	}

	@EventTarget
	public void onAttackEntity(EventAttackEntity event) {
		
		if(!(event.getEntity() instanceof EntityLivingBase)) {
			return;
		}
		
    	if(target != null) {
    		 for (int i = 0; i < amountSetting.getValueInt(); i++) {
                mc.theWorld.spawnParticle(EnumParticleTypes.BLOCK_CRACK, target.posX, target.posY + target.height - 0.75, target.posZ, 0, 0, 0, Block.getStateId(Blocks.redstone_block.getDefaultState()));
    		 }
  		 }
  	
		 if(soundSetting.isToggled() && target != null) {
 			 mc.getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation("dig.stone"), 4.0F, 1.2F, ((float) target.posX), ((float) target.posY), ((float) target.posZ)));
 		 }
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if(mc.objectMouseOver != null & mc.objectMouseOver.entityHit != null) {
			if(mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
				target = (EntityLivingBase) mc.objectMouseOver.entityHit;
			}
		}
	}
}
