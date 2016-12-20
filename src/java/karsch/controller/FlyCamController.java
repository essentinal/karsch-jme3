package karsch.controller;

import karsch.Values;

import com.jme3.animation.LoopMode;
import com.jme3.app.Application;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.input.ChaseCamera;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl.ControlDirection;

public class FlyCamController {
	private final Application app;

	private final CameraNode camnode;
	private final ChaseCamera chaser;
	private final MotionEvent cameraMotionControl;

	public FlyCamController(final Application app, final MotionPath path,
			final Camera cam, final Camera camToSet, final Node rootNode,
			final Node target) {
		this.app = app;
		camnode = new CameraNode("camnode", cam);
		camnode.setControlDir(ControlDirection.SpatialToCamera);
		camnode.setEnabled(false);

		// DisplaySystem.getDisplaySystem().getRenderer().setCamera(cam);

		cameraMotionControl = new MotionEvent(camnode, path);
		cameraMotionControl.setLoopMode(LoopMode.DontLoop);
		cameraMotionControl
				.setLookAt(target.getWorldTranslation(), Vector3f.UNIT_Y);
		cameraMotionControl.setDirectionType(MotionEvent.Direction.LookAt);

		rootNode.attachChild(camnode);

		chaser = new ChaseCamera(cam, target);
		chaser.setSmoothMotion(true);
		chaser.setMaxDistance(50);
		chaser.setDefaultDistance(50);

		path.addListener(new MotionPathListener() {

			@Override
			public void onWayPointReach(final MotionEvent control,
					final int wayPointIndex) {
				if (path.getNbWayPoints() == wayPointIndex + 1) {
					System.out.println("flycamcontroller done");
					app.getRenderManager().setCamera(camToSet, false);
					rootNode.detachChild(camnode);

					// TODO this must be triggered by a listener
					// Values.getInstance().getHudPass().setEnabled(true);

					Values.getInstance().getLevelGameState().startLevel();

				}
			}
		});

		// TODO this must be done by a listener
		// Values.getInstance().getHudPass().setEnabled(false);
	}

	public void start() {
		cameraMotionControl.play();
	}

}
