package cn.kuelcancel.kurrina.gui.modmenu.category.impl;

import java.awt.Color;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.KurrinaAPI;
import cn.kuelcancel.kurrina.gui.modmenu.GuiModMenu;
import cn.kuelcancel.kurrina.gui.modmenu.category.Category;
import cn.kuelcancel.kurrina.management.cape.CapeCategory;
import cn.kuelcancel.kurrina.management.cape.CapeManager;
import cn.kuelcancel.kurrina.management.cape.impl.Cape;
import cn.kuelcancel.kurrina.management.cape.impl.CustomCape;
import cn.kuelcancel.kurrina.management.cape.impl.NormalCape;
import cn.kuelcancel.kurrina.management.color.AccentColor;
import cn.kuelcancel.kurrina.management.color.ColorManager;
import cn.kuelcancel.kurrina.management.color.palette.ColorPalette;
import cn.kuelcancel.kurrina.management.color.palette.ColorType;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.nanovg.NanoVGManager;
import cn.kuelcancel.kurrina.management.nanovg.font.Fonts;
import cn.kuelcancel.kurrina.management.nanovg.font.Icon;
import cn.kuelcancel.kurrina.management.notification.NotificationType;
import cn.kuelcancel.kurrina.utils.ColorUtils;
import cn.kuelcancel.kurrina.utils.SearchUtils;
import cn.kuelcancel.kurrina.utils.mouse.MouseUtils;

public class CosmeticsCategory extends Category {

	private CapeCategory currentCategory;
	
	public CosmeticsCategory(GuiModMenu parent) {
		super(parent, TranslateText.COSMETICS, Icon.SHOPPING, true);
	}

	@Override
	public void initGui() {
		currentCategory = CapeCategory.ALL;
	}
	
