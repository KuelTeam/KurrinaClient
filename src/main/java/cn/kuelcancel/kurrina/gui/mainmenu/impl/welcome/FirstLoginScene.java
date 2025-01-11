package cn.kuelcancel.kurrina.gui.mainmenu.impl.welcome;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.gui.mainmenu.GuiKurrinaMainMenu;
import cn.kuelcancel.kurrina.gui.mainmenu.impl.AccountScene;
import cn.kuelcancel.kurrina.management.nanovg.NanoVGManager;
import cn.kuelcancel.kurrina.utils.animation.normal.Animation;
import cn.kuelcancel.kurrina.utils.animation.normal.Direction;
import cn.kuelcancel.kurrina.utils.animation.normal.other.DecelerateAnimation;
import cn.kuelcancel.kurrina.utils.buffer.ScreenAlpha;
import cn.kuelcancel.kurrina.utils.render.BlurUtils;
import net.minecraft.client.gui.ScaledResolution;

public class FirstLoginScene extends AccountScene {
	
	private Animation fadeAnimation;
	private ScreenAlpha screenAlpha = new ScreenAlpha();
	
	public FirstLoginScene(GuiKurrinaMainMenu parent) {
		super(parent);
	}

	@Override
	public void initScene() {
		super.initScene();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		ScaledResolution sr = new ScaledResolution(mc);
		Kurrina instance = Kurrina.getInstance();
		NanoVGManager nvg = instance.getNanoVGManager();
		
		if(fadeAnimation == null) {
			fadeAnimation = new DecelerateAnimation(800, 1);
			fadeAnimation.setDirection(Direction.FORWARDS);
			fadeAnimation.reset();
		}
		
		if(fadeAnimation.isDone(Direction.BACKWARDS)) {
			this.setCurrentScene(this.getSceneByClass(LastMessageScene.class));
		}
		
		BlurUtils.drawBlurScreen(14);
		
		screenAlpha.wrap(() -> this.drawNanoVG(mouseX, mouseY, partialTicks, sr, instance, nvg), fadeAnimation.getValueFloat());
	}
	
	/*@Override
	public Runnable afterMicrosoftLogin() {
		
		AccountManager accountManager = Kurrina.getInstance().getAccountManager();
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				if(accountManager.getCurrentAccount() != null) {
					accountManager.save();
					fadeAnimation.setDirection(Direction.BACKWARDS);
				}
			}
		};
		
		return runnable;
	}*/
	
	@Override
	public void keyTyped(char typedChar, int keyCode) {
		textBox.keyTyped(typedChar, keyCode);
	}
}