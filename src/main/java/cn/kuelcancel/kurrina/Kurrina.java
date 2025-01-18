package cn.kuelcancel.kurrina;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import cn.kuelcancel.kurrina.injection.mixin.KurrinaTweaker;
import cn.kuelcancel.kurrina.logger.KurrinaLogger;
import cn.kuelcancel.kurrina.management.account.AccountManager;
import cn.kuelcancel.kurrina.management.cape.CapeManager;
import cn.kuelcancel.kurrina.management.changelog.ChangelogManager;
import cn.kuelcancel.kurrina.management.color.ColorManager;
import cn.kuelcancel.kurrina.management.command.CommandManager;
import cn.kuelcancel.kurrina.management.download.DownloadManager;
import cn.kuelcancel.kurrina.management.event.EventManager;
import cn.kuelcancel.kurrina.management.file.FileManager;
import cn.kuelcancel.kurrina.management.language.LanguageManager;
import cn.kuelcancel.kurrina.management.mods.ModManager;
import cn.kuelcancel.kurrina.management.mods.impl.GlobalSettingsMod;
import cn.kuelcancel.kurrina.management.music.MusicManager;
import cn.kuelcancel.kurrina.management.nanovg.NanoVGManager;
import cn.kuelcancel.kurrina.management.notification.NotificationManager;
import cn.kuelcancel.kurrina.management.profile.ProfileManager;
import cn.kuelcancel.kurrina.management.quickplay.QuickPlayManager;
import cn.kuelcancel.kurrina.management.screenshot.ScreenshotManager;
import cn.kuelcancel.kurrina.management.security.SecurityFeatureManager;
import cn.kuelcancel.kurrina.management.waypoint.WaypointManager;
import cn.kuelcancel.kurrina.utils.OptifineUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class Kurrina {

	private static Kurrina instance = new Kurrina();
	private Minecraft mc = Minecraft.getMinecraft();
	private KurrinaAPI api;
	
	private String name, version;
	
	private NanoVGManager nanoVGManager;
	private FileManager fileManager;
	private DownloadManager downloadManager;
	private LanguageManager languageManager;
	private AccountManager accountManager;
	private EventManager eventManager;
	private ModManager modManager;
	private CapeManager capeManager;
	private ColorManager colorManager;
	private ProfileManager profileManager;
	private MusicManager musicManager;
	private CommandManager commandManager;
	private ScreenshotManager screenshotManager;
	private NotificationManager notificationManager;
	private SecurityFeatureManager securityFeatureManager;
	private QuickPlayManager quickPlayManager;
	private ChangelogManager changelogManager;
	private WaypointManager waypointManager;
	
	public Kurrina() {
		name = "Kurrina";
		version = "1.5";
	}
	
	public void start() {
		
		OptifineUtils.disableFastRender();
		this.removeOptifineZoom();
		
		fileManager = new FileManager();
		downloadManager = new DownloadManager();
		languageManager = new LanguageManager();
		accountManager = new AccountManager();
		eventManager = new EventManager();
		modManager = new ModManager();
		
		modManager.init();
		
		capeManager = new CapeManager();
		colorManager = new ColorManager();
		profileManager = new ProfileManager();
		musicManager = new MusicManager();
		
		api = new KurrinaAPI();
		api.init();
		
		commandManager = new CommandManager();
		screenshotManager = new ScreenshotManager();
		notificationManager = new NotificationManager();
		securityFeatureManager = new SecurityFeatureManager();
		quickPlayManager = new QuickPlayManager();
		changelogManager = new ChangelogManager();
		waypointManager = new WaypointManager();
		
		eventManager.register(new KurrinaHandler());
		
		setupLibraryPath();
		
		GlobalSettingsMod.getInstance().setToggled(true);
		mc.updateDisplay();
	}
	
	public void stop() {
		profileManager.save();
		accountManager.save();
	}
	
	private void removeOptifineZoom() {
		if(KurrinaTweaker.hasOptifine) {
			try {
				this.unregisterKeybind((KeyBinding) GameSettings.class.getField("ofKeyBindZoom").get(mc.gameSettings));
			} catch(Exception e) {
				KurrinaLogger.error("Failed to unregister zoom key", e);
			}
		}
	}
	
    private void unregisterKeybind(KeyBinding key) {
        if (Arrays.asList(mc.gameSettings.keyBindings).contains(key)) {
            mc.gameSettings.keyBindings = ArrayUtils.remove(mc.gameSettings.keyBindings, Arrays.asList(mc.gameSettings.keyBindings).indexOf(key));
            key.setKeyCode(0);
        }
    }
	
    private void setupLibraryPath() {
    	
    	File cefDir = new File(fileManager.getExternalDir(), "cef");
    	
        try {
			System.setProperty("jcef.path", cefDir.getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
	public static Kurrina getInstance() {
		return instance;
	}

	public KurrinaAPI getApi() {
		return api;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public FileManager getFileManager() {
		return fileManager;
	}

	public DownloadManager getDownloadManager() {
		return downloadManager;
	}

	public ModManager getModManager() {
		return modManager;
	}

	public LanguageManager getLanguageManager() {
		return languageManager;
	}

	public EventManager getEventManager() {
		return eventManager;
	}

	public NanoVGManager getNanoVGManager() {
		return nanoVGManager;
	}

	public ColorManager getColorManager() {
		return colorManager;
	}

	public ProfileManager getProfileManager() {
		return profileManager;
	}

	public MusicManager getMusicManager() {
		return musicManager;
	}

	public AccountManager getAccountManager() {
		return accountManager;
	}

	public CapeManager getCapeManager() {
		return capeManager;
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}

	public ScreenshotManager getScreenshotManager() {
		return screenshotManager;
	}

	public void setNanoVGManager(NanoVGManager nanoVGManager) {
		this.nanoVGManager = nanoVGManager;
	}

	public NotificationManager getNotificationManager() {
		return notificationManager;
	}

	public SecurityFeatureManager getSecurityFeatureManager() {
		return securityFeatureManager;
	}

	public QuickPlayManager getQuickPlayManager() {
		return quickPlayManager;
	}

	public ChangelogManager getChangelogManager() {
		return changelogManager;
	}

	public WaypointManager getWaypointManager() {
		return waypointManager;
	}
}
