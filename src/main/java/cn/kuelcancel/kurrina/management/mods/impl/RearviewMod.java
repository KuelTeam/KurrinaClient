package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventFireOverlay;
import cn.kuelcancel.kurrina.management.event.impl.EventHurtCamera;
import cn.kuelcancel.kurrina.management.event.impl.EventRender2D;
import cn.kuelcancel.kurrina.management.event.impl.EventRenderPumpkinOverlay;
import cn.kuelcancel.kurrina.management.event.impl.EventRenderTick;
import cn.kuelcancel.kurrina.management.event.impl.EventWaterOverlay;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.HUDMod;
import cn.kuelcancel.kurrina.management.mods.impl.rearview.RearviewCamera;
import cn.kuelcancel.kurrina.management.mods.settings.impl.BooleanSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.NumberSetting;
import cn.kuelcancel.kurrina.management.nanovg.NanoVGManager;
import cn.kuelcancel.kurrina.utils.TimerUtils;

public class RearviewMod extends HUDMod {

	private RearviewCamera rearviewCamera = new RearviewCamera();
	private TimerUtils timer = new TimerUtils();
	
	private NumberSetting rearviewWidthSetting = new NumberSetting(TranslateText.WIDTH, this, 190, 10, 500, true);
	private NumberSetting rearviewHeightSetting = new NumberSetting(TranslateText.HEIGHT, this, 100, 10, 500, true);
	private NumberSetting fpsSetting = new NumberSetting(TranslateText.FPS, this, 60, 1, 120, true);
	private NumberSetting fovSetting = new NumberSetting(TranslateText.FOV, this, 70, 30, 120, true);
	private BooleanSetting lockCameraSetting = new BooleanSetting(TranslateText.LOCK_CAMERA, this, true);
	private NumberSetting alphaSetting = new NumberSetting(TranslateText.ALPHA, this, 1.0F, 0.0F, 1.0F, false);
	
	public RearviewMod() {
		super(TranslateText.REARVIEW, TranslateText.REARVIEW_DESCRIPTION);
	}

	@EventTarget
	public void onRenderTick(EventRenderTick event) {
		if(mc.theWorld != null) {
			if(timer.delay((long) (1000 / fpsSetting.getValue()))) {
				rearviewCamera.updateMirror();
				timer.reset();
			}
		}
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		
		NanoVGManager nvg = Kurrina.getInstance().getNanoVGManager();
		
		nvg.setupAndDraw(() -> drawNanoVG(nvg));
	}
	
	private void drawNanoVG(NanoVGManager nvg) {
		
		int width = (int) (rearviewWidthSetting.getValueInt() * this.getScale());
		int height = (int) (rearviewHeightSetting.getValueInt() * this.getScale());
		
		rearviewCamera.setFov(fovSetting.getValueFloat());
		rearviewCamera.setLockCamera(lockCameraSetting.isToggled());
		
		nvg.drawShadow(this.getX(), this.getY(), width, height, 6 * this.getScale());
		nvg.drawRoundedImage(rearviewCamera.getTexture(), this.getX(), this.getY() + height, width, -height, 6 * this.getScale(), alphaSetting.getValueFloat());
		
		this.setWidth((int) (width / this.getScale()));
		this.setHeight((int) (height / this.getScale()));
	}
	
	@EventTarget
	public void onFireOverlay(EventFireOverlay event) {
		if(rearviewCamera.isRecording()) {
			event.setCancelled(true);
		}
	}
	
	@EventTarget
	public void onWaterOverlay(EventWaterOverlay event) {
		if(rearviewCamera.isRecording()) {
			event.setCancelled(true);
		}
	}
	
	@EventTarget
	public void onHurtCamera(EventHurtCamera event) {
        if (rearviewCamera.isRecording()) {
    		event.setIntensity(0);
        }
	}
	
	@EventTarget
	public void onRenderPumpkinOverlay(EventRenderPumpkinOverlay event) {
		if(rearviewCamera.isRecording()) {
			event.setCancelled(true);
		}
	}
}
