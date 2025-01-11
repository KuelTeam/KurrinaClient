package cn.kuelcancel.kurrina.management.mods.impl;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import cn.kuelcancel.kurrina.injection.interfaces.IMixinMinecraft;
import cn.kuelcancel.kurrina.logger.KurrinaLogger;
import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventPreRenderTick;
import cn.kuelcancel.kurrina.management.event.impl.EventToggleFullscreen;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;

public class BorderlessFullscreenMod extends Mod {

	private int prevX, prevY, prevWidth, prevHeight;
	
	private long fullscreenTime = -1;
	
	public BorderlessFullscreenMod() {
		super(TranslateText.BORDERLESS_FULSCREEN, TranslateText.BORDERLESS_FULLSCREEN_DESCRIPTION, ModCategory.OTHER);
	}

	@EventTarget
	public void onRenderTick(EventPreRenderTick event) {
		if(fullscreenTime != -1 && System.currentTimeMillis() - fullscreenTime >= 100) {
			fullscreenTime = -1;
			
			if(mc.inGameHasFocus) {
				mc.mouseHelper.grabMouseCursor();
			}
		}
	}
	
	@EventTarget
	public void onFullscreenToggle(EventToggleFullscreen event) {
		event.setApplyState(false);
		setBorderlessFullscreen(event.getState());
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		if(mc.isFullScreen()) {
			setBorderlessFullscreen(true);
		}
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		
		if(mc.isFullScreen()) {
			setBorderlessFullscreen(false);
			mc.toggleFullscreen();
			mc.toggleFullscreen();
		}
	}
	
	private void setBorderlessFullscreen(boolean state) {
		try {
			System.setProperty("org.lwjgl.opengl.Window.undecorated", Boolean.toString(state));
			Display.setFullscreen(false);
			Display.setResizable(!state);

			if(state) {
				prevX = Display.getX();
				prevY = Display.getY();
				prevWidth = mc.displayWidth;
				prevHeight = mc.displayHeight;
				Display.setDisplayMode(new DisplayMode(Display.getDesktopDisplayMode().getWidth(), Display.getDesktopDisplayMode().getHeight()));
				Display.setLocation(0, 0);
				((IMixinMinecraft)mc).resizeWindow(Display.getDesktopDisplayMode().getWidth(), Display.getDesktopDisplayMode().getHeight());
			}
			else {
				Display.setDisplayMode(new DisplayMode(prevWidth, prevHeight));
				Display.setLocation(prevX, prevY);
				((IMixinMinecraft)mc).resizeWindow(prevWidth, prevHeight);

				if(mc.inGameHasFocus) {
					mc.mouseHelper.ungrabMouseCursor();
					fullscreenTime = System.currentTimeMillis();
				}
			}
		}
		catch(LWJGLException error) {
			KurrinaLogger.error("Could not totggle borderless fullscreen", error);
		}
	}
}
