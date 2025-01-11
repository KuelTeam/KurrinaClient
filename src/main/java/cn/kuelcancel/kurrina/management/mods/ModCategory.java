package cn.kuelcancel.kurrina.management.mods;

import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.utils.animation.ColorAnimation;
import cn.kuelcancel.kurrina.utils.animation.simple.SimpleAnimation;

public enum ModCategory {
	ALL(TranslateText.ALL), 
	PLAYER(TranslateText.PLAYER), 
	RENDER(TranslateText.RENDER),
	HUD(TranslateText.HUD),
	WORLD(TranslateText.WORLD),
	HYPIXEL(TranslateText.HYPIXEL),
	OTHER(TranslateText.OTHER);
	
	private TranslateText nameTranslate;
	private ColorAnimation textColorAnimation;
	private SimpleAnimation backgroundAnimation;
	
	private ModCategory(TranslateText nameTranslate) {
		this.nameTranslate = nameTranslate;
		this.backgroundAnimation = new SimpleAnimation();
		this.textColorAnimation = new ColorAnimation();
	}

	public String getName() {
		return nameTranslate.getText();
	}

	public SimpleAnimation getBackgroundAnimation() {
		return backgroundAnimation;
	}

	public ColorAnimation getTextColorAnimation() {
		return textColorAnimation;
	}
}
