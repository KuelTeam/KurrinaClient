package cn.kuelcancel.kurrina.gui.mainmenu.impl.welcome;

import java.awt.Color;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.gui.mainmenu.GuiKurrinaMainMenu;
import cn.kuelcancel.kurrina.gui.mainmenu.MainMenuScene;
import cn.kuelcancel.kurrina.management.nanovg.NanoVGManager;
import cn.kuelcancel.kurrina.management.nanovg.font.Fonts;
import cn.kuelcancel.kurrina.utils.TimerUtils;
import cn.kuelcancel.kurrina.utils.animation.normal.Animation;
import cn.kuelcancel.kurrina.utils.animation.normal.Direction;
import cn.kuelcancel.kurrina.utils.animation.normal.other.DecelerateAnimation;
import cn.kuelcancel.kurrina.utils.render.BlurUtils;
import net.minecraft.client.gui.ScaledResolution;

public class WelcomeMessageScene extends MainMenuScene {

	// Todo: translate text

	private Animation fadeAnimation;
	private int step;
	private String message;

	private TimerUtils timer = new TimerUtils();

	public WelcomeMessageScene(GuiKurrinaMainMenu parent) {
		super(parent);

		step = 0;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		ScaledResolution sr = new ScaledResolution(mc);
		NanoVGManager nvg = Kurrina.getInstance().getNanoVGManager();
		String hello = "Hello!";
		String welcomeMessage = "Welcome to Kurrina Client";
		String setupMessage = "This is a Updated Version of Soar Client";
		String setupMessage2 = "Time to setup Kurrina Client.";

		BlurUtils.drawBlurScreen(14);

		if(fadeAnimation == null && this.getParent().isDoneBackgroundAnimation()) {
			fadeAnimation = new DecelerateAnimation(800, 1);
			fadeAnimation.setDirection(Direction.FORWARDS);
			fadeAnimation.reset();
			timer.reset();
		}

		if(fadeAnimation != null) {

			switch(step) {
				case 0:
					message = hello;
					break;
				case 1:
					message = welcomeMessage;
					break;
				case 2:
					message = setupMessage;
					break;
				case 3:
					message = setupMessage2;
					break;
			}

			nvg.setupAndDraw(() -> {
				nvg.drawCenteredText(message, sr.getScaledWidth() / 2,
						(sr.getScaledHeight() / 2) - (nvg.getTextHeight(message, 26, Fonts.REGULAR) / 2),
						new Color(255, 255, 255, (int) (fadeAnimation.getValueFloat() * 255)), 26, Fonts.REGULAR);
			});

			if(timer.delay(2500) && fadeAnimation.getDirection().equals(Direction.FORWARDS)) {
				fadeAnimation.setDirection(Direction.BACKWARDS);
				timer.reset();
			}

			if(fadeAnimation.isDone(Direction.BACKWARDS)) {

				if(step == 4) {
					this.setCurrentScene(this.getSceneByClass(LanguageSelectScene.class));
					return;
				}

				step++;
				fadeAnimation.setDirection(Direction.FORWARDS);
			}
		}
	}
}
