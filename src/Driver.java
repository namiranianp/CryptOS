import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.HashSet;

public class Driver {
	static int level;
	static HashSet<Integer> editingFiles;
	static HashMap<Integer, Inode> allNodes;

	public static Inode createFileSystem(File rootFile) {
		Inode rootNode = new Inode(level, rootFile);
		rootNode.setHash(RecursiveHashCalculaor.calculateGeneralHash(rootNode));
		allNodes.put(level, rootNode);

		File childFile;
		Inode childNode;

		allNodes.put(level, rootNode);

		String absolutePath = rootFile.getAbsolutePath();

		if (rootFile.isDirectory()) {
			for (String s : rootFile.list()) {
				childFile = new File(absolutePath + '/' + s);
				level++;
				childNode = createFileSystem(childFile);
				rootNode.addChild(childNode);
			}
		}

		return rootNode;
	}

	public static void main(String[] args) {
		level = 0;
		editingFiles = new HashSet<>();
		editingFiles.add(3);
		editingFiles.add(12);

		allNodes = new HashMap<>();
		
		File rootFile = new File("/Users/pedramaranian/code/212/FileSystem");

		System.out.println("getPath " + rootFile.getAbsolutePath());

		Inode root = createFileSystem(rootFile);

		allNodes.get(3).setBeingEdited(true);
		
		System.out.println(root);

		RecursiveHashCalculaor.calcultateHash(allNodes.get(3));

		System.out.println(root);
	}
}
