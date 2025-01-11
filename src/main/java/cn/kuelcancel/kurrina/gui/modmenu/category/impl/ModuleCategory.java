package cn.kuelcancel.kurrina.gui.modmenu.category.impl;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.gui.modmenu.GuiModMenu;
import cn.kuelcancel.kurrina.gui.modmenu.category.Category;
import cn.kuelcancel.kurrina.management.color.AccentColor;
import cn.kuelcancel.kurrina.management.color.ColorManager;
import cn.kuelcancel.kurrina.management.color.palette.ColorPalette;
import cn.kuelcancel.kurrina.management.color.palette.ColorType;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import cn.kuelcancel.kurrina.management.mods.ModManager;
import cn.kuelcancel.kurrina.management.mods.settings.Setting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.BooleanSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.ColorSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.ComboSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.ImageSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.KeybindSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.NumberSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.SoundSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.TextSetting;
import cn.kuelcancel.kurrina.management.nanovg.NanoVGManager;
import cn.kuelcancel.kurrina.management.nanovg.font.Fonts;
import cn.kuelcancel.kurrina.management.nanovg.font.Icon;
import cn.kuelcancel.kurrina.ui.comp.Comp;
import cn.kuelcancel.kurrina.ui.comp.impl.CompColorPicker;
import cn.kuelcancel.kurrina.ui.comp.impl.CompComboBox;
import cn.kuelcancel.kurrina.ui.comp.impl.CompImageSelect;
import cn.kuelcancel.kurrina.ui.comp.impl.CompKeybind;
import cn.kuelcancel.kurrina.ui.comp.impl.CompSlider;
import cn.kuelcancel.kurrina.ui.comp.impl.CompSoundSelect;
import cn.kuelcancel.kurrina.ui.comp.impl.CompToggleButton;
import cn.kuelcancel.kurrina.ui.comp.impl.field.CompModTextBox;
import cn.kuelcancel.kurrina.utils.ColorUtils;
import cn.kuelcancel.kurrina.utils.MathUtils;
import cn.kuelcancel.kurrina.utils.SearchUtils;
import cn.kuelcancel.kurrina.utils.animation.normal.Animation;
import cn.kuelcancel.kurrina.utils.animation.normal.Direction;
import cn.kuelcancel.kurrina.utils.animation.normal.other.SmoothStepAnimation;
import cn.kuelcancel.kurrina.utils.animation.simple.SimpleAnimation;
import cn.kuelcancel.kurrina.utils.mouse.MouseUtils;
import cn.kuelcancel.kurrina.utils.mouse.Scroll;

public class ModuleCategory extends Category {

	private ModCategory currentCategory;
	
	private Scroll settingScroll = new Scroll();
	private boolean openSetting;
	private Animation settingAnimation;
	private Mod currentMod;
	
	private ArrayList<ModuleSetting> comps = new ArrayList<ModuleSetting>();
	
	public ModuleCategory(GuiModMenu parent) {
		super(parent, TranslateText.MODULE, Icon.ARCHIVE, true);
	}
	
	@Override
	public void initGui() {
		currentCategory = ModCategory.ALL;
		openSetting = false;
		settingAnimation = new SmoothStepAnimation(260, 1.0);
		settingAnimation.setValue(1.0);
	}

