package cn.kuelcancel.kurrina.management.profile;

import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.utils.animation.ColorAnimation;
import cn.kuelcancel.kurrina.utils.animation.simple.SimpleAnimation;

public enum ProfileType {
	ALL(0, TranslateText.ALL), FAVORITE(1, TranslateText.FAVORITE);
	
	private int id;
	private TranslateText nameTranslate;
	private ColorAnimation textColorAnimation;
	private SimpleAnimation backgroundAnimation;
	
	private ProfileType(int id, TranslateText nameTranslate) {
		this.id = id;
		this.nameTranslate = nameTranslate;
		this.backgroundAnimation = new SimpleAnimation();
		this.textColorAnimation = new ColorAnimation();
	}

	public String getName() {
		return nameTranslate.getText();
	}
	
	public String getKey() {
		return nameTranslate.getKey();
	}
	
	public SimpleAnimation getBackgroundAnimation() {
		return backgroundAnimation;
	}

	public ColorAnimation getTextColorAnimation() {
		return textColorAnimation;
	}

	public int getId() {
		return id;
	}
	
	public static ProfileType getTypeById(int id) {
		
		for(ProfileType type : ProfileType.values()) {
			if(type.getId() == id) {
				return type;
			}
		}
		
		return ProfileType.ALL;
	}
}
