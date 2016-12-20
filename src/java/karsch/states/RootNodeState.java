package karsch.states;

import com.jme3.app.state.AbstractAppState;
import com.jme3.scene.Node;

/**
 * Extended app state with a root node.
 * 
 * @since 12.08.2012
 * @author Stephan Dreyer
 */
public class RootNodeState extends AbstractAppState {
	protected Node rootNode;

	/**
	 * Instantiates a new root node state.
	 */
	public RootNodeState() {
		rootNode = new Node(this.getClass().getSimpleName() + " Root Node");
	}

	/**
	 * Gets the root node.
	 * 
	 * @return the root node
	 */
	public Node getRootNode() {
		return rootNode;
	}

	@Override
	public void update(final float tpf) {
		rootNode.updateLogicalState(tpf);
		rootNode.updateGeometricState();
	}

	@Override
	public void cleanup() {

	}

}
