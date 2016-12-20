package karsch.controller;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

// convenience class for jme3 
public abstract class TimedControl extends AbstractControl {

	private float minTime;
	private float maxTime;
	private float speed;

	public void setMaxTime(final float maxTime) {
		this.maxTime = maxTime;
	}

	public void setMinTime(final float minTime) {
		this.minTime = minTime;
	}

	public float getMaxTime() {
		return maxTime;
	}

	public float getMinTime() {
		return minTime;
	}

	public void setSpeed(final float speed) {
		this.speed = speed;
	}

	public float getSpeed() {
		return speed;
	}

	@Override
	public final void update(final float tpf) {
		super.update(tpf);
	}

	@Override
	protected void controlRender(final RenderManager rm, final ViewPort vp) {

	}
}
