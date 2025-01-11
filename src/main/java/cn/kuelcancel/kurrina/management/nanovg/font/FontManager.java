package cn.kuelcancel.kurrina.management.nanovg.font;

import java.nio.ByteBuffer;

import org.lwjgl.nanovg.NanoVG;

import cn.kuelcancel.kurrina.logger.KurrinaLogger;
import cn.kuelcancel.kurrina.utils.IOUtils;

public class FontManager {

	public void init(long nvg) {
		for(Font f : Fonts.getFonts()) {
			loadFont(nvg, f);
		}
	}
	
	private void loadFont(long nvg, Font font) {
		
		if(font.isLoaded()) {
			return;
		}
		
		int loaded = -1;
		
		try {
			ByteBuffer buffer = IOUtils.resourceToByteBuffer(font.getResourceLocation());
			loaded = NanoVG.nvgCreateFontMem(nvg, font.getName(), buffer, false);
			font.setBuffer(buffer);
		} catch (Exception e) {
			KurrinaLogger.error("Failed to load font", e);
		}
		
		if(loaded == -1) {
			throw new RuntimeException("Failed to init font " + font.getName());
		}else {
			font.setLoaded(true);
		}
	}
}
