import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Driver {
	static int level = 0;
	static HashSet<Integer> editingFiles;
	static HashMap<Integer, Inode> allNodes;

	public static Inode createFileSystem(File rootFile) {
		System.out.println("root is: " + rootFile);
		Inode rootNode = new Inode(level, rootFile);

		// TODO: sets the hash upon initialization
		rootNode.setHash(RecursiveHashCalculaor.calculateFileHash(rootNode));

		File childFile;
		Inode childNode;

		if (editingFiles.contains(level)) {
			rootNode.setBeingEdited(true);
		}

		allNodes.put(level, rootNode);

		String absolutePath = rootFile.getAbsolutePath();

		if (rootFile.isDirectory()) {
			for (String s : rootFile.list()) {
				childFile = new File(absolutePath + '/' + s);
				level++;
				childNode = createFileSystem(childFile);
				rootNode.setHash(rootNode.getHash() + childNode.getHash());
				rootNode.addChild(childNode);
			}
		}

		return rootNode;
	}

	public static void main(String[] args) {
		editingFiles = new HashSet<>();
		editingFiles.add(3);
		editingFiles.add(12);

		allNodes = new HashMap<>();

		File rootFile = new File("/Users/pedramaranian/code/212/FileSystem");

		System.out.println("getPath " + rootFile.getAbsolutePath());

		Inode root = createFileSystem(rootFile);

		System.out.println(root);

		RecursiveHashCalculaor.calcultateHash(allNodes.get(3));

		System.out.println(root);
	}
}
