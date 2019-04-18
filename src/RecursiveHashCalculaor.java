import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
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
	public static String calcultateHash(Inode node) {
		return recursivelyCalculateHash(node.getParent());
	}

	/**
	 * This function recursively calculates the hash for all of the nodes related to
	 * the one passed in and sets the hash of each node.
	 * 
	 * @param node The immediate parent of the node being edited
	 * @return The new hash value for the root node
	 */
	private static String recursivelyCalculateHash(Inode node) {
		System.out.print("node: " + node.getId());
		File nodeFile = node.getFile();

		if (nodeFile.isFile()) {
			// if this is just a file, return our calculated hash
			return calculateFileHash(node);
		}

		String nodeHash;
		ArrayList<Inode> todo = new ArrayList<>(node.getChildren());
		ArrayList<String> hashes = new ArrayList<>();
		Inode child;

		// add directory metadata before adding children hashes
		hashes.add(nodeFile.getAbsolutePath());
		hashes.add(Long.toString(nodeFile.lastModified()));
		hashes.add(Boolean.toString(nodeFile.canExecute()));
		hashes.add(Boolean.toString(nodeFile.canRead()));
		hashes.add(Boolean.toString(nodeFile.canWrite()));

		// iterate over all children of this node
		while (!todo.isEmpty()) {
			for (int i = 0; i < todo.size(); i++) {
				child = todo.get(i);

				if (child.isBeingEdited()) {
					System.out.println(" Option 1");
					// TODO in multithreading we'd wait for this
					child.setBeingEdited(false);

					nodeHash = calculateFileHash(child);
					child.setHash(nodeHash);
					child.setOldHash(false);
					hashes.add(nodeHash);

					todo.remove(i);
					i--;
				} else if (child.hasOldHash()) {
					System.out.println(" Option 2");
					// TODO in multithreading we'd wait for this
					// child has an out of date hash, must recursively update downward

					recursivelyCalculateHash(child);
				} else if (!child.hasOldHash()) {
					System.out.println(" Option 3");
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

	public static String calculateGeneralHash(Inode node) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			File file = node.getFile();
			Path filePath = node.getFile().toPath();
			StringBuffer hashHex = new StringBuffer();
			byte[] hashDigest;

			if (file.isFile()) {
				byte[] fileBytes = Files.readAllBytes(filePath);
				md.update(fileBytes);
			}

			md.update(file.getAbsolutePath().getBytes());
			md.update(Long.toString(file.lastModified()).getBytes());
			md.update(Boolean.toString(file.canExecute()).getBytes());
			md.update(Boolean.toString(file.canRead()).getBytes());
			md.update(Boolean.toString(file.canWrite()).getBytes());

			if (file.isDirectory()) {
				for (Inode child : node.getChildren()) {
					md.update(child.getHash().getBytes());
				}
			}

			hashDigest = md.digest();
			for (int i = 0; i < hashDigest.length; i++) {
				hashHex.append(Integer.toHexString(0xFF & hashDigest[i]));
			}

			return hashHex.toString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static String calculateFileHash(Inode node) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			File file = node.getFile();
			Path filePath = node.getFile().toPath();
			StringBuffer hashHex = new StringBuffer();
			byte[] hashDigest;
			byte[] fileBytes = Files.readAllBytes(filePath);

			md.update(fileBytes);
			md.update(file.getAbsolutePath().getBytes());
			md.update(Long.toString(file.lastModified()).getBytes());
			md.update(Boolean.toString(file.canExecute()).getBytes());
			md.update(Boolean.toString(file.canRead()).getBytes());
			md.update(Boolean.toString(file.canWrite()).getBytes());

			hashDigest = md.digest();
			for (int i = 0; i < hashDigest.length; i++) {
				hashHex.append(Integer.toHexString(0xFF & hashDigest[i]));
			}

			return hashHex.toString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * TODO Calculates the hash for the Inode based on the collection of information
	 * passed in.
	 * 
	 * @param node
	 * @return
	 */
	public static String calculateHash(Collection<String> nodeInfo) {
		StringBuffer hashHex = new StringBuffer();
		byte[] hashDigest;

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			for (String s : nodeInfo) {
				md.update(s.getBytes());
			}

			hashDigest = md.digest();
			for (int i = 0; i < hashDigest.length; i++) {
				hashHex.append(Integer.toHexString(0xFF & hashDigest[i]));
			}
		} catch (Exception e) {
			// TODO
			System.exit(0);
		}

		return hashHex.toString();
	}
}
