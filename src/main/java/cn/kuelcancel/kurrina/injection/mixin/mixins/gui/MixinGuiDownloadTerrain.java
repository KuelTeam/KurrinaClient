package cn.kuelcancel.kurrina.injection.mixin.mixins.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import cn.kuelcancel.kurrina.management.event.impl.EventLoadWorld;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiScreen;

@Mixin(GuiDownloadTerrain.class)
public class MixinGuiDownloadTerrain extends GuiScreen {

	@Overwrite
	public void initGui() {
		this.buttonList.clear();
		new EventLoadWorld().call();
	}
}
