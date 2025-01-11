package cn.kuelcancel.kurrina.injection.mixin.mixins.texture;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.management.event.impl.EventSwitchTexture;
import net.minecraft.client.renderer.texture.TextureMap;

@Mixin(TextureMap.class)
public class MixinTextureMap {

	@Inject(method = "loadTextureAtlas", at = @At("RETURN"))
	public void preLoadTextureAtlas(CallbackInfo ci) {
        if(Kurrina.getInstance().getEventManager() != null) {
            new EventSwitchTexture().call();
        }
	}
}
