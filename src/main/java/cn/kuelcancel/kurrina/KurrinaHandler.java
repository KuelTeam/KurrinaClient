package cn.kuelcancel.kurrina;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;

import cn.kuelcancel.kurrina.gui.modmenu.GuiModMenu;
import cn.kuelcancel.kurrina.logger.KurrinaLogger;
import cn.kuelcancel.kurrina.management.account.Account;
import cn.kuelcancel.kurrina.management.account.AccountManager;
import cn.kuelcancel.kurrina.management.account.AccountType;
import cn.kuelcancel.kurrina.management.cape.CapeManager;
import cn.kuelcancel.kurrina.management.cape.impl.Cape;
import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventClickMouse;
import cn.kuelcancel.kurrina.management.event.impl.EventJoinServer;
import cn.kuelcancel.kurrina.management.event.impl.EventLocationCape;
import cn.kuelcancel.kurrina.management.event.impl.EventLocationSkin;
import cn.kuelcancel.kurrina.management.event.impl.EventReceivePacket;
import cn.kuelcancel.kurrina.management.event.impl.EventTick;
import cn.kuelcancel.kurrina.management.event.impl.EventUpdate;
import cn.kuelcancel.kurrina.management.profile.Profile;
import cn.kuelcancel.kurrina.utils.OptifineUtils;
import cn.kuelcancel.kurrina.utils.TargetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.util.ResourceLocation;

public class KurrinaHandler {

	private Minecraft mc = Minecraft.getMinecraft();
	
	private Kurrina instance;
	
	private String prevOfflineName;
	private ResourceLocation offlineSkin;
	
	public KurrinaHandler() {
		instance = Kurrina.getInstance();
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		OptifineUtils.disableFastRender();
	}
	
	@EventTarget
	public void onJoinServer(EventJoinServer event) {
		for(Profile p : instance.getProfileManager().getProfiles()) {
			if(!p.getServerIp().isEmpty() && StringUtils.containsIgnoreCase(event.getIp(), p.getServerIp())) {
				instance.getModManager().disableAll();
				instance.getProfileManager().load(p.getJsonFile());
				break;
			}
		}
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		TargetUtils.onUpdate();
	}
	
	@EventTarget
	public void onClickMouse(EventClickMouse event) {
        if (mc.gameSettings.keyBindTogglePerspective.isPressed()) {
            mc.gameSettings.thirdPersonView = (mc.gameSettings.thirdPersonView + 1) % 3;
            mc.renderGlobal.setDisplayListEntitiesDirty();
        }
	}
	
	@EventTarget
	public void onReceivePacket(EventReceivePacket event) {
    	if(event.getPacket() instanceof S2EPacketCloseWindow && mc.currentScreen instanceof GuiModMenu) {
    		event.setCancelled(true);
    	}
	}
	
	@EventTarget
	public void onCape(EventLocationCape event) {
		
		CapeManager capeManager = instance.getCapeManager();
		
		if(event.getPlayerInfo() != null && event.getPlayerInfo().getGameProfile().getId().equals(mc.thePlayer.getGameProfile().getId())) {
			
			Cape currentCape = capeManager.getCurrentCape();
			
			if(!currentCape.equals(capeManager.getCapeByName("None"))) {
				event.setCancelled(true);
				event.setCape(currentCape.getCape());
			}
		}
	}
	
	@EventTarget
	public void onSkin(EventLocationSkin event) {
		
		if(event.getPlayerInfo() != null && event.getPlayerInfo().getGameProfile().getId().equals(mc.thePlayer.getGameProfile().getId())) {
			
			AccountManager accountManager = instance.getAccountManager();
			Account currentAccount = accountManager.getCurrentAccount();
			
			if(currentAccount.getType().equals(AccountType.OFFLINE)) {
				
				if(prevOfflineName != currentAccount.getName()) {
					
					if(currentAccount.getSkinFile() != null) {
						try {
							
				            BufferedImage t = ImageIO.read(currentAccount.getSkinFile());
				            DynamicTexture nibt = new DynamicTexture(t);
				            
				            offlineSkin = mc.getTextureManager().getDynamicTextureLocation("offlineskin", nibt);
						} catch(Exception e) {
							KurrinaLogger.error("Failed to load offline skin", e);
						}
					}
				}
				
				if(offlineSkin != null) {
					event.setCancelled(true);
					event.setSkin(offlineSkin);
				}
			}
		}
	}
}
