package cn.kuelcancel.kurrina.utils.animation.normal.easing;

import cn.kuelcancel.kurrina.utils.animation.normal.Animation;

public class EaseOutCubic extends Animation {

	public EaseOutCubic(int ms, double endPoint) {
		super(ms, endPoint);
	}

	@Override
	protected double getEquation(double x) {
		
	    double x1 = x / duration;
	    x1--;
	    
	    return x1 * x1 * x1 + 1;
	}
}
