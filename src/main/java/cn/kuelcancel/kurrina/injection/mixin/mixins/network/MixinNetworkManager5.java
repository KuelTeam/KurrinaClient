package cn.kuelcancel.kurrina.injection.mixin.mixins.network;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.connection.UserConnectionImpl;
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;
import cn.kuelcancel.kurrina.management.mods.impl.ViaVersionMod;
import cn.kuelcancel.kurrina.viaversion.MCPVLBPipeline;
import cn.kuelcancel.kurrina.viaversion.ViaLoadingBase;
import cn.kuelcancel.kurrina.viaversion.ViaKurrina;

@Mixin(targets = "net.minecraft.network.NetworkManager$5")
public class MixinNetworkManager5 {

    @Inject(method = "initChannel", at = @At(value = "TAIL"), remap = false)
    private void onInitChannel(Channel channel, CallbackInfo ci) {
    	
    	if (ViaVersionMod.getInstance().isToggled() && ViaVersionMod.getInstance().isLoaded() && channel instanceof SocketChannel && 
    			ViaLoadingBase.getInstance().getTargetVersion().getVersion() != ViaKurrina.NATIVE_VERSION) {
    	    final UserConnection user = new UserConnectionImpl(channel, true);
    	    new ProtocolPipelineImpl(user);
    	    
    	    channel.pipeline().addLast(new MCPVLBPipeline(user));
    	}
    }
}
