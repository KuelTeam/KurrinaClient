package cn.kuelcancel.kurrina.management.mods.impl;

import org.lwjgl.opengl.GL11;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.injection.interfaces.IMixinS14PacketEntity;
import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventReceivePacket;
import cn.kuelcancel.kurrina.management.event.impl.EventRender3D;
import cn.kuelcancel.kurrina.management.event.impl.EventUpdate;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import cn.kuelcancel.kurrina.utils.ColorUtils;
import cn.kuelcancel.kurrina.utils.Render3DUtils;
import cn.kuelcancel.kurrina.utils.ServerUtils;
import cn.kuelcancel.kurrina.utils.TargetUtils;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;

public class PlayerPredicatorMod extends Mod {

	private Position realTargetPosition = new Position(0, 0, 0);
	private AbstractClientPlayer target;
	private boolean isActive;
	
	public PlayerPredicatorMod() {
		super(TranslateText.PLAYER_PREDICATOR, TranslateText.PLAYER_PREDICATOR_DESCRIPTION, ModCategory.WORLD);
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		target = TargetUtils.getTarget();
		
		if(target == null) {
			isActive = true;
			return;
		}
		
		if(isActive) {
			realTargetPosition = new Position(target.posX, target.posY, target.posZ);
			isActive = false;
		}
	}
	
	@EventTarget
	public void onReceivePacket(EventReceivePacket event) {
		
		Packet<?> packet = event.getPacket();
		
		if(target == null) {
			return;
		}
		
		if(packet instanceof S14PacketEntity) {
			
            S14PacketEntity s14PacketEntity = ((S14PacketEntity) packet);
            IMixinS14PacketEntity iS14PacketEntity = (IMixinS14PacketEntity) s14PacketEntity;
            
            if (iS14PacketEntity.getEntityId() == target.getEntityId()) {
                realTargetPosition.x += iS14PacketEntity.getPosX() / 32D;
                realTargetPosition.y += iS14PacketEntity.getPosY() / 32D;
                realTargetPosition.z += iS14PacketEntity.getPosZ() / 32D;
            }
		} else if(packet instanceof S18PacketEntityTeleport) {
			
			S18PacketEntityTeleport s18PacketEntityTeleport = (S18PacketEntityTeleport) packet;
			
			realTargetPosition = new Position(s18PacketEntityTeleport.getX() / 32D, s18PacketEntityTeleport.getY() / 32D, s18PacketEntityTeleport.getZ() / 32D);
		}
	}
	
	@EventTarget
	public void onRender3D(EventRender3D event) {
		
		if(target == null) {
			return;
		}
		
		if (realTargetPosition.squareDistanceTo(target.posX, target.posY, target.posZ) > 0.00001 && !ServerUtils.isHypixel()) {
			
	        GlStateManager.pushMatrix();
	        GlStateManager.pushAttrib();
	        GlStateManager.enableBlend();
	        GlStateManager.disableTexture2D();
	        GlStateManager.disableLighting();
	        GL11.glDepthMask(false);

	        double expand = 0.14;

	        ColorUtils.setColor(ColorUtils.applyAlpha(Kurrina.getInstance().getColorManager().getCurrentColor().getInterpolateColor(0), 80).getRGB());
	        
	        Render3DUtils.drawBoundingBox(mc.thePlayer.getEntityBoundingBox().offset(-mc.thePlayer.posX, -mc.thePlayer.posY, -mc.thePlayer.posZ).
	                offset(realTargetPosition.x, realTargetPosition.y, realTargetPosition.z).expand(expand, expand, expand));

	        GlStateManager.enableTexture2D();
	        GlStateManager.enableLighting();
	        GlStateManager.disableBlend();
	        GL11.glDepthMask(true);
	        GlStateManager.popAttrib();
	        GlStateManager.popMatrix();
	        GlStateManager.resetColor();
		}
	}
	
	private class Position {
		
		private double x, y, z;
		
		private Position(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		private double squareDistanceTo(double x, double y, double z) {
	    	
	        double d0 = x - this.x;
	        double d1 = y - this.y;
	        double d2 = z - this.z;
	        
	        return d0 * d0 + d1 * d1 + d2 * d2;
	    }
	}
}
