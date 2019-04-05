
public class Driver {

	public static void main(String[] args) {
		Inode root = new Inode(0);

		Inode inode1 = new Inode(1);
		Inode inode2 = new Inode(2);
		Inode inode3 = new Inode(3);
		Inode inode4 = new Inode(4);
		Inode inode5 = new Inode(5);
		Inode inode6 = new Inode(6);
		Inode inode7 = new Inode(7);
		Inode inode8 = new Inode(8);
		Inode inode9 = new Inode(9);

		// level 1
		root.addChild(inode1);
		root.addChild(inode2);
		root.addChild(inode3);

		// level 2
		inode1.addChild(inode4);
		inode1.addChild(inode5);
		inode2.addChild(inode6);

		// level 3
		inode4.addChild(inode7);
		inode6.addChild(inode8);
		inode6.addChild(inode9);

		// what I am editing
		inode7.setBeingEdited(true);
		inode9.setBeingEdited(true);
		inode8.setBeingEdited(true);

		System.out.println(root);

		RecursiveHashCalculaor.calcultateHash(inode7);

		System.out.println(root);
	}
}
