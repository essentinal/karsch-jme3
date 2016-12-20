package test.performance;

import java.util.Random;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.BatchNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.util.TangentBinormalGenerator;

public class TestBoxelSpam extends SimpleApplication {
	private BatchNode boxes;

	final int xsize = 92;
	final int ysize = 92;

	public TestBoxelSpam() {

	}

	@Override
	public void simpleInitApp() {

		for (int x = 0; x < xsize; x++) {
			for (int y = 0; y < ysize; y++) {
				addBox(x, y);
			}
		}

		PointLight pl = new PointLight();
		pl.setColor(new ColorRGBA(1f, 1f, 0.8f, 1f));
		pl.setRadius(xsize / 2f);
		pl.setPosition(new Vector3f(xsize / 2f, 5f, ysize / 2f));
		rootNode.addLight(pl);

		pl = new PointLight();
		pl.setColor(ColorRGBA.Blue);
		pl.setRadius(xsize / 2f);
		pl.setPosition(new Vector3f(xsize / -2f, -5f, ysize / -2f));
		rootNode.addLight(pl);

		final AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White);
		rootNode.addLight(al);

		flyCam.setMoveSpeed(10);
		cam.setLocation(cam.getLocation().add(0f, xsize / 2f, 0f));
		cam.lookAt(new Vector3f(xsize / 2f, 1f, ysize / 2f), Vector3f.UNIT_Y);
	}

	private final Random rnd = new Random();

	private void addBox(final int x, final int y) {

		final Box b = new Box(0.5f, 2f, 0.5f);
		final Geometry geo = new Geometry("box" + x + "," + y, b);

		TangentBinormalGenerator.generate(geo);

		final Material mat = assetManager
				.loadMaterial("Textures/BumpMapTest/SimpleBump.j3m");
		// mat.setFloat("Shininess", rnd.nextFloat() * 10);
		geo.setMaterial(mat);

		geo.setLocalTranslation(
				x,
				((rnd.nextFloat() - FastMath.sin(x / 3f) - FastMath.sin(y / 3f)) / 1f) - 2f,
				y);

		if (boxes == null) {
			boxes = new BatchNode();
		} else if (boxes.getChildren().size() > xsize * ysize / 20) {
			boxes.batch();
			rootNode.attachChild(boxes);
			boxes = new BatchNode();
		}

		boxes.attachChild(geo);
	}

	public static void main(final String[] args) {
		new TestBoxelSpam().start();
	}
}
