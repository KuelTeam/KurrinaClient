package cn.kuelcancel.kurrina.gui.mainmenu.impl.welcome;

import java.awt.Color;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.gui.mainmenu.GuiKurrinaMainMenu;
import cn.kuelcancel.kurrina.gui.mainmenu.MainMenuScene;
import cn.kuelcancel.kurrina.gui.mainmenu.impl.MainScene;
import cn.kuelcancel.kurrina.management.nanovg.NanoVGManager;
import cn.kuelcancel.kurrina.management.nanovg.font.Fonts;
import cn.kuelcancel.kurrina.utils.TimerUtils;
import cn.kuelcancel.kurrina.utils.animation.normal.Animation;
import cn.kuelcancel.kurrina.utils.animation.normal.Direction;
import cn.kuelcancel.kurrina.utils.animation.normal.other.DecelerateAnimation;
import cn.kuelcancel.kurrina.utils.render.BlurUtils;
import net.minecraft.client.gui.ScaledResolution;

public class LastMessageScene extends MainMenuScene {

	private Animation fadeAnimation, blurAnimation;
	private int step;
	private String message;
	
	private TimerUtils timer = new TimerUtils();
	
	public LastMessageScene(GuiKurrinaMainMenu parent) {
		super(parent);
		
		step = 0;
		blurAnimation = new DecelerateAnimation(800, 13);
		blurAnimation.setValue(13);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		ScaledResolution sr = new ScaledResolution(mc);
		NanoVGManager nvg = Kurrina.getInstance().getNanoVGManager();
		String compMessage = "Setup is complete!";
		String welcomeMessage = "Thank you for choosing Kurrina Client!";
		
		BlurUtils.drawBlurScreen(1 + blurAnimation.getValueFloat());
		
		if(fadeAnimation == null && this.getParent().isDoneBackgroundAnimation()) {
			fadeAnimation = new DecelerateAnimation(800, 1);
			fadeAnimation.setDirection(Direction.FORWARDS);
			fadeAnimation.reset();
			timer.reset();
		}
		
		if(blurAnimation.isDone(Direction.BACKWARDS)) {
			Kurrina.getInstance().getApi().createFirstLoginFile();
			this.setCurrentScene(this.getSceneByClass(MainScene.class));
		}
		
		if(fadeAnimation != null) {
			
			switch(step) {
				case 0:
					message = compMessage;
					break;
				case 1:
					message = welcomeMessage;
					break;
			}
			
			nvg.setupAndDraw(() -> {
				nvg.drawCenteredText(message, sr.getScaledWidth() / 2, 
						(sr.getScaledHeight() / 2) - (nvg.getTextHeight(message, 26, Fonts.REGULAR) / 2), 
						new Color(255, 255, 255, (int) (fadeAnimation.getValueFloat() * 255)), 26, Fonts.REGULAR);
			});
			
			if(timer.delay(5000) && fadeAnimation.getDirection().equals(Direction.FORWARDS)) {
				fadeAnimation.setDirection(Direction.BACKWARDS);
				timer.reset();
			}
			
			if(fadeAnimation.isDone(Direction.BACKWARDS)) {
				
				if(step == 1) {
					blurAnimation.setDirection(Direction.BACKWARDS);
					return;
				}
				
				step++;
				fadeAnimation.setDirection(Direction.FORWARDS);
			}
		}
	}
}