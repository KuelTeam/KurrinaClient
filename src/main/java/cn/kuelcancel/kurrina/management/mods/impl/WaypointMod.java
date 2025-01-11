package cn.kuelcancel.kurrina.management.mods.impl;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.gui.GuiWaypoint;
import cn.kuelcancel.kurrina.injection.interfaces.IMixinRenderManager;
import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventKey;
import cn.kuelcancel.kurrina.management.event.impl.EventRender3D;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import cn.kuelcancel.kurrina.management.mods.settings.impl.KeybindSetting;
import cn.kuelcancel.kurrina.management.waypoint.Waypoint;
import cn.kuelcancel.kurrina.utils.ColorUtils;
import cn.kuelcancel.kurrina.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class WaypointMod extends Mod {

	private KeybindSetting keybindSetting = new KeybindSetting(TranslateText.KEYBIND, this, Keyboard.KEY_B);
	
	public WaypointMod() {
		super(TranslateText.WAYPOINT, TranslateText.WAYPOINT_DESCRIPTION, ModCategory.WORLD);
	}

	@EventTarget
	public void onRender3D(EventRender3D event) {
		
		for(Waypoint wy : Kurrina.getInstance().getWaypointManager().getWaypoints()) {
			if(Kurrina.getInstance().getWaypointManager().getWorld().equals(wy.getWorld())) {
				
				double distance = this.getDistance(wy, mc.getRenderViewEntity());
				double renderDistance = (mc.gameSettings.renderDistanceChunks * 16) * 0.75;
				
				String tagName = wy.getName() + " [" + (int) distance + "m]";
				
				double x = wy.getX() - ((IMixinRenderManager)mc.getRenderManager()).getRenderPosX();
				double y = 2.0 + wy.getY() - ((IMixinRenderManager)mc.getRenderManager()).getRenderPosY();
				double z = wy.getZ() - ((IMixinRenderManager)mc.getRenderManager()).getRenderPosZ();
				
				if(distance > renderDistance) {
					x = x / distance * renderDistance;
					y = y / distance * renderDistance;
					z = z / distance * renderDistance;
					distance = renderDistance;
				}
				
				float scale = (float) (0.016666668f * (1.0 + distance) * 0.15);
				
                GL11.glPushMatrix();
                GlStateManager.translate(x, y, z);
                GlStateManager.disableDepth();

                GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
                GlStateManager.scale(-scale, -scale, scale);
                
                RenderUtils.drawRect((fr.getStringWidth(tagName) / 2), 0, fr.getStringWidth(tagName) + 11, fr.FONT_HEIGHT + 7, ColorUtils.getColorByInt(Integer.MIN_VALUE));
                RenderUtils.drawOutline((fr.getStringWidth(tagName) / 2), 0, fr.getStringWidth(tagName) + 11, fr.FONT_HEIGHT + 7, 2.5F, wy.getColor());
                
                fr.drawString(tagName, (fr.getStringWidth(tagName) / 2) + 6, 4, Color.WHITE.getRGB());
                
                GlStateManager.enableDepth();
                GL11.glPopMatrix();
			}
		}
	}
	
	@EventTarget
	public void onKey(EventKey event) {
		
		if(event.getKeyCode() == keybindSetting.getKeyCode()) {
			mc.displayGuiScreen(new GuiWaypoint());
		}
	}
	
    private double getDistance(Waypoint wy, Entity entity) {
    	
        double x = wy.getX() - entity.posX;
        double y = wy.getY() - entity.posY;
        double z = wy.getZ() - entity.posZ;
        
        return Math.sqrt(x * x + y * y + z * z);
    }
}
