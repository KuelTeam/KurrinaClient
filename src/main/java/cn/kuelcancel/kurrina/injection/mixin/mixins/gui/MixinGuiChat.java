package cn.kuelcancel.kurrina.injection.mixin.mixins.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import cn.kuelcancel.kurrina.hooks.GuiChatHook;
import cn.kuelcancel.kurrina.management.mods.impl.ChatTranslateMod;
import cn.kuelcancel.kurrina.utils.Multithreading;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

@Mixin(GuiChat.class)
public class MixinGuiChat extends GuiScreen {

	@Shadow
	protected GuiTextField inputField;;
	
	@Inject(method = "drawScreen", at = @At("TAIL"))
	public void postDrawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		if(ChatTranslateMod.getInstance().isToggled()) {
			GuiChatHook.drawScreen(mouseX, mouseY, partialTicks);
		}
	}
	
	@Inject(method = "mouseClicked", at = @At("HEAD"))
	public void preMouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
		if(ChatTranslateMod.getInstance().isToggled()) {
			GuiChatHook.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}
	
	@Redirect(method = "keyTyped", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiChat;sendChatMessage(Ljava/lang/String;)V"))
	public void cancelSendMessage() {
		
        String s = this.inputField.getText().trim();
        
        if (s.length() > 0) {
        	
    		if(ChatTranslateMod.getInstance().isToggled() && GuiChatHook.isToggled()) {
    			Multithreading.runAsync(() -> {
    				GuiChatHook.sendTranslatedMessage(s);
    			});
    		} else {
    			this.sendChatMessage(s);
    		}
        }
	}
}
