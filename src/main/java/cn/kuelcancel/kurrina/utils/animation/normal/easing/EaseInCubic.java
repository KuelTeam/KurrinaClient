package cn.kuelcancel.kurrina.utils.animation.normal.easing;

import cn.kuelcancel.kurrina.utils.animation.normal.Animation;

public class EaseInCubic extends Animation {

	public EaseInCubic(int ms, double endPoint) {
		super(ms, endPoint);
		this.reset();
	}

	@Override
	protected double getEquation(double x) {
		
	    double x1 = x / duration;
	    
	    return x1 * x1 * x1;
	}
}
