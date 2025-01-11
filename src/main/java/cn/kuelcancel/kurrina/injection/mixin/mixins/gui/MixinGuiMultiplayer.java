package cn.kuelcancel.kurrina.injection.mixin.mixins.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import cn.kuelcancel.kurrina.gui.GuiFixConnecting;
import cn.kuelcancel.kurrina.management.mods.impl.ViaVersionMod;
import cn.kuelcancel.kurrina.viaversion.ViaKurrina;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;

@Mixin(GuiMultiplayer.class)
public class MixinGuiMultiplayer extends GuiScreen {

	@Inject(method = "initGui", at = @At("TAIL"))
    public void preInitGui(CallbackInfo ci) {
		if(ViaVersionMod.getInstance().isToggled()) {
			this.buttonList.add(ViaKurrina.getInstance().getAsyncVersionSlider());
		}
	}
	
	@Overwrite
    private void connectToServer(ServerData server){
        mc.displayGuiScreen(new GuiFixConnecting(this, mc, server));
    }
}
