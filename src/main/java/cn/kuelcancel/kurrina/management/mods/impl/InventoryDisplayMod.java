package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventRender2D;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.HUDMod;
import cn.kuelcancel.kurrina.management.nanovg.NanoVGManager;
import cn.kuelcancel.kurrina.management.nanovg.font.Fonts;
import cn.kuelcancel.kurrina.utils.GlUtils;
import cn.kuelcancel.kurrina.utils.render.RenderUtils;
import net.minecraft.item.ItemStack;

public class InventoryDisplayMod extends HUDMod {

	public InventoryDisplayMod() {
		super(TranslateText.INVENTORY_DISPLAY, TranslateText.INVENTORY_DISPLAY_DESCRIPTION);
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		
		int startX = this.getX() + 6;
		int startY = this.getY() + 22;
        int index = 0;
        
		NanoVGManager nvg = Kurrina.getInstance().getNanoVGManager();
		
		nvg.setupAndDraw(() -> drawNanoVG());
		
		GlUtils.startScale(this.getX(), this.getY(), this.getScale());
		
		for(int i = 9; i < 36; i++) {
			
            ItemStack slot = mc.thePlayer.inventory.mainInventory[i];
            
            if(slot == null) {
                startX += 20;
                index += 1;

                if(index > 8) {
                	index = 0;
                    startY += 20;
                    startX = this.getX() + 4;
                }

                continue;
            }

            RenderUtils.drawItemStack(slot, startX, startY);
            
            startX += 20;
            index += 1;
            if(index > 8) {
            	index = 0;
                startY += 20;
                startX = this.getX() + 6;
            }
		}
		
		GlUtils.stopScale();
	}
	
	private void drawNanoVG() {
		
		this.drawBackground(188, 82);
		this.drawText("Inventory", 5.5F, 6F, 10.5F, Fonts.REGULAR);
		this.drawRect(0, 17.5F, 188, 1);
		
		this.setWidth(188);
		this.setHeight(82);
	}
}
