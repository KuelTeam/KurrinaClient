package cn.kuelcancel.kurrina.management.cape.impl;

import java.io.File;

import cn.kuelcancel.kurrina.management.cape.CapeCategory;
import net.minecraft.util.ResourceLocation;

public class CustomCape extends Cape {

	private File sample;
	
	public CustomCape(String name, File sample, ResourceLocation cape, boolean premium, CapeCategory category) {
		super(name, cape, premium, category);
		this.sample = sample;
	}

	public File getSample() {
		return sample;
	}
}
