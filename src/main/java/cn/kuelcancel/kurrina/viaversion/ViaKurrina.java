package cn.kuelcancel.kurrina.viaversion;

import java.io.File;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.management.file.FileManager;
import cn.kuelcancel.kurrina.viaversion.gui.AsyncVersionSlider;

public class ViaKurrina {
	
    public final static int NATIVE_VERSION = 47;
    private static ViaKurrina instance;

    public static void create() {
    	instance = new ViaKurrina();
    }

    private AsyncVersionSlider asyncVersionSlider;

    public ViaKurrina() {
    	
    	FileManager fileManager = Kurrina.getInstance().getFileManager();
    	
        ViaLoadingBase.ViaLoadingBaseBuilder.create().runDirectory(new File(fileManager.getKurrinaDir(), "ViaVersion")).nativeVersion(NATIVE_VERSION).onProtocolReload(comparableProtocolVersion -> {
            if (getAsyncVersionSlider() != null) {
                getAsyncVersionSlider().setVersion(comparableProtocolVersion.getVersion());
            }
        }).build();
    }

    public static ViaKurrina getInstance() {
		return instance;
	}

	public void initAsyncSlider() {
        this.initAsyncSlider(5, 5, 110, 20);
    }

    public void initAsyncSlider(int x, int y, int width, int height) {
        asyncVersionSlider = new AsyncVersionSlider(-1, x, y, Math.max(width, 110), height);
    }

    public AsyncVersionSlider getAsyncVersionSlider() {
        return asyncVersionSlider;
    }
}
