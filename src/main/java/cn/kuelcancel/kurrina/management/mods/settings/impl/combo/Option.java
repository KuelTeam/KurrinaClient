package cn.kuelcancel.kurrina.management.mods.settings.impl.combo;

import cn.kuelcancel.kurrina.management.language.TranslateText;

public class Option {

	private TranslateText nameTranslate;
	
	public Option(TranslateText nameTranslate) {
		this.nameTranslate = nameTranslate;
	}

	public String getName() {
		return nameTranslate.getText();
	}

	public String getNameKey() {
		return nameTranslate.getKey();
	}
	
	public TranslateText getTranslate() {
		return nameTranslate;
	}
}
