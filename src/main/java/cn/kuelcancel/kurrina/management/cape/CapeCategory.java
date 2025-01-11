package cn.kuelcancel.kurrina.management.cape;

import cn.kuelcancel.kurrina.utils.animation.ColorAnimation;
import cn.kuelcancel.kurrina.utils.animation.simple.SimpleAnimation;

public enum CapeCategory {
	ALL("All"), KURRINA("Kurrina"), MINECON("Minecon"), FLAG("Flag"), SPECIAL("Special");
	
	private String name;
	private SimpleAnimation backgroundAnimation = new SimpleAnimation();
	private ColorAnimation textColorAnimation = new ColorAnimation();
	
	private CapeCategory(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public SimpleAnimation getBackgroundAnimation() {
		return backgroundAnimation;
	}

	public ColorAnimation getTextColorAnimation() {
		return textColorAnimation;
	}
}
