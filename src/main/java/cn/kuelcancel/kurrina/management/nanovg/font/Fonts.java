package cn.kuelcancel.kurrina.management.nanovg.font;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.util.ResourceLocation;

public class Fonts {
	
	private static final String PATH = "kurrina/fonts/";
	
	public static final Font REGULAR = new Font("regular", new ResourceLocation(PATH + "Font.ttf"));
	public static final Font MEDIUM = new Font("medium", new ResourceLocation(PATH + "Font.ttf"));
	public static final Font DEMIBOLD = new Font("demi-bold", new ResourceLocation(PATH + "Font.ttf"));
	public static final Font ICON = new Font("icon", new ResourceLocation(PATH + "Icon.ttf"));

	public static ArrayList<Font> getFonts() {
		return new ArrayList<Font>(Arrays.asList(MEDIUM, DEMIBOLD, REGULAR, ICON));
	}
}
