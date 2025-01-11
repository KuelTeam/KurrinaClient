package cn.kuelcancel.kurrina.gui.mainmenu;

import cn.kuelcancel.kurrina.management.nanovg.NanoVGManager;
import cn.kuelcancel.kurrina.utils.animation.simple.SimpleAnimation;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class MainMenuScene {

	public Minecraft mc = Minecraft.getMinecraft();
	private GuiKurrinaMainMenu parent;

	private SimpleAnimation animation = new SimpleAnimation();

	public MainMenuScene(GuiKurrinaMainMenu parent) {
		this.parent = parent;
	}

	public void initScene() {}

	public void initGui() {}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {}

	public void drawScreen(int mouseX, int mouseY, float partialTicks, SimpleAnimation animation) {}

	public void drawButton(NanoVGManager nvg, String text, float x, float y, int mouseX, int mouseY, int width, int height, SimpleAnimation animation) {}

	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException, URISyntaxException {}

	public void keyTyped(char typedChar, int keyCode) {}

	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {}

	public void handleInput() {}

	public void onGuiClosed() {}

	public void onSceneClosed() {}

	public GuiKurrinaMainMenu getParent() {
		return parent;
	}

	public void setCurrentScene(MainMenuScene scene) {
		parent.setCurrentScene(scene);
	}

	public Color getBackgroundColor() {
		return parent.getBackgroundColor();
	}

	public SimpleAnimation getAnimation() {
		return animation;
	}

	public MainMenuScene getSceneByClass(Class<? extends MainMenuScene> clazz) {
		return parent.getSceneByClass(clazz);
	}
}
