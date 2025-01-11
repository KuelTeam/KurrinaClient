package cn.kuelcancel.kurrina.management.mods.impl;

import org.lwjgl.input.Keyboard;

import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventKey;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import cn.kuelcancel.kurrina.management.mods.settings.impl.KeybindSetting;
import cn.kuelcancel.kurrina.utils.PlayerUtils;
import net.minecraft.init.Items;

public class QuickSwitchMod extends Mod {

	private KeybindSetting swordSetting = new KeybindSetting(TranslateText.SWORD, this, Keyboard.KEY_NONE);
	private KeybindSetting blockSetting = new KeybindSetting(TranslateText.BLOCK, this, Keyboard.KEY_NONE);
	private KeybindSetting rodSetting = new KeybindSetting(TranslateText.ROD, this, Keyboard.KEY_NONE);
	private KeybindSetting axeSetting = new KeybindSetting(TranslateText.AXE, this, Keyboard.KEY_NONE);
	private KeybindSetting pickaxeSetting = new KeybindSetting(TranslateText.PICKAXE, this, Keyboard.KEY_NONE);
	private KeybindSetting bowSetting = new KeybindSetting(TranslateText.BOW, this, Keyboard.KEY_NONE);
	
	public QuickSwitchMod() {
		super(TranslateText.QUICK_SWITCH, TranslateText.QUICK_SWITCH_DESCRIPTION, ModCategory.PLAYER);
	}
	
	@EventTarget
	public void onKey(EventKey event) {
		
		if(event.getKeyCode() == swordSetting.getKeyCode()) {
			setCurrentItem(PlayerUtils.getBestSword(mc.thePlayer));
		}
		
		if(event.getKeyCode() == blockSetting.getKeyCode()) {
			setCurrentItem(PlayerUtils.getBestBlock(mc.thePlayer));
		}
		
		if(event.getKeyCode() == rodSetting.getKeyCode()) {
			setCurrentItem(PlayerUtils.getItemSlot(Items.fishing_rod));
		}
		
		if(event.getKeyCode() == axeSetting.getKeyCode()) {
			setCurrentItem(PlayerUtils.getBestAxe(mc.thePlayer));
		}
		
		if(event.getKeyCode() == pickaxeSetting.getKeyCode()) {
			setCurrentItem(PlayerUtils.getBestPickaxe(mc.thePlayer));
		}
		
		if(event.getKeyCode() == bowSetting.getKeyCode()) {
			setCurrentItem(PlayerUtils.getBestBow(mc.thePlayer));
		}
	}
	
	private void setCurrentItem(int slot) {
		mc.thePlayer.inventory.currentItem = slot;
	}
}
