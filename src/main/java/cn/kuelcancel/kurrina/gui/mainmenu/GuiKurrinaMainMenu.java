package cn.kuelcancel.kurrina.gui.mainmenu;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.gui.mainmenu.impl.*;
import cn.kuelcancel.kurrina.gui.mainmenu.impl.welcome.*;
import cn.kuelcancel.kurrina.management.account.Account;
import cn.kuelcancel.kurrina.management.account.AccountManager;
import cn.kuelcancel.kurrina.management.account.AccountType;
import cn.kuelcancel.kurrina.management.color.AccentColor;
import cn.kuelcancel.kurrina.management.color.palette.ColorPalette;
import cn.kuelcancel.kurrina.management.event.impl.EventRenderNotification;
import cn.kuelcancel.kurrina.management.nanovg.NanoVGManager;
import cn.kuelcancel.kurrina.management.nanovg.font.Fonts;
import cn.kuelcancel.kurrina.management.nanovg.font.Icon;
import cn.kuelcancel.kurrina.management.profile.mainmenu.impl.Background;
import cn.kuelcancel.kurrina.management.profile.mainmenu.impl.CustomBackground;
import cn.kuelcancel.kurrina.management.profile.mainmenu.impl.DefaultBackground;
import cn.kuelcancel.kurrina.utils.animation.normal.Animation;
import cn.kuelcancel.kurrina.utils.animation.normal.Direction;
import cn.kuelcancel.kurrina.utils.animation.normal.other.DecelerateAnimation;
import cn.kuelcancel.kurrina.utils.animation.simple.SimpleAnimation;
import cn.kuelcancel.kurrina.utils.mouse.MouseUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class GuiKurrinaMainMenu extends GuiScreen {

	private MainMenuScene currentScene;

	private SimpleAnimation closeFocusAnimation = new SimpleAnimation();
	private SimpleAnimation shopFocusAnimation = new SimpleAnimation();
	private SimpleAnimation backgroundSelectFocusAnimation = new SimpleAnimation();
	private SimpleAnimation[] backgroundAnimations = new SimpleAnimation[2];

	private boolean openAccount;
	private SimpleAnimation accountAnimation = new SimpleAnimation();

	private ArrayList<MainMenuScene> scenes = new ArrayList<MainMenuScene>();

	private Account removeAccount;

	private Animation fadeIconAnimation, fadeBackgroundAnimation;

	public GuiKurrinaMainMenu() {

		Kurrina instance = Kurrina.getInstance();

		for(int i = 0; i < backgroundAnimations.length; i++) {
			backgroundAnimations[i] = new SimpleAnimation();
		}

		scenes.add(new MainScene(this));
		scenes.add(new AccountScene(this));
		scenes.add(new BackgroundScene(this));
		scenes.add(new ShopScene(this));
		scenes.add(new WelcomeMessageScene(this));
		scenes.add(new ThemeSelectScene(this));
		scenes.add(new AccentColorSelectScene(this));
		scenes.add(new LoginMessageScene(this));
		scenes.add(new FirstLoginScene(this));
		scenes.add(new LastMessageScene(this));
		scenes.add(new CheckingDataScene(this));
		scenes.add(new MicrosoftLoginScene(this));

		if(instance.getApi().isFirstLogin()) {
			currentScene = getSceneByClass(WelcomeMessageScene.class);
		} else {
			if(instance.getAccountManager().getCurrentAccount() == null) {
				currentScene = getSceneByClass(AccountScene.class);
			} else {
				currentScene = getSceneByClass(MainScene.class);
			}
		}
	}

	@Override
	public void initGui() {
		currentScene.initGui();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		ScaledResolution sr = new ScaledResolution(mc);

		Kurrina instance = Kurrina.getInstance();
		NanoVGManager nvg = instance.getNanoVGManager();
		boolean isFirstLogin = instance.getApi().isFirstLogin();

		if(removeAccount != null) {
			instance.getAccountManager().getAccounts().remove(removeAccount);
			removeAccount = null;
		}

		backgroundAnimations[0].setAnimation(Mouse.getX(), 16);
		backgroundAnimations[1].setAnimation(Mouse.getY(), 16);

		nvg.setupAndDraw(() -> {

			drawNanoVG(sr, instance, nvg);

			if(!isFirstLogin) {
//				drawButtons(mouseX, mouseY, sr, nvg);
				drawButton(nvg, Icon.X, sr.getScaledWidth() - 28, 6, mouseX, mouseY, 22, 22, closeFocusAnimation);
				drawButton(nvg, Icon.IMAGE, sr.getScaledWidth() - 28 * 2, 6, mouseX, mouseY, 22, 22, backgroundSelectFocusAnimation);
				drawButton(nvg, Icon.SHOPPING, sr.getScaledWidth() - 28 * 3, 6, mouseX, mouseY, 22, 22, shopFocusAnimation);
			}
		});

		if(!isFirstLogin) {
			drawAccount(mouseX, mouseY, instance, nvg);
		}

		if(currentScene != null) {
			currentScene.drawScreen(mouseX, mouseY, partialTicks);
		}

		if(fadeBackgroundAnimation == null || (fadeBackgroundAnimation != null && !fadeBackgroundAnimation.isDone(Direction.FORWARDS))) {
			nvg.setupAndDraw(() -> drawSplashScreen(sr, nvg));
		}

		nvg.setupAndDraw(() -> {
			new EventRenderNotification().call();
		});

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	private void drawNanoVG(ScaledResolution sr, Kurrina instance, NanoVGManager nvg) {

		String copyright = "Copyright Mojang AB. Do not distribute!";
		Background currentBackground = instance.getProfileManager().getBackgroundManager().getCurrentBackground();

		if(currentBackground instanceof DefaultBackground) {

			DefaultBackground bg = (DefaultBackground) currentBackground;

			nvg.drawImage(bg.getImage(), -21 + backgroundAnimations[0].getValue() / 90, backgroundAnimations[1].getValue() * -1 / 90, sr.getScaledWidth() + 21, sr.getScaledHeight() + 20);
		}else if(currentBackground instanceof CustomBackground) {

			CustomBackground bg = (CustomBackground) currentBackground;

			nvg.drawImage(bg.getImage(), -21 + backgroundAnimations[0].getValue() / 90, backgroundAnimations[1].getValue() * -1 / 90, sr.getScaledWidth() + 21, sr.getScaledHeight() + 20);
		}

		nvg.drawText(copyright, sr.getScaledWidth() - (nvg.getTextWidth(copyright, 9, Fonts.REGULAR)) - 4, sr.getScaledHeight() - 12, new Color(255, 255, 255), 9, Fonts.REGULAR);
		nvg.drawText("Kurrina Client v" + instance.getVersion(), 4, sr.getScaledHeight() - 12, new Color(255, 255, 255), 9, Fonts.REGULAR);
	}

	private void drawButton(NanoVGManager nvg, String text, float x, float y, int mouseX, int mouseY, int width, int height, SimpleAnimation animation) {
		boolean isHovered = MouseUtils.isInside(mouseX, mouseY, x, y, width, height);
		Color backgroundColor = this.getBackgroundColor();
		AccentColor currentColor = Kurrina.getInstance().getColorManager().getCurrentColor();
		Color hoverColor = currentColor.getInterpolateColor(0);

		animation.setAnimation(isHovered ? 1.0F : 0.0F, 10);

		float animationValue = animation.getValue();
		int red = (int) (255 - animationValue * (255 - hoverColor.getRed()));
		int green = (int) (255 - animationValue * (255 - hoverColor.getGreen()));
		int blue = (int) (255 - animationValue * (255 - hoverColor.getBlue()));
		Color interpolatedColor = new Color(red, green, blue);

		if (isHovered) {
			nvg.save();
//			nvg.scissor(x - 90, y, width, height);
//			nvg.drawGradientCircle(mouseX, mouseY, radius, new Color(200, 200, 200), backgroundColor);
//			float fillWidth = animation.getValue() * width; // 动态调整填充宽度
//			nvg.drawRoundedRect(x - 90, y, fillWidth, height, 4.5F, Color.LIGHT_GRAY); // 从左到右填充
			nvg.drawRoundedRect(x, y, width, height, 4F, Color.LIGHT_GRAY);
			nvg.drawOutlineRoundedRect(x, y, width, height, 4F, 1F, interpolatedColor);
		}
		nvg.drawRoundedRect(x, y, width, height, 4F, backgroundColor);
		if(text.equals(Icon.X)) {
			nvg.drawCenteredText(text, x + 9F, y + 2F, interpolatedColor, 18F, Fonts.ICON);
		} else {
			nvg.drawCenteredText(text, x + 10F, y + 3F, interpolatedColor, 16F, Fonts.ICON);
		}
		nvg.restore();
	}

	private void drawButtons(int mouseX, int mouseY, ScaledResolution sr, NanoVGManager nvg) {

		closeFocusAnimation.setAnimation(MouseUtils.isInside(mouseX, mouseY, sr.getScaledWidth() - 28, 6, 22, 22) ? 1.0F : 0.0F, 16);

		nvg.drawRoundedRect(sr.getScaledWidth() - 28, 6, 22, 22, 4, this.getBackgroundColor());
		nvg.drawCenteredText(Icon.X, sr.getScaledWidth() - 19F, 8F, new Color(255, 255 - (int) (closeFocusAnimation.getValue() * 200), 255 - (int) (closeFocusAnimation.getValue() * 200)), 18, Fonts.ICON);

		backgroundSelectFocusAnimation.setAnimation(MouseUtils.isInside(mouseX, mouseY, sr.getScaledWidth() - 28 - 28, 6, 22, 22) ? 1.0F : 0.0F, 16);

		nvg.drawRoundedRect(sr.getScaledWidth() - 28 - 28, 6, 22, 22, 4, this.getBackgroundColor());
		nvg.drawCenteredText(Icon.IMAGE, sr.getScaledWidth() - 19F - 26.5F, 9.5F, new Color(255 - (int) (backgroundSelectFocusAnimation.getValue() * 200), 255, 255 - (int) (backgroundSelectFocusAnimation.getValue() * 200)), 15, Fonts.ICON);

		shopFocusAnimation.setAnimation(MouseUtils.isInside(mouseX, mouseY, sr.getScaledWidth() - (28 * 3), 6, 22, 22) ? 1.0F : 0.0F, 16);

		nvg.drawRoundedRect(sr.getScaledWidth() - (28 * 3), 6, 22, 22, 4, this.getBackgroundColor());
		nvg.drawCenteredText(Icon.SHOPPING, sr.getScaledWidth() - (26 * 3) + 4.5F, 9.5F, new Color(255 - (int) (shopFocusAnimation.getValue() * 200), 255, 255), 15, Fonts.ICON);
	}

	private void drawAccount(int mouseX, int mouseY, Kurrina instance, NanoVGManager nvg) {

		Account currentAccount = instance.getAccountManager().getCurrentAccount();

		nvg.setupAndDraw(() -> drawAccountNanoVG(mouseX, mouseY, instance, nvg, currentAccount));
	}

	private void drawAccountNanoVG(int mouseX, int mouseY, Kurrina instance, NanoVGManager nvg, Account currentAccount) {

		AccountManager accountManager = instance.getAccountManager();

		if(accountManager.getCurrentAccount() != null) {
			File headFile = new File(instance.getFileManager().getCacheDir(), "head/" + accountManager.getCurrentAccount().getName() + ".png");
			String name = currentAccount.getName();
			ColorPalette palette = instance.getColorManager().getPalette();

			float maxUserWidth = nvg.getTextWidth(name, 9.5F, Fonts.REGULAR);
			float progress = accountAnimation.getValue();
			int size = accountManager.getAccounts().size() - 1;
			int offsetY = 20;

			for(Account acc : accountManager.getAccounts()) {

				float tWidth = nvg.getTextWidth(acc.getName(), 9.5F, Fonts.REGULAR);

				if(tWidth > maxUserWidth) {
					maxUserWidth = tWidth;
				}
			}

			boolean isInsideAccount = MouseUtils.isInside(mouseX, mouseY, 6, 6, 20, 20);

			if (openAccount) {
				isInsideAccount = true;
			}

			if (MouseUtils.isInside(mouseX, mouseY, 6, 6, 20 + maxUserWidth + 18, 20)) {
				openAccount = isInsideAccount;
			} else {
				openAccount = isInsideAccount && MouseUtils.isInside(mouseX, mouseY, 6, 6, 20 + maxUserWidth + 18, 20 + (size * 20));
			}

			accountAnimation.setAnimation(openAccount ? 1.0F : 0F, 16);

			nvg.drawRoundedRect(6, 6, 20 + (progress * (maxUserWidth + 18)), 20 + (progress * 20 * size), 4, this.getBackgroundColor());

			if(!headFile.exists()) {
				nvg.drawPlayerHead(new ResourceLocation("textures/entity/steve.png"), 9, 9, 14, 14, 2);
			}else {
				nvg.drawRoundedImage(headFile, 9, 9, 14, 14, 2);
			}

			nvg.save();
			nvg.translate(-18 + (progress * 18), 0);
			nvg.drawText(name, 26, 13, new Color(255, 255, 255, (int) (progress * 255)), 9.5F, Fonts.REGULAR);
			nvg.drawText(Icon.PLUS, maxUserWidth + 29, 10F, new Color(255, 255, 255, (int) (progress * 255)), 13F, Fonts.ICON);
			nvg.restore();

			for(Account acc : accountManager.getAccounts()) {

				if(!acc.equals(currentAccount)) {

					headFile = new File(instance.getFileManager().getCacheDir(), "head/" + acc.getName() + ".png");

					nvg.save();
					nvg.translate(0, -18 + (progress * 18));

					if(!headFile.exists()) {
						nvg.drawPlayerHead(new ResourceLocation("textures/entity/steve.png"), 9, 9 + offsetY, 14, 14, 2, accountAnimation.getValue());
					}else {
						nvg.drawRoundedImage(headFile, 9, 9 + offsetY, 14, 14, 2, accountAnimation.getValue());
					}

					nvg.restore();

					nvg.save();
					nvg.translate(-18 + (progress * 18), -18 + (progress * 18));

					nvg.drawText(acc.getName(), 26, 13 + offsetY, new Color(255, 255, 255, (int) (progress * 255)), 9.5F, Fonts.REGULAR);
					nvg.drawText(Icon.TRASH, maxUserWidth + 30, 11F + offsetY, palette.getMaterialRed((int) (progress * 255)), 10F, Fonts.ICON);

					nvg.restore();

					offsetY+=20;
				}
			}
		}
	}

	private void drawSplashScreen(ScaledResolution sr, NanoVGManager nvg) {

		if(fadeIconAnimation == null) {
			fadeIconAnimation = new DecelerateAnimation(1000, 1);
			fadeIconAnimation.setDirection(Direction.FORWARDS);
			fadeIconAnimation.reset();
		}

		if(fadeIconAnimation != null) {

			if(fadeIconAnimation.isDone(Direction.FORWARDS) && fadeBackgroundAnimation == null) {
				fadeBackgroundAnimation = new DecelerateAnimation(1000, 1);
				fadeBackgroundAnimation.setDirection(Direction.FORWARDS);
				fadeBackgroundAnimation.reset();
			}

			nvg.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0, 0, 0, fadeBackgroundAnimation != null ? (int) (255 - (fadeBackgroundAnimation.getValue() * 255)) : 255));
			nvg.drawCenteredText(Icon.Kurrina, sr.getScaledWidth() / 2, (sr.getScaledHeight() / 2) - (nvg.getTextHeight(Icon.Kurrina, 130, Fonts.ICON) / 2) - 1, new Color(255, 255, 255, (int) (255 - (fadeIconAnimation.getValue() * 255))), 130, Fonts.ICON);
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

		ScaledResolution sr = new ScaledResolution(mc);

		Kurrina instance = Kurrina.getInstance();
		NanoVGManager nvg = instance.getNanoVGManager();
		AccountManager accountManager = instance.getAccountManager();
		boolean isFirstLogin = instance.getApi().isFirstLogin();

		if(mouseButton == 0 && !isFirstLogin) {

			if(MouseUtils.isInside(mouseX, mouseY, sr.getScaledWidth() - 28, 6, 22, 22)) {
				mc.shutdown();
			}

			if(MouseUtils.isInside(mouseX, mouseY, sr.getScaledWidth() - 28 - 28, 6, 22, 22)) {
				this.setCurrentScene(this.getSceneByClass(BackgroundScene.class));
			}

			if(MouseUtils.isInside(mouseX, mouseY, sr.getScaledWidth() - (28 * 3), 6, 22, 22)) {
				this.setCurrentScene(this.getSceneByClass(ShopScene.class));
			}

			if(openAccount) {

				Account currentAccount = accountManager.getCurrentAccount();
				float maxUserWidth = nvg.getTextWidth(currentAccount.getName(), 9.5F, Fonts.REGULAR);
				int offsetY = 20;

				for(Account acc : accountManager.getAccounts()) {

					float tWidth = nvg.getTextWidth(acc.getName(), 9.5F, Fonts.REGULAR);

					if(tWidth > maxUserWidth) {
						maxUserWidth = tWidth;
					}
				}

				if(MouseUtils.isInside(mouseX, mouseY, maxUserWidth + 28, 9, 15, 15)) {
					currentScene = getSceneByClass(AccountScene.class);
				}

				for(Account acc : accountManager.getAccounts()) {

					if(!acc.equals(currentAccount)) {

						if(MouseUtils.isInside(mouseX, mouseY, maxUserWidth + 28, 8 + offsetY, 15, 15)) {
							removeAccount = acc;
						}

						if(MouseUtils.isInside(mouseX, mouseY, 6, 6 + offsetY, maxUserWidth + 20, 20)) {
							if (acc.getType() == AccountType.MICROSOFT) {
								accountManager.getAuthenticator().loginWithRefreshToken(acc.getRefreshToken());
							} else {
								accountManager.setCurrentAccount(acc);
							}
						}

						offsetY+=20;
					}
				}
			}
		}


		try {
			currentScene.mouseClicked(mouseX, mouseY, mouseButton);
			super.mouseClicked(mouseX, mouseY, mouseButton);
		} catch (IOException | URISyntaxException e) {}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		currentScene.mouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {
		currentScene.keyTyped(typedChar, keyCode);
	}

	@Override
	public void handleInput() throws IOException {

		if(currentScene instanceof MicrosoftLoginScene) {
			currentScene.handleInput();
		} else {
			super.handleInput();
		}
	}

	@Override
	public void onGuiClosed() {
		currentScene.onGuiClosed();
	}

	public MainMenuScene getCurrentScene() {
		return currentScene;
	}

	public void setCurrentScene(MainMenuScene currentScene) {

		if(this.currentScene != null) {
			this.currentScene.onSceneClosed();
		}

		this.currentScene = currentScene;

		if(this.currentScene != null) {
			this.currentScene.initScene();
		}
	}

	public boolean isDoneBackgroundAnimation() {
		return fadeBackgroundAnimation != null && fadeBackgroundAnimation.isDone(Direction.FORWARDS);
	}

	public MainMenuScene getSceneByClass(Class<? extends MainMenuScene > clazz) {

		for(MainMenuScene s : scenes) {
			if(s.getClass().equals(clazz)) {
				return s;
			}
		}

		return null;
	}

	public Color getBackgroundColor() {
		return new Color(230, 230, 230, 120);
	}
}