package cn.kuelcancel.kurrina.management.mods.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.management.color.AccentColor;
import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventRender2D;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.HUDMod;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import cn.kuelcancel.kurrina.management.mods.settings.impl.BooleanSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.ComboSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.combo.Option;
import cn.kuelcancel.kurrina.management.nanovg.NanoVGManager;
import cn.kuelcancel.kurrina.management.nanovg.font.Fonts;

public class ArrayListMod extends HUDMod {

	private BooleanSetting backgroundSetting = new BooleanSetting(TranslateText.BACKGROUND, this, true);
	
	private BooleanSetting hudSetting = new BooleanSetting(TranslateText.HUD, this, false);
	private BooleanSetting renderSetting = new BooleanSetting(TranslateText.RENDER, this, false);
	private BooleanSetting playerSetting = new BooleanSetting(TranslateText.PLAYER, this, false);
	private BooleanSetting otherSetting = new BooleanSetting(TranslateText.OTHER, this, false);
	
	private ComboSetting modeSetting = new ComboSetting(TranslateText.MODE, this, TranslateText.RIGHT, new ArrayList<Option>(Arrays.asList(
			new Option(TranslateText.RIGHT), new Option(TranslateText.LEFT))));
	
	
	public ArrayListMod() {
		super(TranslateText.ARRAY_LIST, TranslateText.ARRAY_LIST_DESCRIPTION);
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		
		NanoVGManager nvg = Kurrina.getInstance().getNanoVGManager();
		
		nvg.setupAndDraw(() -> drawNanoVG());
	}
	
	private void drawNanoVG() {
		
		Kurrina instance = Kurrina.getInstance();
		AccentColor currentColor = instance.getColorManager().getCurrentColor();
		
		ArrayList<Mod> enabledMods = new ArrayList<Mod>();
		int maxWidth = 0;
		
		for(Mod m : instance.getModManager().getMods()) {
			
			if(!hudSetting.isToggled() && m.getCategory().equals(ModCategory.HUD)) {
				continue;
			}
			
			if(!renderSetting.isToggled() && m.getCategory().equals(ModCategory.RENDER)) {
				continue;
			}
			
			if(!playerSetting.isToggled() && m.getCategory().equals(ModCategory.PLAYER)) {
				continue;
			}
			
			if(!otherSetting.isToggled() && m.getCategory().equals(ModCategory.OTHER)) {
				continue;
			}
			
			if(m.isToggled() && !m.isHide()) {
				
				float nameWidth = this.getTextWidth(m.getName(), 8.5F, Fonts.REGULAR);
				
				enabledMods.add(m);
				
				if(maxWidth < nameWidth) {
					maxWidth = (int) nameWidth;
				}
			}
		}
		
		enabledMods.sort((m1, m2) -> (int) this.getTextWidth(m2.getName(), 8.5F, Fonts.REGULAR) - (int) this.getTextWidth(m1.getName(), 8.5F, Fonts.REGULAR));
		
		int y = 0;
		int colorIndex = 0;
		boolean isRight = modeSetting.getOption().getTranslate().equals(TranslateText.RIGHT);
		
		for(Mod m : enabledMods) {
			
			float nameWidth = this.getTextWidth(m.getName(), 8.5F, Fonts.REGULAR);
			
			if(backgroundSetting.isToggled()) {
				this.drawRect((isRight ? (maxWidth - nameWidth) : 0), y, nameWidth + 5, 12, new Color(0, 0, 0, 100));
			}
			
			this.drawText(m.getName(), 3 + (isRight ? (maxWidth - nameWidth) : 0), 
					y + 2.5F, 8.5F, Fonts.REGULAR, currentColor.getInterpolateColor(colorIndex));
			
			y += 12;
			colorIndex-=10;
		}
		
		this.setWidth(maxWidth + 4);
		this.setHeight(y);
	}
}
