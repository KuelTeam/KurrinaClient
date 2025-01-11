package cn.kuelcancel.kurrina.management.mods.impl;

import org.lwjgl.input.Keyboard;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventRender2D;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.HUDMod;
import cn.kuelcancel.kurrina.management.mods.settings.impl.BooleanSetting;
import cn.kuelcancel.kurrina.management.nanovg.NanoVGManager;
import cn.kuelcancel.kurrina.management.nanovg.font.Fonts;
import cn.kuelcancel.kurrina.utils.animation.simple.SimpleAnimation;

public class KeystrokesMod extends HUDMod {

	private BooleanSetting spaceSetting = new BooleanSetting(TranslateText.SPACE, this, true);
	private BooleanSetting unmarkedSetting = new BooleanSetting(TranslateText.UNMARKED, this, false);
	
	private SimpleAnimation[] animations = new SimpleAnimation[5];
	
	public KeystrokesMod() {
		super(TranslateText.KEYSTROKES, TranslateText.KEYSTROKES_DESCRIPTION);
		
	    for (int i = 0; i < 5; i++) {
	        animations[i] = new SimpleAnimation();
	    }
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		
		NanoVGManager nvg = Kurrina.getInstance().getNanoVGManager();
		
		nvg.setupAndDraw(() -> drawNanoVG());
	}
	
	private void drawNanoVG() {
		
		boolean openGui = mc.currentScreen != null;
		
		animations[0].setAnimation(!openGui && Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()) ? 1.0F : 0.0F, 16);
		animations[1].setAnimation(!openGui && Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()) ? 1.0F : 0.0F, 16);
		animations[2].setAnimation(!openGui && Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()) ? 1.0F : 0.0F, 16);
		animations[3].setAnimation(!openGui && Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()) ? 1.0F : 0.0F, 16);
		animations[4].setAnimation(!openGui && Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode()) ? 1.0F : 0.0F, 16);
		
		// W
		this.drawBackground(32, 0, 28, 28);
		
		// A
		this.drawBackground(0, 32, 28, 28);
		
		// S
		this.drawBackground(32, 32, 28, 28);
		
		// D
		this.drawBackground(64, 32, 28, 28);
		
		// W
		this.save();
		this.scale(32, 0, 28, 28, animations[0].getValue());
		this.drawRoundedRect(32, 0, 28, 28, 6, this.getFontColor((int) (120 * animations[0].getValue())));
		this.restore();
		
		// A
		this.save();
		this.scale(0, 32, 28, 28, animations[1].getValue());
		this.drawRoundedRect(0, 32, 28, 28, 6, this.getFontColor((int) (120 * animations[1].getValue())));
		this.restore();
		
		// S
		this.save();
		this.scale(32, 32, 28, 28, animations[2].getValue());
		this.drawRoundedRect(32, 32, 28, 28, 6, this.getFontColor((int) (120 * animations[2].getValue())));
		this.restore();
		
		// D
		this.save();
		this.scale(64, 32, 28, 28, animations[3].getValue());
		this.drawRoundedRect(64, 32, 28, 28, 6, this.getFontColor((int) (120 * animations[3].getValue())));
		this.restore();
		
		if(!unmarkedSetting.isToggled()) {
			this.drawCenteredText(Keyboard.getKeyName(mc.gameSettings.keyBindForward.getKeyCode()), 32 + (28 / 2), (28 / 2) - 4, 12, Fonts.REGULAR);
			this.drawCenteredText(Keyboard.getKeyName(mc.gameSettings.keyBindLeft.getKeyCode()), 0 + (28 / 2), 32 + (28 / 2) - 4, 12, Fonts.REGULAR);
			this.drawCenteredText(Keyboard.getKeyName(mc.gameSettings.keyBindBack.getKeyCode()), 32 + (28 / 2), 32 + (28 / 2) - 4, 12, Fonts.REGULAR);
			this.drawCenteredText(Keyboard.getKeyName(mc.gameSettings.keyBindRight.getKeyCode()), 64 + (28 / 2), 32 + (28 / 2) - 4, 12, Fonts.REGULAR);
		}
		
		if(spaceSetting.isToggled()) {
			
			this.drawBackground(0, 64, (28 * 3) + 8, 22);
			
			this.save();
			this.scale(0, 64, (28 * 3) + 8, 22, animations[4].getValue());
			this.drawRoundedRect(0, 64, (28 * 3) + 8, 22, 6, this.getFontColor((int) (120 * animations[4].getValue())));
			this.restore();
			
			if(!unmarkedSetting.isToggled()) {
				this.drawRoundedRect(10, 74F, (26 * 3) - 6, 2, 1);
			}
		}
		
		this.setWidth(28 * 3 + 8);
		this.setHeight(spaceSetting.isToggled() ? 64 + 22 : 32 + 28);
	}
}
