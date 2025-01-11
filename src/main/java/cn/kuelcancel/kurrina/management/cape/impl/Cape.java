package cn.kuelcancel.kurrina.management.cape.impl;

import cn.kuelcancel.kurrina.management.cape.CapeCategory;
import cn.kuelcancel.kurrina.utils.animation.simple.SimpleAnimation;
import net.minecraft.util.ResourceLocation;

public class Cape {
	
	private String name;
	private ResourceLocation cape;
	private boolean premium;
	private CapeCategory category;
	
	private SimpleAnimation animation = new SimpleAnimation();
	
	public Cape(String name, ResourceLocation cape, boolean premium, CapeCategory category) {
		this.name = name;
		this.premium = premium;
		this.category = category;
		this.cape = cape;
	}

	public String getName() {
		return name;
	}

	public boolean isPremium() {
		return premium;
	}

	public CapeCategory getCategory() {
		return category;
	}

	public SimpleAnimation getAnimation() {
		return animation;
	}

	public ResourceLocation getCape() {
		return cape;
	}
}
