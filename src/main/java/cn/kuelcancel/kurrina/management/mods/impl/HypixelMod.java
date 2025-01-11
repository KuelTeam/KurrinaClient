package cn.kuelcancel.kurrina.management.mods.impl;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventReceivePacket;
import cn.kuelcancel.kurrina.management.event.impl.EventSendChat;
import cn.kuelcancel.kurrina.management.event.impl.EventSendPacket;
import cn.kuelcancel.kurrina.management.event.impl.EventUpdate;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import cn.kuelcancel.kurrina.management.mods.impl.hypixel.HypixelGameMode;
import cn.kuelcancel.kurrina.management.mods.settings.impl.BooleanSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.NumberSetting;
import cn.kuelcancel.kurrina.utils.ColorUtils;
import cn.kuelcancel.kurrina.utils.Multithreading;
import cn.kuelcancel.kurrina.utils.ServerUtils;
import cn.kuelcancel.kurrina.utils.TimerUtils;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.scoreboard.Scoreboard;

public class HypixelMod extends Mod {

	private static HypixelMod instance;
	
	private BooleanSetting autoggSetting = new BooleanSetting(TranslateText.AUTO_GG, this, false);
	private NumberSetting autoggDelaySetting = new NumberSetting(TranslateText.AUTO_GG_DELAY, this, 3, 0, 5, true);
	
	private BooleanSetting autoPlaySetting = new BooleanSetting(TranslateText.AUTO_PLAY, this, false);
	private NumberSetting autoPlayDelaySetting = new NumberSetting(TranslateText.AUTO_PLAY_DELAY, this, 3, 0, 5, true);
	
	private BooleanSetting autoTipSetting = new BooleanSetting(TranslateText.AUTO_TIP, this, true);
	
	private BooleanSetting antiLSetting = new BooleanSetting(TranslateText.ANTI_L, this, false);
	
	private TimerUtils tipTimer = new TimerUtils();
	
	private HypixelGameMode currentMode;
	
	public HypixelMod() {
		super(TranslateText.HYPIXEL, TranslateText.HYPIXEL_DESCRIPTION, ModCategory.HYPIXEL);
		
		instance = this;
	}
	
