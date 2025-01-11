package cn.kuelcancel.kurrina.management.account.microsoft;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.util.UUIDTypeAdapter;
import cn.kuelcancel.kurrina.utils.network.Http;
import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.KurrinaAPI;
import cn.kuelcancel.kurrina.injection.interfaces.IMixinMinecraft;
import cn.kuelcancel.kurrina.logger.KurrinaLogger;
import cn.kuelcancel.kurrina.management.account.Account;
import cn.kuelcancel.kurrina.management.account.AccountManager;
import cn.kuelcancel.kurrina.management.account.AccountType;
import cn.kuelcancel.kurrina.management.account.skin.SkinDownloader;
import cn.kuelcancel.kurrina.management.cape.CapeManager;
import cn.kuelcancel.kurrina.management.file.FileManager;
import cn.kuelcancel.kurrina.management.profile.mainmenu.BackgroundManager;
import cn.kuelcancel.kurrina.management.profile.mainmenu.impl.CustomBackground;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MicrosoftAuthentication {

    private Minecraft mc = Minecraft.getMinecraft();

    private SkinDownloader skinDownloader;

    public MicrosoftAuthentication() {
        skinDownloader = new SkinDownloader();
    }

    public void loginWithRefreshToken(String refreshToken) {
        // 构建请求参数
        Map<String, String> params = new HashMap<>();
        params.put("client_id", "d1ed1b72-9f7c-41bc-9702-365d2cbd2e38");
        params.put("grant_type", "refresh_token");
        params.put("refresh_token", refreshToken);

        JsonObject response = null;
        try {
            // 发送POST请求
            response = Http.gson().fromJson(Http.postURL("https://login.live.com/oauth20_token.srf", params), JsonObject.class);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // 检查响应是否包含access_token
        if (response != null && response.has("access_token")) {
            String accessToken = response.get("access_token").getAsString();
            // 继续处理access_token
            getXboxLiveToken(accessToken, refreshToken);
        } else {
            System.out.println("Failed to obtain access token");
        }

    }

    public void loginWithUrl(String url) {
        try {
            getMicrosoftToken(new URL(url));
        } catch (MalformedURLException e) {}
    }

    public void loginWithPopUpWindow(Runnable afterLogin) throws URISyntaxException, IOException {
        new MicrosoftLoginBrowser(afterLogin);
    }

    private void getMicrosoftToken(URL tokenURL) {
        String code = tokenURL.toString();

        code = code.substring(code.lastIndexOf('=') + 1);
        String token = "https://login.live.com/oauth20_token.srf";
        String oauth = null;
        Map<String, String> tokenParams = new HashMap<>();
        tokenParams.put("client_id", "d1ed1b72-9f7c-41bc-9702-365d2cbd2e38");
        tokenParams.put("code", code);
        tokenParams.put("grant_type", "authorization_code");
        tokenParams.put("redirect_uri", "http://127.0.0.1:17342");
        try {
            oauth = Http.postURL(token, tokenParams);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String accessToken = Http.gson().fromJson(oauth, JsonObject.class).get("access_token").getAsString();
        String refreshToken = Http.gson().fromJson(oauth, JsonObject.class).get("refresh_token").getAsString();
        getXboxLiveToken(accessToken, refreshToken);
    }

    private void getXboxLiveToken(String accessToken, String refreshToken) {
        JsonObject xbl = null;
        Map<String, Object> xblParams = new HashMap<>();
        Map<String, String> properties = new HashMap<>();
        properties.put("AuthMethod", "RPS");
        properties.put("SiteName", "user.auth.xboxlive.com");
        properties.put("RpsTicket", "d=" + accessToken);
        xblParams.put("Properties", properties);
        xblParams.put("RelyingParty", "http://auth.xboxlive.com");
        xblParams.put("TokenType", "JWT");
        try {
            xbl = Http.gson().fromJson(Http.postJSON("https://user.auth.xboxlive.com/user/authenticate", xblParams), JsonObject.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //        JsonObject response = HttpUtils.postJson("https://user.auth.xboxlive.com/user/authenticate", request);
        String xbl_token = xbl.get("Token").getAsString();

        getXSTS(xbl_token, refreshToken);
    }

    private void getXSTS(String xbl_token, String refreshToken) {

        JsonObject xsts = null;
        Map<String, Object> xstsParams = new HashMap<>();
        Map<String, Object> properties = new HashMap<>();
        properties.put("SandboxId", "RETAIL");
        properties.put("UserTokens", new String[]{xbl_token});
        xstsParams.put("Properties", properties);
        xstsParams.put("RelyingParty", "rp://api.minecraftservices.com/");
        xstsParams.put("TokenType", "JWT");
        try {
            xsts = Http.gson().fromJson(Http.postJSON("https://xsts.auth.xboxlive.com/xsts/authorize", xstsParams), JsonObject.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String xstsToken = xsts.get("Token").getAsString();
        String xstsUhs = xsts.get("DisplayClaims").getAsJsonObject().get("xui").getAsJsonArray().get(0).getAsJsonObject().get("uhs").getAsString();
        if (xsts.has("XErr")) {
            switch (xsts.get("XErr").getAsString()) {
                case "2148916233":
                    KurrinaLogger.error("This account doesn't have an Xbox account.");
                    break;
                case "2148916235":
                    KurrinaLogger.error("Xbox isn't available in your country.");
                    break;
                case "2148916238":
                    KurrinaLogger.error("The account is under 18 and must be added to a Family (https://start.ui.xboxlive.com/AddChildToFamily)");
                    break;
            }
        } else {

            getMinecraftToken(xstsUhs, xstsToken, refreshToken);
        }
    }

    private void getMinecraftToken(String xstsUhs, String xstsToken, String refreshToken) {
        JsonObject mcJson = null;
        Map<String, Object> loginParams = new HashMap<>();
        loginParams.put("identityToken", String.format("XBL3.0 x=%s;%s", xstsUhs, xstsToken));
        try {
            mcJson = Http.gson().fromJson(Http.postJSON("https://api.minecraftservices.com/authentication/login_with_xbox", loginParams), JsonObject.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        String mcToken = mcJson.get("access_token").getAsString();

        checkMinecraftOwnership(mcToken, refreshToken);
    }

    private void checkMinecraftOwnership(String mcToken, String refreshToken) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + mcToken);
        boolean ownsMinecraft = false;

        JsonObject response = null;
        try {
            response = Http.gson().fromJson(Http.get("https://api.minecraftservices.com/entitlements/mcstore", headers), JsonObject.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (response != null && response.has("items")) {
            for (JsonElement item : response.getAsJsonArray("items")) {
                String itemName = item.getAsJsonObject().get("name").getAsString();
                if ("product_minecraft".equals(itemName) || "game_minecraft".equals(itemName)) {
                    ownsMinecraft = true;
                    break;
                }
            }
        }


        if (!ownsMinecraft) {
            KurrinaLogger.error("User doesn't own Minecraft");
        } else {
            getMinecraftProfile(mcToken, refreshToken);
        }
    }

    private void getMinecraftProfile(String token, String refreshToken) {
        Kurrina instance = Kurrina.getInstance();
        AccountManager accountManager = instance.getAccountManager();
        FileManager fileManager = instance.getFileManager();
        File headDir = new File(fileManager.getCacheDir(), "head");
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);

        JsonObject response = null;
        try {
            response = Http.gson().fromJson(Http.get("https://api.minecraftservices.com/minecraft/profile", headers), JsonObject.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String name = response.get("name").getAsString();
        String uuid = response.get("id").getAsString();
        Account account = new Account(name, uuid, refreshToken, AccountType.MICROSOFT);

        if(!headDir.exists()) {
            fileManager.createDir(headDir);
        }

        skinDownloader.downloadFace(headDir, name, UUIDTypeAdapter.fromString(uuid));

        ((IMixinMinecraft) mc).setSession(new Session(name, uuid, token, "mojang"));

        if(accountManager.getAccountByName(account.getName()) == null) {
            accountManager.getAccounts().add(account);
        }

        accountManager.setCurrentAccount(account);

        check();
    }

    private void check() {
        Kurrina instance = Kurrina.getInstance();
        KurrinaAPI api = Kurrina.getInstance().getApi();
        CapeManager capeManager = instance.getCapeManager();
        BackgroundManager backgroundManager = instance.getProfileManager().getBackgroundManager();
        if(!api.isSpecialUser()) {

            if(capeManager.getCurrentCape().isPremium()) {
                capeManager.setCurrentCape(capeManager.getCapeByName("None"));
            }

            if(backgroundManager.getCurrentBackground() instanceof CustomBackground) {
                backgroundManager.setCurrentBackground(backgroundManager.getBackgroundById(0));
            }
        }
    }

    public SkinDownloader getSkinDownloader() {
        return skinDownloader;
    }
}