	@Override
	public void initCategory() {
		scroll.resetAll();
		openSetting = false;
		settingAnimation = new SmoothStepAnimation(260, 1.0);
		settingAnimation.setValue(1.0);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		Kurrina instance = Kurrina.getInstance();
		NanoVGManager nvg = instance.getNanoVGManager();
		ModManager modManager = instance.getModManager();
		ColorManager colorManager = instance.getColorManager();
		ColorPalette palette = colorManager.getPalette();
		AccentColor accentColor = colorManager.getCurrentColor();
		
		int offsetX = 0;
		float offsetY = 13;
		int index = 1;
		float scrollValue = scroll.getValue();
		
		settingAnimation.setDirection(openSetting ? Direction.BACKWARDS : Direction.FORWARDS);
		
		if(settingAnimation.isDone(Direction.FORWARDS)) {
			this.setCanClose(true);
			currentMod = null;
		}
		
		nvg.save();
		nvg.translate((float) -(600 - (settingAnimation.getValue() * 600)), 0);
		
		//Draw mod scene
		
		nvg.save();
		nvg.translate(0, scrollValue);
		
		for(ModCategory c : ModCategory.values()) {
			
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
		
		offsetY = offsetY + 23;
		
		for(Mod m : modManager.getMods()) {
			
			if(filterMod(m)) {
				continue;
			}
			
			if(offsetY + scrollValue + 45 > 0 && offsetY + scrollValue < this.getHeight()) {
				
				nvg.drawRoundedRect(this.getX() + 15, this.getY() + offsetY, this.getWidth() - 30, 40, 8, palette.getBackgroundColor(ColorType.DARK));
				nvg.drawRoundedRect(this.getX() + 21, this.getY() + offsetY + 6, 28, 28, 6, palette.getBackgroundColor(ColorType.NORMAL));
				nvg.drawText(m.getName(), this.getX() + 56, this.getY() + offsetY + 15F, palette.getFontColor(ColorType.DARK), 13, Fonts.MEDIUM);
				nvg.drawText(m.getDescription(), this.getX() + 56 + (nvg.getTextWidth(m.getName(), 13, Fonts.MEDIUM)) + 5, this.getY() + offsetY + 16, palette.getFontColor(ColorType.NORMAL), 9, Fonts.REGULAR);
				
				m.getAnimation().setAnimation(m.isToggled() ? 1.0F : 0.0F, 16);
				
				nvg.save();
				nvg.scale(this.getX() + 21, this.getY() + offsetY + 6, 28, 28, m.getAnimation().getValue());
				
				nvg.drawGradientRoundedRect(this.getX() + 21, this.getY() + offsetY + 6, 28, 28, 6, ColorUtils.applyAlpha(accentColor.getColor1(), (int) (m.getAnimation().getValue() * 255)), ColorUtils.applyAlpha(accentColor.getColor2(), (int) (m.getAnimation().getValue() * 255)));
				
				nvg.restore();
				
				if(modManager.getSettingsByMod(m) != null) {
					nvg.drawText(Icon.SETTINGS, this.getX() + this.getWidth() - 39, this.getY() + offsetY + 13.5F, palette.getFontColor(ColorType.NORMAL), 13, Fonts.ICON);
				}
			}
			
			index++;
			offsetY+=50;
		}
		
		nvg.restore();
		nvg.restore();
		
		//Draw mod setting scene
		
		nvg.save();
		nvg.translate((float) (settingAnimation.getValue() * 600), 0);
		
		if(currentMod != null) {
			
			int setIndex = 0;
			
			if(MouseUtils.isInside(mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getHeight())) {
				settingScroll.onScroll();
				settingScroll.onAnimation();
			}
			
			offsetY = 15;
			offsetX = 0;
			
			nvg.save();
			
			nvg.drawRoundedRect(this.getX() + 15, this.getY() + offsetY, this.getWidth() - 30, this.getHeight() - 30, 10, palette.getBackgroundColor(ColorType.DARK));
			nvg.drawRect(this.getX() + 15, this.getY() + offsetY + 27, this.getWidth() - 30, 1, palette.getBackgroundColor(ColorType.NORMAL));
			nvg.drawText(currentMod.getName(), this.getX() + 26, this.getY() + offsetY + 9, palette.getFontColor(ColorType.DARK), 13, Fonts.MEDIUM);
			nvg.drawText(Icon.REFRESH, this.getX() + this.getWidth() - 39, this.getY() + offsetY + 7.5F, palette.getFontColor(ColorType.DARK), 13, Fonts.ICON);
			
			offsetY = 44;
			
			nvg.scissor(this.getX() + 15, this.getY() + offsetY, this.getWidth() - 30, this.getHeight() - 59);
			nvg.translate(0, settingScroll.getValue());
			
			for(ModuleSetting s : comps) {
				
				s.openAnimation.setAnimation(s.openY, 16);
				
				nvg.drawText(s.setting.getName(), this.getX() + offsetX + 26, this.getY() + offsetY + 15F + s.openAnimation.getValue(), palette.getFontColor(ColorType.DARK), 10, Fonts.MEDIUM);
				
				if(s.comp instanceof CompToggleButton) {
					
					CompToggleButton toggleButton = (CompToggleButton) s.comp;
					
					toggleButton.setX(this.getX() + offsetX + 168);
					toggleButton.setY(this.getY() + offsetY + 12 + s.openAnimation.getValue());
					toggleButton.setScale(0.85F);
				}
				
				if(s.comp instanceof CompSlider) {
					
					CompSlider slider = (CompSlider) s.comp;
					
					slider.setX(this.getX() + offsetX + 122);
					slider.setY(this.getY() + offsetY + 17 + s.openAnimation.getValue());
					slider.setWidth(75);
				}
				
				if(s.comp instanceof CompComboBox) {
					
					CompComboBox comboBox = (CompComboBox) s.comp;
					
					comboBox.setX(this.getX() + offsetX + 122);
					comboBox.setY(this.getY() + offsetY + 11 + s.openAnimation.getValue());
				}
				
				if(s.comp instanceof CompKeybind) {
					
					CompKeybind keybind = (CompKeybind) s.comp;
					
					keybind.setX(this.getX() + offsetX + 122);
					keybind.setY(this.getY() + offsetY + 11 + s.openAnimation.getValue());
				}
				
				if(s.comp instanceof CompModTextBox) {
					
					CompModTextBox textBox = (CompModTextBox) s.comp;
					
					textBox.setX(this.getX() + offsetX + 122);
					textBox.setY(this.getY() + offsetY + 11 + s.openAnimation.getValue());
					textBox.setWidth(75);
					textBox.setHeight(16);
				}
				
				if(s.comp instanceof CompColorPicker) {
					
					CompColorPicker picker = (CompColorPicker) s.comp;
					
					picker.setX(this.getX() + offsetX + 98);
					picker.setY(this.getY() + offsetY + 12.5F + s.openAnimation.getValue());
					picker.setScale(0.8F);
				}
				
				if(s.comp instanceof CompImageSelect) {
					
					CompImageSelect imageSelect = (CompImageSelect) s.comp;
					
					imageSelect.setX(this.getX() + offsetX + 181);
					imageSelect.setY(this.getY() + offsetY + 11 + s.openAnimation.getValue());
				}
				
				if(s.comp instanceof CompSoundSelect) {
					
					CompSoundSelect soundSelect = (CompSoundSelect) s.comp;
					
					soundSelect.setX(this.getX() + offsetX + 181);
					soundSelect.setY(this.getY() + offsetY + 11 + s.openAnimation.getValue());
				}
				
				s.comp.draw(mouseX, (int) (mouseY - settingScroll.getValue()), partialTicks);
				
				offsetX+=194;
				setIndex++;
				
				if(setIndex % 2 == 0) {
					offsetY+=29;
					offsetX = 0;
				}
			}
			
			nvg.restore();
			
			settingScroll.setMaxScroll(this.getModuleSettingHeight());
		}
		
		nvg.restore();
		
		scroll.setMaxScroll((index - (index > 5 ? 5.18F : index)) * 50);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		
		Kurrina instance = Kurrina.getInstance();
		NanoVGManager nvg = instance.getNanoVGManager();
		ModManager modManager = instance.getModManager();
		
		int offsetX = 0;
		float offsetY = 13 + scroll.getValue();
		
		if(!openSetting) {
			for(ModCategory c : ModCategory.values()) {
				
				float textWidth = nvg.getTextWidth(c.getName(), 9, Fonts.MEDIUM);
				
				if(MouseUtils.isInside(mouseX, mouseY, this.getX() + 15 + offsetX, this.getY() + offsetY - 3, textWidth + 20, 16) && mouseButton == 0) {
					currentCategory = c;
				}
				
				offsetX+=textWidth + 28;
			}
			
			offsetY = offsetY + 23;
			
			for(Mod m : modManager.getMods()) {
				
				if(filterMod(m)) {
					continue;
				}
				
				if(MouseUtils.isInside(mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getHeight()) && mouseButton == 0) {
					if(MouseUtils.isInside(mouseX, mouseY, this.getX() + 15, this.getY() + offsetY, this.getWidth() - 60, 40)) {
						m.toggle();
					}
					
					if(MouseUtils.isInside(mouseX, mouseY, this.getX() + this.getWidth() - 44, this.getY() + offsetY + 9, 22, 22) && !openSetting) {
						
						ArrayList<Setting> settings = modManager.getSettingsByMod(m);
						int setIndex = 0;
						
						offsetX = 0;
						offsetY = 44;
						
						if(settings != null) {
							
							comps.clear();
							
							for(Setting s : settings) {
								
								if(s instanceof BooleanSetting) {
									
									BooleanSetting bSetting = (BooleanSetting) s;
									
									CompToggleButton toggleButton = new CompToggleButton(bSetting);
									
									toggleButton.setX(this.getX() + offsetX + 168);
									toggleButton.setY(this.getY() + offsetY + 8);
									toggleButton.setScale(0.85F);
									
									comps.add(new ModuleSetting(s, toggleButton));
								}
								
								if(s instanceof NumberSetting) {
									
									NumberSetting nSetting = (NumberSetting)s;
									
									CompSlider slider = new CompSlider(nSetting);
									
									slider.setX(this.getX() + offsetX + 122);
									slider.setY(this.getY() + offsetY + 13);
									slider.setWidth(75);
									
									comps.add(new ModuleSetting(s, slider));
								}
								
								if(s instanceof ComboSetting) {
									
									ComboSetting cSetting = (ComboSetting) s;
									
									CompComboBox comboBox = new CompComboBox(75, cSetting);
									
									comboBox.setX(this.getX() + offsetX + 122);
									comboBox.setY(this.getY() + offsetY + 11);
									
									comps.add(new ModuleSetting(s, comboBox));
								}
								
								if(s instanceof ImageSetting) {
									
									ImageSetting iSetting = (ImageSetting) s;
									CompImageSelect imageSelect = new CompImageSelect(iSetting);
									
									imageSelect.setX(this.getX() + offsetX + 181);
									imageSelect.setY(this.getY() + offsetY + 11);
									
									comps.add(new ModuleSetting(s, imageSelect));
								}
								
								if(s instanceof SoundSetting) {
									
									SoundSetting sSetting = (SoundSetting) s;
									CompSoundSelect soundSelect = new CompSoundSelect(sSetting);
									
									soundSelect.setX(this.getX() + offsetX + 181);
									soundSelect.setY(this.getY() + offsetY + 11);
									
									comps.add(new ModuleSetting(s, soundSelect));
								}
								
								if(s instanceof KeybindSetting) {
									
									KeybindSetting kSetting = (KeybindSetting) s;
									CompKeybind keybind = new CompKeybind(75, kSetting);
									
									keybind.setX(this.getX() + offsetX + 122);
									keybind.setY(this.getY() + offsetY + 7);
									
									comps.add(new ModuleSetting(s, keybind));
								}
								
								if(s instanceof TextSetting) {
									
									TextSetting tSetting = (TextSetting) s;
									
									CompModTextBox textBox = new CompModTextBox(tSetting);
									
									textBox.setX(this.getX() + offsetX + 122);
									textBox.setY(this.getY() + offsetY + 7);
									textBox.setWidth(75);
									textBox.setHeight(16);
									
									comps.add(new ModuleSetting(s, textBox));
								}
								
								if(s instanceof ColorSetting) {
									
									ColorSetting cSetting = (ColorSetting) s;
									CompColorPicker picker = new CompColorPicker(cSetting);
									
									picker.setX(this.getX() + offsetX + 98);
									picker.setY(this.getY() + offsetY + 8.5F);
									picker.setScale(0.8F);
									
									comps.add(new ModuleSetting(s, picker));
								}
								
								offsetX+=194;
								setIndex++;
								
								if(setIndex % 2 == 0) {
									offsetY+=29;
									offsetX = 0;
								}
							}
							
							settingScroll.resetAll();
							currentMod = m;
							openSetting = true;
							this.setCanClose(false);
						}
					}
				}
				
				offsetY+=50;
			}
		}
		
		if(openSetting && settingAnimation.isDone(Direction.BACKWARDS)) {
			
			for(ModuleSetting s: comps) {
				
				if(MouseUtils.isInside(mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getHeight()) && mouseButton == 0) {
					
					s.comp.mouseClicked(mouseX, (int) (mouseY - settingScroll.getValue()), mouseButton);
					
					if(s.comp instanceof CompColorPicker) {
						
						CompColorPicker picker = (CompColorPicker) s.comp;
						int openIndex = 1;
						
						if(!picker.isInsideOpen(mouseX, (int) (mouseY - settingScroll.getValue()))) {
							continue;
						}
						
						for(int i = 0; i < comps.size(); i++) {
							
							if((openIndex * 2) + (comps.indexOf(s)) < comps.size()) {
								
								ModuleSetting s2 = comps.get((openIndex * 2) + (comps.indexOf(s)));
								int add = picker.isShowAlpha() ? 100 : 85;
								
								s2.openY+= picker.isOpen() ? add : -add;
							}
							
							openIndex++;
						}
					}
				}
			}
			
			if(MouseUtils.isInside(mouseX, mouseY, this.getX() + this.getWidth() - 41, this.getY() + 15 + 6F, 16, 16) && mouseButton == 0) {
				
				for(ModuleSetting s : comps) {
					s.setting.reset();
				}
			}
		}
		
		if(openSetting && mouseButton == 3) {
			openSetting = false;
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		
		for(ModuleSetting s : comps) {
			
			if(MouseUtils.isInside(mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getHeight()) && mouseButton == 0) {
				s.comp.mouseReleased(mouseX, mouseY, mouseButton);
			}
		}
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) {
		
		boolean binding = false;
		
		for(ModuleSetting s : comps) {
			
			if(s.comp instanceof CompKeybind) {
				
				CompKeybind keybind = (CompKeybind) s.comp;
				
				if(keybind.isBinding()) {
					binding = true;
				}
			}
			
			s.comp.keyTyped(typedChar, keyCode);
		}
		
		if(binding) {
			return;
		}
		
		if(openSetting && keyCode == Keyboard.KEY_ESCAPE && !binding) {
			openSetting = false;
		}
	}
	
	private boolean filterMod(Mod m) {
		
		if(m.isHide()) {
			return true;
		}
		
		if(!currentCategory.equals(ModCategory.ALL) && !m.getCategory().equals(currentCategory)) {
			return true;
		}
		
		if(!this.getSearchBox().getText().isEmpty() && !SearchUtils.isSimillar(Kurrina.getInstance().getModManager().getWords(m), this.getSearchBox().getText())) {
			return true;
		}
		
		return false;
	}
	
	private int getModuleSettingHeight() {
		
	    int oddOutput = 0;
	    int evenOutput = 0;
	    int oddTotal = 0;
	    int evenTotal = 0;

	    for (int i = 0; i < comps.size(); i++) {
	    	
	        if (MathUtils.isOdd(i + 1)) {
	            oddOutput += 29;
	        } else {
	            evenOutput += 29;
	        }

	        ModuleSetting s = comps.get(i);
	        if (s.comp instanceof CompColorPicker) {
	            CompColorPicker picker = (CompColorPicker) s.comp;
	            if (picker.isOpen()) {
	                int add = picker.isShowAlpha() ? 100 : 85;
	                if (MathUtils.isOdd(i + 1)) {
	                    oddTotal += add;
	                } else {
	                    evenTotal += add;
	                }
	            }
	        }
	    }

	    int output = Math.max(oddOutput, evenOutput) + Math.max(oddTotal, evenTotal);
	    
	    return Math.max(0, output - (this.getHeight() - 72));
	}
	
	private class ModuleSetting {
		
		private SimpleAnimation openAnimation = new SimpleAnimation();
		
		private Setting setting;
		private Comp comp;
		private float openY;
		
		public ModuleSetting(Setting setting, Comp comp) {
			this.setting = setting;
			this.comp = comp;
			this.openY = 0;
		}
	}
}
