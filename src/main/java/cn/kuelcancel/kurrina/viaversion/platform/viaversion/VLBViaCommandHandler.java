package cn.kuelcancel.kurrina.viaversion.platform.viaversion;

import com.viaversion.viaversion.commands.ViaCommandHandler;

import cn.kuelcancel.kurrina.viaversion.command.impl.LeakDetectSubCommand;

public class VLBViaCommandHandler extends ViaCommandHandler {

    public VLBViaCommandHandler() {
        super();
        this.registerVLBDefaults();
    }

    public void registerVLBDefaults() {
        this.registerSubCommand(new LeakDetectSubCommand());
    }
}
