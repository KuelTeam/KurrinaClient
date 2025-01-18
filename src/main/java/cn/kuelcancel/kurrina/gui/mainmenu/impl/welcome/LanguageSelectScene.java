package cn.kuelcancel.kurrina.gui.mainmenu.impl.welcome;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.gui.mainmenu.GuiKurrinaMainMenu;
import cn.kuelcancel.kurrina.gui.mainmenu.MainMenuScene;
import cn.kuelcancel.kurrina.management.color.AccentColor;
import cn.kuelcancel.kurrina.management.language.Language;
import cn.kuelcancel.kurrina.management.language.LanguageManager;
import cn.kuelcancel.kurrina.management.nanovg.NanoVGManager;
import cn.kuelcancel.kurrina.management.nanovg.font.Fonts;
import cn.kuelcancel.kurrina.utils.animation.normal.Animation;
import cn.kuelcancel.kurrina.utils.animation.normal.Direction;
import cn.kuelcancel.kurrina.utils.animation.normal.other.DecelerateAnimation;
import cn.kuelcancel.kurrina.utils.buffer.ScreenAlpha;
import cn.kuelcancel.kurrina.utils.mouse.MouseUtils;
import cn.kuelcancel.kurrina.utils.render.BlurUtils;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public class LanguageSelectScene extends MainMenuScene {

    private int x, y, width, height;

    private Animation fadeAnimation;
    private ScreenAlpha screenAlpha = new ScreenAlpha();
    private LanguageManager languageManager = Kurrina.getInstance().getLanguageManager();
    private Language currentLanguage = languageManager.getCurrentLanguage();

    public LanguageSelectScene(GuiKurrinaMainMenu parent) {
        super(parent);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        ScaledResolution sr = new ScaledResolution(mc);

        // Adjust the background size to fit just the language options
        width = 220;  // Reduced width to fit two language options
        height = 146;
        x = sr.getScaledWidth() / 2 - (width / 2);
        y = sr.getScaledHeight() / 2 - (height / 2);

        if (fadeAnimation == null) {
            fadeAnimation = new DecelerateAnimation(800, 1);
            fadeAnimation.setDirection(Direction.FORWARDS);
            fadeAnimation.reset();
        }

        BlurUtils.drawBlurScreen(14);

        screenAlpha.wrap(() -> drawNanoVG(), fadeAnimation.getValueFloat());

        if (fadeAnimation.isDone(Direction.BACKWARDS)) {
            this.setCurrentScene(this.getSceneByClass(ThemeSelectScene.class));
        }
    }

    private void drawNanoVG() {

        Kurrina instance = Kurrina.getInstance();
        NanoVGManager nvg = instance.getNanoVGManager();
        AccentColor currentColor = instance.getColorManager().getCurrentColor();

        int offsetX = 0;

        // Draw the background rectangle with a smaller width to avoid excess space
        nvg.drawRoundedRect(x, y, width, height, 8, this.getBackgroundColor());
        nvg.drawCenteredText("Choose a Language", x + (width / 2), y + 10, Color.WHITE, 16, Fonts.MEDIUM);
        nvg.drawRect(x, y + 27, width, 1, Color.WHITE);

        // Only display the two languages (Chinese and English)
        for (Language lang : new Language[]{Language.CHINESE, Language.ENGLISH}) {
            nvg.drawRoundedImage(lang.getFlag(), x + offsetX + 14, y + 42, 90, 56, 4);
            nvg.drawCenteredText(lang.getName(), x + offsetX + 14 + (90 / 2), y + 104, Color.WHITE, 7F, Fonts.REGULAR);
            if (lang == currentLanguage) {
                nvg.drawGradientOutlineRoundedRect(x + offsetX + 14, y + 42, 90, 56, 6, 2, currentColor.getColor1(), currentColor.getColor2());
            }
            offsetX += 102;  // Shift the next flag and name horizontally
        }

        // Draw the 'Next' button without extra space at the bottom
        nvg.drawRoundedRect(x + width - 86, y + height - 26, 80, 20, 6, this.getBackgroundColor());
        nvg.drawCenteredText("Next", x + width - 86 + (80 / 2), y + height - 20, Color.WHITE, 10, Fonts.REGULAR);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        // Check which language the user clicked
        int offsetX = 0;
        for (Language lang : new Language[]{Language.CHINESE, Language.ENGLISH}) {
            if (MouseUtils.isInside(mouseX, mouseY, x + offsetX + 14, y + 42, 90, 56) && mouseButton == 0) {
                currentLanguage = lang;
            }
            offsetX += 102;
        }

        // If the user clicked the "Next" button, apply the language change
        if (MouseUtils.isInside(mouseX, mouseY, x + width - 86, y + height - 26, 80, 20) && mouseButton == 0) {
            Kurrina.getInstance().getLanguageManager().setCurrentLanguage(currentLanguage);
            fadeAnimation.setDirection(Direction.BACKWARDS);
        }
    }
}
