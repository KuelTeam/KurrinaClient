package cn.kuelcancel.kurrina.management.mods.impl;

import org.lwjgl.input.Keyboard;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.gui.GuiWebBrowser;
import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventKey;
import cn.kuelcancel.kurrina.management.event.impl.EventRender2D;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.HUDMod;
import cn.kuelcancel.kurrina.management.mods.settings.impl.KeybindSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.NumberSetting;
import cn.kuelcancel.kurrina.management.nanovg.NanoVGManager;
import cn.kuelcancel.kurrina.management.notification.NotificationType;
import cn.kuelcancel.kurrina.mcef.Mcef;
import cn.kuelcancel.kurrina.mcef.McefBrowser;

public class WebBrowserMod extends HUDMod {

	private static WebBrowserMod instance;
	
	private NumberSetting widthSetting = new NumberSetting(TranslateText.WIDTH, this, 190, 10, 500, true);
	private NumberSetting heightSetting = new NumberSetting(TranslateText.HEIGHT, this, 100, 10, 500, true);
	private NumberSetting alphaSetting = new NumberSetting(TranslateText.ALPHA, this, 1.0F, 0.0F, 1.0F, false);
	
	private KeybindSetting keybindSetting = new KeybindSetting(TranslateText.KEYBIND, this, Keyboard.KEY_J);
	
	private McefBrowser browser;
	
	public WebBrowserMod() {
		super(TranslateText.WEB_BROWSER, TranslateText.WEB_BROWSER_DESCRIPTION);
		
		instance = this;
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		
		NanoVGManager nvg = Kurrina.getInstance().getNanoVGManager();
		
		nvg.setupAndDraw(() -> drawNanoVG(nvg));
	}
	
	private void drawNanoVG(NanoVGManager nvg) {
		
		int width = (int) (widthSetting.getValueInt() * this.getScale());
		int height = (int) (heightSetting.getValueInt() * this.getScale());
		
		if(browser != null) {
			nvg.drawShadow(this.getX(), this.getY(), width, height, 6 * this.getScale());
			nvg.drawRoundedImage(browser.getTexture(), this.getX(), this.getY(), width, height, 6 * this.getScale(), alphaSetting.getValueFloat());
		}

		this.setWidth((int) (width / this.getScale()));
		this.setHeight((int) (height / this.getScale()));
	}
	
	@EventTarget
	public void onKey(EventKey event) {
		
		if(event.getKeyCode() == keybindSetting.getKeyCode()) {
			
			if(browser == null) {
				
				if(!Mcef.isInitialized()) {
					Mcef.getClient();
				}
				
		        String url = "https://www.google.com/";
		        
		        boolean transparent = true;
		        browser = Mcef.createBrowser(url, transparent);
		        
		        if(browser == null) {
		        	Kurrina.getInstance().getNotificationManager().post(TranslateText.ERROR, TranslateText.REQUIRED_FILE_MISSING, NotificationType.ERROR);
		        }
			}
			
			if(browser != null) {
				mc.displayGuiScreen(new GuiWebBrowser(browser));
			}
		}
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		
		if(browser != null) {
			browser.close();
			browser = null;
		}
	}

	public static WebBrowserMod getInstance() {
		return instance;
	}
}