	@Override
	public void initCategory() {
		scroll.resetAll();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		Kurrina instance = Kurrina.getInstance();
		NanoVGManager nvg = instance.getNanoVGManager();
		ColorManager colorManager = instance.getColorManager();
		AccentColor accentColor = colorManager.getCurrentColor();
		ColorPalette palette = colorManager.getPalette();
		CapeManager capeManager = instance.getCapeManager();
		
		int offsetX = 0;
		float offsetY = 13;
		int index = 1;
		int prevIndex = 1;
		
		nvg.save();
		nvg.translate(0, scroll.getValue());
		
		for(CapeCategory c : CapeCategory.values()) {
			
			float textWidth = nvg.getTextWidth(c.getName(), 9, Fonts.MEDIUM);
			boolean isCurrentCategory = c.equals(currentCategory);
			
			c.getBackgroundAnimation().setAnimation(isCurrentCategory ? 1.0F : 0.0F, 16);
			
			Color defaultColor = palette.getBackgroundColor(ColorType.DARK);
			Color color1 = ColorUtils.applyAlpha(accentColor.getColor1(), (int) (c.getBackgroundAnimation().getValue() * 255));
			Color color2 = ColorUtils.applyAlpha(accentColor.getColor2(), (int) (c.getBackgroundAnimation().getValue() * 255));
			Color textColor = c.getTextColorAnimation().getColor(isCurrentCategory ? Color.WHITE : palette.getFontColor(ColorType.DARK), 20);

			nvg.drawRoundedRect(this.getX() + 15 + offsetX, this.getY() + offsetY - 3, textWidth + 20, 16, 6, defaultColor);
			nvg.drawGradientRoundedRect(this.getX() + 15 + offsetX, this.getY() + offsetY - 3, textWidth + 20, 16, 6, color1, color2);
			
			nvg.drawText(c.getName(), this.getX() + 15 + offsetX + ((textWidth + 20) - textWidth) / 2, this.getY() + offsetY + 1.5F, textColor, 9, Fonts.MEDIUM);
			
			offsetX+=textWidth + 28;
		}
		
		offsetX = 0;
		offsetY = offsetY + 23;
		
		for(Cape cp : capeManager.getCapes()) {
			
			if(filterCape(cp)) {
				continue;
			}
			
			cp.getAnimation().setAnimation(cp.equals(capeManager.getCurrentCape()) ? 1.0F : 0.0F, 16);
			
			nvg.drawGradientRoundedRect(this.getX() + 15 + offsetX - 2, this.getY() + offsetY - 2, 88 + 4, 135 + 4, 8.5F, ColorUtils.applyAlpha(accentColor.getColor1(), (int) (cp.getAnimation().getValue() * 255)), ColorUtils.applyAlpha(accentColor.getColor2(), (int) (cp.getAnimation().getValue() * 255)));
			nvg.drawRoundedRect(this.getX() + 15 + offsetX, this.getY() + offsetY, 88, 135, 8, palette.getBackgroundColor(ColorType.DARK));
			
			if(cp instanceof NormalCape) {
				
				NormalCape c = ((NormalCape)cp);
				
				if(c.getSample() != null) {
					nvg.drawRoundedImage(c.getSample(), this.getX() + 24 + offsetX, this.getY() + offsetY + 9, 70, 105, 8);
				}
			}else if(cp instanceof CustomCape) {
				
				CustomCape c = ((CustomCape)cp);
				
				if(c.getSample() != null) {
					nvg.drawRoundedImage(c.getSample(), this.getX() + 24 + offsetX, this.getY() + offsetY + 9, 70, 105, 8);
				}
			}
			
			nvg.drawCenteredText(cp.getName(), this.getX() + 15 + offsetX + (88 / 2), this.getY() + offsetY + 120.5F, palette.getFontColor(ColorType.DARK), 10, Fonts.MEDIUM);
			
			offsetX+=100;
			
			if(index % 4 == 0) {
				offsetX = 0;
				offsetY+=147;
				prevIndex++;
			}
			
			index++;
		}
		
		scroll.setMaxScroll(prevIndex == 1 ? 0 : offsetY - (147 / 1.48F));
		
		nvg.restore();
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		
		Kurrina instance = Kurrina.getInstance();
		NanoVGManager nvg = instance.getNanoVGManager();
		KurrinaAPI api = instance.getApi();
		
		int offsetX = 0;
		float offsetY = 13 + scroll.getValue();
		CapeManager capeManager = instance.getCapeManager();
		int index = 1;
		
		for(CapeCategory c : CapeCategory.values()) {
			
			float textWidth = nvg.getTextWidth(c.getName(), 9, Fonts.MEDIUM);
			
			if(MouseUtils.isInside(mouseX, mouseY, this.getX() + 15 + offsetX, this.getY() + offsetY - 3, textWidth + 20, 16) && mouseButton == 0) {
				currentCategory = c;
			}
			
			offsetX+=textWidth + 28;
		}
		
		offsetX = 0;
		offsetY = offsetY + 23;
		
		for(Cape cp : capeManager.getCapes()) {
			
			if(filterCape(cp)) {
				continue;
			}
			
			if(MouseUtils.isInside(mouseX, mouseY, this.getX() + 15 + offsetX, this.getY() + offsetY, 88, 135) && mouseButton == 0) {
				
				if(cp.isPremium()) {
					
					if(api.isSpecialUser()) {
						capeManager.setCurrentCape(cp);
					} else {
						instance.getNotificationManager().post(TranslateText.ERROR, TranslateText.PREMIUM_ONLY, NotificationType.ERROR);
					}
				} else {
					capeManager.setCurrentCape(cp);
				}
			}
			
			offsetX+=100;
			
			if(index % 4 == 0) {
				offsetX = 0;
				offsetY+=147;
			}
			
			index++;
		}
	}
	
	private boolean filterCape(Cape cp) {
		
		if(!currentCategory.equals(CapeCategory.ALL) && !currentCategory.equals(cp.getCategory())) {
			return true;
		}
		
		if(!this.getSearchBox().getText().isEmpty() && !SearchUtils.isSimillar(cp.getName(), this.getSearchBox().getText())) {
			return true;
		}
		
		return false;
	}
}