	@Override
	public void setup() {
		currentMode = HypixelGameMode.SKYWARS_SOLO_NORMAL;
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {

		if(!ServerUtils.isHypixel()) {
			tipTimer.reset();
			return;
		}
		
		Scoreboard scoreboard = mc.theWorld.getScoreboard();
		
        if (scoreboard != null && scoreboard.getObjectiveInDisplaySlot(1) != null) {
        	
        	String title = ColorUtils.removeColorCode(scoreboard.getObjectiveInDisplaySlot(1).getDisplayName());
        	
        	if(title.contains("TNT RUN")) {
        		currentMode = HypixelGameMode.TNT_RUN;
        	}
        	
        	if(title.contains("BOW SPLEEF")) {
        		currentMode = HypixelGameMode.BOW_SPLEEF;
        	}
        	
        	if(title.contains("PVP RUN")) {
        		currentMode = HypixelGameMode.PVP_RUN;
        	}
        	
        	if(title.contains("TNT TAG")) {
        		currentMode = HypixelGameMode.TNT_TAG;
        	}
        	
        	if(title.contains("TNT WIZARDS")) {
        		currentMode = HypixelGameMode.TNT_WIZARDS;
        	}
        }
        
		if(autoTipSetting.isToggled()) {
			if(tipTimer.delay(1200000)) {
				mc.thePlayer.sendChatMessage("/tip all");
				tipTimer.reset();
			}
		}else {
			tipTimer.reset();
		}
	}

	@EventTarget
	public void onSentChat(EventSendChat event) {
		
		if(!ServerUtils.isHypixel()) {
			return;
		}
		
		String message = event.getMessage();
		
		if(message.startsWith("/play")) {
			
			HypixelGameMode mode = HypixelGameMode.getModeByCommand(message);
			
			if(mode != null) {
				currentMode = mode;
			}
		}
	}
	
	@EventTarget
	public void onReceivePacket(EventReceivePacket event) {
		
		if(event.getPacket() instanceof S2FPacketSetSlot) {
			
			S2FPacketSetSlot slotPacket = (S2FPacketSetSlot) event.getPacket();
			ItemStack stack = slotPacket.func_149174_e();
			
			if(stack != null && stack.getItem().equals(Items.paper) && 
					(HypixelGameMode.isBedwars(currentMode) || HypixelGameMode.isTntGames(currentMode))) {
				sendNextGame();
				return;
			}
		}
		
		if(event.getPacket() instanceof S02PacketChat) {
			
			S02PacketChat chatPacket = (S02PacketChat) event.getPacket();
			String chatMessage = chatPacket.getChatComponent().getUnformattedText();
			
            if (antiLSetting.isToggled()) {

                Pattern regex = Pattern.compile(".*\\b[Ll]+\\b.*");
                Matcher matcher = regex.matcher(chatMessage);
                
                event.setCancelled(matcher.find());
            }
		}
		
		if(event.getPacket() instanceof S45PacketTitle) {
			
			S45PacketTitle titlePacket = (S45PacketTitle) event.getPacket();
			
			if(titlePacket.getMessage() != null) {
				
				String title = titlePacket.getMessage().getFormattedText();
				
	            if (autoggSetting.isToggled() && title.startsWith("\2476\247l") && title.endsWith("\247r")) {
        			Multithreading.schedule(() -> {
        				mc.thePlayer.sendChatMessage("/achat gg");
        			}, autoggDelaySetting.getValueInt(), TimeUnit.SECONDS);
	            }
	            
				if(title.startsWith("\2476\247l") && title.endsWith("\247r") || title.startsWith("\247c\247lY") && title.endsWith("\247r")) {
					sendNextGame();
				}
			}
		}
	}
	
    @EventTarget
    public void onSendPacket(EventSendPacket event) {
    	
    	if(!ServerUtils.isHypixel()) {
    		return;
    	}
    	
        if (event.getPacket() instanceof C0EPacketClickWindow) {
        	
            C0EPacketClickWindow packet = (C0EPacketClickWindow) event.getPacket();
            String itemname;
            
            if(packet.getClickedItem() == null) {
            	return;
            }
            
            itemname = packet.getClickedItem().getDisplayName();
            
            if (packet.getClickedItem().getDisplayName().startsWith("\247a")) {
            	
                int itemID = Item.getIdFromItem(packet.getClickedItem().getItem());
                
                if (itemID == 381 || itemID == 368) {
                    if (itemname.contains("SkyWars")) {
                        if (itemname.contains("Doubles")) {
                            if (itemname.contains("Normal")) {
                            	currentMode = HypixelGameMode.SKYWARS_DOUBLES_NORMAL;
                            } else if (itemname.contains("Insane")) {
                            	currentMode = HypixelGameMode.SKYWARS_DOUBLES_INSANE;
                            }
                        } else if (itemname.contains("Solo")) {
                            if (itemname.contains("Normal")) {
                            	currentMode = HypixelGameMode.SKYWARS_SOLO_NORMAL;
                            } else if (itemname.contains("Insane")) {
                            	currentMode = HypixelGameMode.SKYWARS_SOLO_INSANE;
                            }
                        }
                    }
                } else if (itemID == 355) {
                    if (itemname.contains("Bed Wars")) {
                        if (itemname.contains("4v4")) {
                        	currentMode = HypixelGameMode.BEDWARS_4V4;
                        } else if (itemname.contains("3v3")) {
                        	currentMode = HypixelGameMode.BEDWARS_3V3;
                        } else if (itemname.contains("Doubles")) {
                        	currentMode = HypixelGameMode.BEDWARS_DOUBLES;
                        } else if (itemname.contains("Solo")) {
                        	currentMode = HypixelGameMode.BEDWARS_SOLO;
                        }
                    }
                } else if(itemID == 397) {
                	if(itemname.contains("UHC Duel")) {
                		if(itemname.contains("1v1")) {
                			currentMode = HypixelGameMode.UHC_DUEL_1V1;
                		} else if(itemname.contains("2v2")) {
                			currentMode = HypixelGameMode.UHC_DUEL_2V2;
                		} else if(itemname.contains("4v4")) {
                			currentMode = HypixelGameMode.UHC_DUEL_4V4;
                		} else if(itemname.contains("Player FFA")) {
                			currentMode = HypixelGameMode.UHC_DUEL_MEETUP;
                		}
                	}
                }
            }
        }
    }
    
    private void sendNextGame() {
    	if(autoPlaySetting.isToggled()) {
        	Multithreading.schedule(()-> {
        		mc.thePlayer.sendChatMessage(currentMode.getCommand());
        	}, autoPlayDelaySetting.getValueInt(), TimeUnit.SECONDS);
    	}
    }
    
	public static HypixelMod getInstance() {
		return instance;
	}
}
