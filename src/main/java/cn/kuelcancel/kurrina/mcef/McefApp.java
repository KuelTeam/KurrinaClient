package cn.kuelcancel.kurrina.mcef;

import org.cef.CefApp;

public class McefApp {

	private final CefApp handle;
	
	public McefApp(CefApp handle) {
		this.handle = handle;
	}
	
	public CefApp getHandle() {
		return handle;
	}
}
