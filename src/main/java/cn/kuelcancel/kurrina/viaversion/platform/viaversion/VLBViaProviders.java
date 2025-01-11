package cn.kuelcancel.kurrina.viaversion.platform.viaversion;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.platform.ViaPlatformLoader;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.protocol.version.VersionProvider;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;

import cn.kuelcancel.kurrina.viaversion.ViaLoadingBase;
import cn.kuelcancel.kurrina.viaversion.platform.providers.VLBMovementTransmitterProvider;
import cn.kuelcancel.kurrina.viaversion.provider.VLBBaseVersionProvider;

public class VLBViaProviders implements ViaPlatformLoader {

    @Override
    public void load() {
    	
        final ViaProviders providers = Via.getManager().getProviders();
        providers.use(VersionProvider.class, new VLBBaseVersionProvider());
        providers.use(MovementTransmitterProvider.class, new VLBMovementTransmitterProvider());

        if (ViaLoadingBase.getInstance().getProviders() != null) {
        	ViaLoadingBase.getInstance().getProviders().accept(providers);
        }
    }

    @Override
    public void unload() {}
}
