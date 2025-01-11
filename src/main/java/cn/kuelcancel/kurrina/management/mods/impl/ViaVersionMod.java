package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import cn.kuelcancel.kurrina.utils.Multithreading;
import cn.kuelcancel.kurrina.viaversion.ViaLoadingBase;
import cn.kuelcancel.kurrina.viaversion.ViaKurrina;
import cn.kuelcancel.kurrina.viaversion.protocolinfo.ProtocolInfo;

public class ViaVersionMod extends Mod {

	private static ViaVersionMod instance;
	
	private boolean loaded;
	
	public ViaVersionMod() {
		super(TranslateText.VIA_VERSION, TranslateText.VIA_VERSION_DESCRIPTION, ModCategory.OTHER);
		
		instance = this;
		loaded = false;
	}

	@Override
	public void onEnable() {
		super.onEnable();
		
		if(!loaded) {
			loaded = true;
			Multithreading.runAsync(() -> {
				ViaKurrina.create();
				ViaKurrina.getInstance().initAsyncSlider();
			});
		}
	}
	
	@Override
	public void onDisable() {
		
		super.onDisable();
		
		if(loaded) {
			ViaKurrina.getInstance().getAsyncVersionSlider().setVersion(ProtocolInfo.R1_8.getProtocolVersion().getVersion());
			ViaLoadingBase.getInstance().reload(ProtocolInfo.R1_8.getProtocolVersion());
		}
	}

	public static ViaVersionMod getInstance() {
		return instance;
	}

	public boolean isLoaded() {
		return loaded;
	}
}
