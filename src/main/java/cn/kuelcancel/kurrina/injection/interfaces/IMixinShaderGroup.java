package cn.kuelcancel.kurrina.injection.interfaces;

import java.util.List;

import net.minecraft.client.shader.Shader;

public interface IMixinShaderGroup {
	List<Shader> getListShaders();
}
