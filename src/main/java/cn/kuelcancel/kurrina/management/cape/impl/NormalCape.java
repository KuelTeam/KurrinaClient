package cn.kuelcancel.kurrina.management.cape.impl;

import cn.kuelcancel.kurrina.management.cape.CapeCategory;
import net.minecraft.util.ResourceLocation;

public class NormalCape extends Cape {

	private ResourceLocation sample;
	
	public NormalCape(String name, ResourceLocation sample, ResourceLocation cape, boolean premium, CapeCategory category) {
		super(name, cape, premium, category);
		this.sample = sample;
	}

	public ResourceLocation getSample() {
		return sample;
	}
}
