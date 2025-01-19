package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;


public class MotionCameraMod extends Mod {
    public MotionCameraMod() {
        super(TranslateText.MOTION_CAMERA, TranslateText.MOTION_CAMERA_DESCRIPTION, ModCategory.RENDER);
    }

}
