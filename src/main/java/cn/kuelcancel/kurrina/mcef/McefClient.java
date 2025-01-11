package cn.kuelcancel.kurrina.mcef;

import java.util.ArrayList;
import java.util.List;

import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefContextMenuParams;
import org.cef.callback.CefMenuModel;
import org.cef.handler.CefContextMenuHandler;
import org.cef.handler.CefDisplayHandler;
import org.cef.handler.CefLoadHandler;
import org.cef.network.CefRequest;

public class McefClient implements CefLoadHandler, CefContextMenuHandler, CefDisplayHandler {
	
    private final CefClient handle;
    private final List<CefLoadHandler> loadHandlers = new ArrayList<>();
    private final List<CefContextMenuHandler> contextMenuHandlers = new ArrayList<>();
    private final List<CefDisplayHandler> displayHandlers = new ArrayList<>();
    
    public McefClient(CefClient cefClient) {
    	this.handle = cefClient;
    	cefClient.addLoadHandler(this);
    	cefClient.addContextMenuHandler(this);
    	cefClient.addDisplayHandler(this);
    }
    
    public CefClient getHandle() {
        return handle;
    }

    public void addLoadHandler(CefLoadHandler handler) {
        loadHandlers.add(handler);
    }

    @Override
    public void onLoadingStateChange(CefBrowser browser, boolean isLoading, boolean canGoBack, boolean canGoForward) {
        for (CefLoadHandler loadHandler : loadHandlers) {
            loadHandler.onLoadingStateChange(browser, isLoading, canGoBack, canGoForward);
        }
    }

    @Override
    public void onLoadStart(CefBrowser browser, CefFrame frame, CefRequest.TransitionType transitionType) {
        for (CefLoadHandler loadHandler : loadHandlers) {
        	loadHandler.onLoadStart(browser, frame, transitionType);
        }
    }

    @Override
    public void onLoadEnd(CefBrowser browser, CefFrame frame, int httpStatusCode) {
        for (CefLoadHandler loadHandler : loadHandlers) {
        	loadHandler.onLoadEnd(browser, frame, httpStatusCode);
        }
    }

    @Override
    public void onLoadError(CefBrowser browser, CefFrame frame, ErrorCode errorCode, String errorText, String failedUrl) {
        for (CefLoadHandler loadHandler : loadHandlers) {
            loadHandler.onLoadError(browser, frame, errorCode, errorText, failedUrl);
        }
    }

    public void addContextMenuHandler(CefContextMenuHandler handler) {
        contextMenuHandlers.add(handler);
    }

    @Override
    public void onBeforeContextMenu(CefBrowser browser, CefFrame frame, CefContextMenuParams params, CefMenuModel model) {
        for (CefContextMenuHandler contextMenuHandler : contextMenuHandlers) {
            contextMenuHandler.onBeforeContextMenu(browser, frame, params, model);
        }
    }

    @Override
    public boolean onContextMenuCommand(CefBrowser browser, CefFrame frame, CefContextMenuParams params, int commandId, int eventFlags) {
    	
        for (CefContextMenuHandler contextMenuHandler : contextMenuHandlers) {
            if (contextMenuHandler.onContextMenuCommand(browser, frame, params, commandId, eventFlags)) {
            	return true;
            }
        }
        
        return false;
    }

    @Override
    public void onContextMenuDismissed(CefBrowser browser, CefFrame frame) {
        for (CefContextMenuHandler contextMenuHandler : contextMenuHandlers) {
            contextMenuHandler.onContextMenuDismissed(browser, frame);
        }
    }

    public void addDisplayHandler(CefDisplayHandler handler) {
        displayHandlers.add(handler);
    }

    @Override
    public void onAddressChange(CefBrowser browser, CefFrame frame, String url) {
        for (CefDisplayHandler displayHandler : displayHandlers) {
        	displayHandler.onAddressChange(browser, frame, url);
        }
    }

    @Override
    public void onTitleChange(CefBrowser browser, String title) {
        for (CefDisplayHandler displayHandler : displayHandlers) {
        	displayHandler.onTitleChange(browser, title);
        }
    }

    @Override
    public boolean onTooltip(CefBrowser browser, String text) {
    	
        for (CefDisplayHandler displayHandler : displayHandlers) {
            if (displayHandler.onTooltip(browser, text)) {
            	return true;
            }
        }
        
        return false;
    }

    @Override
    public void onStatusMessage(CefBrowser browser, String value) {
        for (CefDisplayHandler displayHandler : displayHandlers) {
        	displayHandler.onStatusMessage(browser, value);
        }
    }

    @Override
    public boolean onConsoleMessage(CefBrowser browser, CefSettings.LogSeverity level, String message, String source, int line) {
    	
        for (CefDisplayHandler displayHandler : displayHandlers) {
            if (displayHandler.onConsoleMessage(browser, level, message, source, line)) {
            	return true;
            }
        }
        
        return false;
    }

    @Override
    public boolean onCursorChange(CefBrowser browser, int cursorType) {
    	
        for (CefDisplayHandler displayHandler : displayHandlers) {
            if (displayHandler.onCursorChange(browser, cursorType)) {
            	return true;
            }
        }
        
        return false;
    }
}
