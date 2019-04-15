import java.util.ArrayList;
import java.util.Collection;

public class RecursiveHashCalculaor {

	/**
	 * This function takes as input, the Inode that is being edited. This function
	 * will then begin recursively calculating the hashes for all of the nodes
	 * related to the one passed in.
	 * 
	 * @param node The node being edited
	 * @return The new hash value for the root node
	 */
	public static float calcultateHash(Inode node) {
		return recursivelyCalculateHash(node.getParent());
	}

	/**
	 * This function recursively calculates the hash for all of the nodes related to
	 * the one passed in and sets the hash of each node.
	 * 
	 * @param node The immediate parent of the node being edited
	 * @return The new hash value for the root node
	 */
	private static float recursivelyCalculateHash(Inode node) {
		System.out.print("node: " + node.getId());
		float nodeHash;
		ArrayList<Inode> todo = new ArrayList<>(node.getChildren());
		Inode child;
		ArrayList<Float> hashes = new ArrayList<>();
		hashes.add((float) node.getId());

		// iterate over all children of this node
		while (!todo.isEmpty()) {
			for (int i = 0; i < todo.size(); i++) {
				child = todo.get(i);

				if (child.isBeingEdited()) {
					System.out.println(" 1");
					// TODO in multithreading we'd wait for this
					child.setBeingEdited(false);

					nodeHash = calculateFileHash(child);
					child.setHash(nodeHash);
					child.setOldHash(false);
					hashes.add(nodeHash);

					todo.remove(i);
					i--;
				} else if (child.hasOldHash()) {
					System.out.println(" 2");
					// TODO in multithreading we'd wait for this
					// child has an out of date hash, must recursively update downward

					recursivelyCalculateHash(child);
				} else if (!child.hasOldHash()){
					System.out.println(" 3");
					// child's hash has not changed

					hashes.add(child.getHash());
					todo.remove(i);
					i--;
				}
			}
		}

		nodeHash = calculateHash(hashes);
		node.setHash(nodeHash);
		node.setOldHash(false);

		if (node.getParent() != null) {
			return recursivelyCalculateHash(node.getParent());
		}

		return nodeHash;
	}

	public static float calculateFileHash(Inode file) {
		if (file.getId() == 3) {
			return 100;
		}
		return (float) file.getId();
	}

	/**
	 * TODO Calculates the hash for the Inode based on the collection of information
	 * passed in.
	 * 
	 * @param node
	 * @return
	 */
	private static float calculateHash(Collection<Float> nodeInfo) {
		ArrayList<Float> asList = new ArrayList<Float>(nodeInfo);
		float hash = 0;
		
		for (Float f : asList) {
			System.out.print(" + " + f);
			hash += f;
		}
		System.out.println();
		return hash;
	}
}
