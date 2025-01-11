package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventRender2D;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.SimpleHUDMod;
import cn.kuelcancel.kurrina.management.mods.settings.impl.BooleanSetting;
import cn.kuelcancel.kurrina.management.nanovg.font.Icon;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class WeatherDisplayMod extends SimpleHUDMod {

	private BooleanSetting iconSetting = new BooleanSetting(TranslateText.ICON, this, true);
	
	public WeatherDisplayMod() {
		super(TranslateText.WEATHER_DISPLAY, TranslateText.WEATHER_DISPLAY_DESCRIPTION);
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		this.draw();
	}
	
	@Override
	public String getText() {
		
		String biome = "";
		String prefix = "Weather: ";
		Chunk chunk = mc.theWorld.getChunkFromBlockCoords(new BlockPos(mc.thePlayer));
		biome = chunk.getBiome(new BlockPos(mc.thePlayer), this.mc.theWorld.getWorldChunkManager()).biomeName;
		
		if(mc.theWorld.isRaining()) {
			if(biome.contains("Extreme Hills") && mc.thePlayer.posY > 100) {
				return prefix + "Snowing";
			}else {
				return prefix + "Raining";
			}
		}
		
		if(mc.theWorld.isThundering()) {
			return prefix + "Thundering";
		}
		
		return prefix + "Cleaning";
	}
	
	@Override
	public String getIcon() {
		
		String biome = "";
		Chunk chunk = mc.theWorld.getChunkFromBlockCoords(new BlockPos(mc.thePlayer));
		biome = chunk.getBiome(new BlockPos(mc.thePlayer), this.mc.theWorld.getWorldChunkManager()).biomeName;
		
		String iconFont = Icon.SUN;
		
		if(mc.theWorld.isRaining()) {
			if(biome.contains("Extreme Hills") && mc.thePlayer.posY > 100) {
				iconFont = Icon.CLOUD_SNOW;
			}else {
				iconFont = Icon.CLOUD_RAIN;
			}
		}
		
		if(mc.theWorld.isThundering()) {
			iconFont = Icon.CLOUD_LIGHTING;
		}
		
		return iconSetting.isToggled() ? iconFont : null;
	}
}
