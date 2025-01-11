package cn.kuelcancel.kurrina.management.notification;

import java.awt.Color;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.management.color.AccentColor;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.nanovg.NanoVGManager;
import cn.kuelcancel.kurrina.management.nanovg.font.Fonts;
import cn.kuelcancel.kurrina.utils.ColorUtils;
import cn.kuelcancel.kurrina.utils.TimerUtils;
import cn.kuelcancel.kurrina.utils.animation.normal.Animation;
import cn.kuelcancel.kurrina.utils.animation.normal.Direction;
import cn.kuelcancel.kurrina.utils.animation.normal.easing.EaseBackIn;
import cn.kuelcancel.kurrina.utils.buffer.ScreenAlpha;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Notification {

	private Animation animation;
	private TranslateText title, message;
	private NotificationType type;
	private TimerUtils timer;
	
	private ScreenAlpha screenAlpha = new ScreenAlpha();
	
	public Notification(TranslateText title, TranslateText message, NotificationType type) {
		this.title = title;
		this.message = message;
		this.type = type;
		this.timer = new TimerUtils();
	}
	
	public void draw() {
		
		NanoVGManager nvg = Kurrina.getInstance().getNanoVGManager();
		
		screenAlpha.wrap(() -> drawNanoVG(nvg), animation.getValueFloat());
	}
	
	private void drawNanoVG(NanoVGManager nvg) {
		
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		Kurrina instance = Kurrina.getInstance();
		AccentColor currentColor = instance.getColorManager().getCurrentColor();
		
		float maxWidth;
		float titleWidth = nvg.getTextWidth(title.getText(), 9.6F, Fonts.MEDIUM);
		float messageWidth = nvg.getTextWidth(message.getText(), 7.6F, Fonts.REGULAR);
		
		if(titleWidth > messageWidth) {
			maxWidth = titleWidth;
		}else {
			maxWidth = messageWidth;
		}
		
		maxWidth = maxWidth + 31;
		
		int x = (int) (sr.getScaledWidth() - maxWidth) - 8;
		int y = sr.getScaledHeight() - 29 - 8;
		
		if(timer.delay(3000)) {
			animation.setDirection(Direction.BACKWARDS);
		}
		
		nvg.save();
		nvg.translate(160 - (animation.getValueFloat() * 160), 0);
		
		nvg.drawShadow(x, y, maxWidth, 29, 6);
		nvg.drawGradientRoundedRect(x, y, maxWidth, 29, 6, ColorUtils.applyAlpha(currentColor.getColor1(), 220), ColorUtils.applyAlpha(currentColor.getColor2(), 220));
		nvg.drawText(type.getIcon(), x + 5, y + 6F, Color.WHITE, 17, Fonts.ICON);
		nvg.drawText(title.getText(), x + 26, y + 6F, Color.white, 9.6F, Fonts.MEDIUM);
		nvg.drawText(message.getText(), x + 26, y + 17.5F, Color.WHITE, 7.5F, Fonts.REGULAR);
		
		nvg.restore();
	}
	
	public void show() {
		animation = new EaseBackIn(300, 1, 0);
		animation.setDirection(Direction.FORWARDS);
		animation.reset();
		timer.reset();
	}
	
	public boolean isShown() {
		return !animation.isDone(Direction.BACKWARDS);
	}
	
	public Animation getAnimation() {
		return animation;
	}

	public TimerUtils getTimer() {
		return timer;
	}
}
