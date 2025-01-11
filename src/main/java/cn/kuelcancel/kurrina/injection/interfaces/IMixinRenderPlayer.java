package cn.kuelcancel.kurrina.injection.interfaces;

import cn.kuelcancel.kurrina.management.mods.impl.skin3d.layers.BodyLayerFeatureRenderer;
import cn.kuelcancel.kurrina.management.mods.impl.skin3d.layers.HeadLayerFeatureRenderer;

public interface IMixinRenderPlayer {
	public boolean hasThinArms();
	public HeadLayerFeatureRenderer getHeadLayer();
	public BodyLayerFeatureRenderer getBodyLayer();
}