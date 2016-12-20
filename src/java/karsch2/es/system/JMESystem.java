package karsch2.es.system;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

public abstract class JMESystem extends AbstractControl {

  @Override
  protected void controlRender(final RenderManager rm, final ViewPort vp) {
  }

  @Override
  protected final void controlUpdate(final float tpf) {
    process(tpf);
  }

  public abstract void process(float tpf);
}
