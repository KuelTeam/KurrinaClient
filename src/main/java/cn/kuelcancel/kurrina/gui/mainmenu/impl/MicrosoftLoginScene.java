package cn.kuelcancel.kurrina.gui.mainmenu.impl;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.gui.mainmenu.GuiKurrinaMainMenu;
import cn.kuelcancel.kurrina.gui.mainmenu.MainMenuScene;
import cn.kuelcancel.kurrina.gui.mainmenu.impl.welcome.FirstLoginScene;
import cn.kuelcancel.kurrina.gui.mainmenu.impl.welcome.LastMessageScene;
import cn.kuelcancel.kurrina.management.nanovg.NanoVGManager;
import cn.kuelcancel.kurrina.mcef.Mcef;
import cn.kuelcancel.kurrina.mcef.McefBrowser;
import cn.kuelcancel.kurrina.utils.Multithreading;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.net.URI;

public class MicrosoftLoginScene extends MainMenuScene {

    private McefBrowser browser;

    public MicrosoftLoginScene(GuiKurrinaMainMenu parent) {
        super(parent);
    }

    @Override
    public void initGui() {
        if(browser != null) {
            resizeBrowser();
        }
    }

    @Override
    public void initScene() {
        try {
            String url = "https://login.live.com/oauth20_authorize.srf? m,.client_id=d1ed1b72-9f7c-41bc-9702-365d2cbd2e38&response_type=code&redirect_uri=http://127.0.0.1:17342&scope=XboxLive.signin%20offline_access&prompt=login";
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        NanoVGManager nvg = Kurrina.getInstance().getNanoVGManager();

        Mcef.getApp().getHandle().N_DoMessageLoopWork();

        nvg.setupAndDraw(() -> drawNanoVG(mouseX, mouseY, partialTicks));

        if(browser != null) {

            if(browser.getURL().contains("The%20user%20has%20denied%20access%20to%20the%20scope%20requested")) {
                backToAccountScene();
                return;
            }

            if(browser.getURL().contains("https://login.live.com/oauth20_desktop.srf?code")) {

                Multithreading.runAsync(() -> {
                    Kurrina.getInstance().getAccountManager().getAuthenticator().loginWithUrl(browser.getURL());
                    Kurrina.getInstance().getAccountManager().save();
                });

                if(Kurrina.getInstance().getApi().isFirstLogin()) {
                    this.setCurrentScene(this.getSceneByClass(LastMessageScene.class));
                } else {
                    this.setCurrentScene(this.getSceneByClass(MainScene.class));
                }
            }
        }
    }

    private void drawNanoVG(int mouseX, int mouseY, float partialTicks) {

        int width = this.getParent().width;
        int height = this.getParent().height;

        Kurrina instance = Kurrina.getInstance();
        NanoVGManager nvg = instance.getNanoVGManager();

        if(browser != null) {
            nvg.drawImage(browser.getTexture(), 0, 0, width, height);
        }
    }

    @Override
    public void handleInput() {

        int width = this.getParent().width;
        int height = this.getParent().height;

        while(Keyboard.next()) {

            boolean pressed = Keyboard.getEventKeyState();
            char key = Keyboard.getEventCharacter();
            int num = Keyboard.getEventKey();

            if(browser != null) {

                if(pressed) {
                    browser.sendKeyPressed(num, key, 0);
                } else {
                    browser.sendKeyReleased(num, key, 0);
                }

                if(key != 0) {
                    browser.sendKeyTyped(key, 0);
                }
            }

            if(pressed) {
                keyTyped(key, num);
            }
        }

        while(Mouse.next()) {

            int button = Mouse.getEventButton();
            boolean pressed = Mouse.getEventButtonState();
            int sx = Mouse.getEventX();
            int sy = Mouse.getEventY();
            int wheel = Mouse.getEventDWheel();

            if(browser != null) {

                int y = mc.displayHeight - sy;

                if(wheel != 0) {
                    browser.sendMouseWheel(sx, y, 0, 1, wheel);
                } else if(button == -1) {
                    browser.sendMouseMove(sx, y, 0, y < 0);
                } else {
                    browser.sendMouseButton(sx, y, 0, button + 1, pressed, 1);
                }
            }

            if(pressed) {

                int x = sx * width / mc.displayWidth;
                int y = height - (sy * height / mc.displayHeight) - 1;

                try {
                    mouseClicked(x, y, button);
                } catch(Exception e) {}
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

        if(keyCode == Keyboard.KEY_ESCAPE) {
            backToAccountScene();
        }
    }

    @Override
    public void onSceneClosed() {
        browser.close();
        browser = null;
    }

    private void resizeBrowser() {
        if(this.getParent().width > 100 && this.getParent().height > 100) {
            browser.resize(mc.displayWidth, mc.displayHeight);
        }
    }

    private void backToAccountScene() {
        this.setCurrentScene(Kurrina.getInstance().getApi().isFirstLogin() ? this.getSceneByClass(FirstLoginScene.class)
                : this.getSceneByClass(AccountScene.class));
    }
}
