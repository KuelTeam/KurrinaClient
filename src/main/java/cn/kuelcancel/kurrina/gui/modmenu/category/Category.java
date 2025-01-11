package cn.kuelcancel.kurrina.gui.modmenu.category;

import cn.kuelcancel.kurrina.gui.modmenu.GuiModMenu;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.ui.comp.impl.field.CompSearchBox;
import cn.kuelcancel.kurrina.utils.animation.ColorAnimation;
import cn.kuelcancel.kurrina.utils.animation.simple.SimpleAnimation;
import cn.kuelcancel.kurrina.utils.mouse.Scroll;
import net.minecraft.client.Minecraft;

public class Category {

	public Minecraft mc = Minecraft.getMinecraft();
	
	private String icon;
	private GuiModMenu parent;
	
	private TranslateText nameTranslate;
	
	private SimpleAnimation textAnimation = new SimpleAnimation();
	private ColorAnimation textColorAnimation = new ColorAnimation();
	private SimpleAnimation categoryAnimation = new SimpleAnimation();
	
	private boolean initialized;
	
	public Scroll scroll;
	
	private boolean showSearchBox;
	
	public Category(GuiModMenu parent, TranslateText nameTranslate, String icon, boolean showSearchBox) {
		this.nameTranslate = nameTranslate;
		this.parent = parent;
		this.icon = icon;
		this.initialized = false;
		this.scroll = parent.getScroll();
		this.showSearchBox = showSearchBox;
	}
	
	public void initGui() {}
	
	public void initCategory() {}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {}
	
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}
	
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {}
	
	public void keyTyped(char typedChar, int keyCode) {}
	
	public String getName() {
		return nameTranslate.getText();
	}

	public String getNameKey() {
		return nameTranslate.getKey();
	}

	public String getIcon() {
		return icon;
	}

	public int getX() {
		return parent.getX() + 32;
	}
	
	public int getY() {
		return parent.getY() + 31;
	}
	
	public int getWidth() {
		return parent.getWidth() - 32;
	}
	
	public int getHeight() {
		return parent.getHeight() - 31;
	}

	public ColorAnimation getTextColorAnimation() {
		return textColorAnimation;
	}

	public SimpleAnimation getTextAnimation() {
		return textAnimation;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public SimpleAnimation getCategoryAnimation() {
		return categoryAnimation;
	}

	public boolean isShowSearchBox() {
		return showSearchBox;
	}
	
	public CompSearchBox getSearchBox() {
		return parent.getSearchBox();
	}
	
	public boolean isCanClose() {
		return parent.isCanClose();
	}
	
	public void setCanClose(boolean canClose) {
		parent.setCanClose(canClose);
	}
}
