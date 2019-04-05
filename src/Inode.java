import java.util.ArrayList;

public class Inode {
	private ArrayList<Inode> children;
	private Inode parent;
	private int id;
	private float hash;
	private boolean beingEdited;
	private boolean haveEditing;
	private boolean oldHash;

	public Inode(int id) {
		children = new ArrayList<>();
		parent = null;
		this.id = id;
	}

	/**
	 * Adds a child node to this node if it is not already a child. Returns true if
	 * the child was not already a child of this node, false otherwise.
	 * 
	 * @param child An {@link Inode}
	 * @return True if child is not yet a child of this node
	 */
	public boolean addChild(Inode child) {
		if (!children.contains(child)) {
			children.add(child);
			child.parent = this;
		}

		return false;
	}

	/**
	 * Sets whether or not this Inode is being edited
	 * 
	 * @param editing whether or not this is being edited
	 */
	public void setBeingEdited(boolean editing) {
		beingEdited = editing;

		if (editing) {
			oldHash = true;
		}

		if (parent != null) {
			parent.setHasEditing(editing);
		}
	}

	/**
	 * Sets whether or not this Inode has a child that is being edited.
	 * 
	 * @param editing whether or not this Inode has a child being edited
	 */
	public void setHasEditing(boolean editing) {
		boolean change = true;

		if (editing) {
			haveEditing = true;
			oldHash = true;

			if (parent != null) {
				parent.setHasEditing(true);
			}
		} else {
			for (Inode child : children) {
				if (child.beingEdited || child.haveEditing) {
					change = false;
					break;
				}
			}

			if (change) {
				haveEditing = false;

				if (parent != null) {
					parent.setHasEditing(false);
				}
			}
		}
	}

	/**
	 * Sets the hash of this Inode equal to hash
	 * 
	 * @param hash A float representing the updated hash for this Inode
	 */
	public void setHash(float hash) {
		this.hash = hash;
	}

	/**
	 * Tells this Inode whether or not the OS needs to calculate a new hash for it
	 * 
	 * @param needNewHash
	 */
	public void setOldHash(boolean needNewHash) {
		oldHash = needNewHash;
	}

	/**
	 * @return The parent of this Inode, another Inode
	 */
	public Inode getParent() {
		return parent;
	}

	/**
	 * @return Copy of the children list for this Inode
	 */
	public ArrayList<Inode> getChildren() {
		return new ArrayList<Inode>(children);
	}

	/**
	 * @return The hash of this Inode
	 */
	public float getHash() {
		return hash;
	}

	/**
	 * @return The id of this Inode
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return Whether or not this Inode is being edited
	 */
	public boolean isBeingEdited() {
		return beingEdited;
	}

	/**
	 * @return Whether or not this Inode has a child that is being edited
	 */
	public boolean doesHasEditing() {
		return haveEditing;
	}

	/**
	 * @return A boolean representing if the hash this Inode has is old, and
	 *         therefore needs to be replaced
	 */
	public boolean hasOldHash() {
		return oldHash;
	}

	public String formatString(int level) {
		StringBuilder builder = new StringBuilder();

		builder.append("id: ");
		builder.append(id);
		builder.append(" parent: ");

		if (parent != null) {
			builder.append(parent.id);
		} else {
			builder.append("-1");
		}

		builder.append(" hash: ");
		builder.append(hash);

		builder.append(" isBeingEdited: ");
		builder.append(beingEdited);

		builder.append(" hasEditing:: ");
		builder.append(haveEditing);
		
		builder.append(" oldHash: ");
		builder.append(oldHash);

		builder.append(" children: ");
		builder.append("\n");

		for (Inode child : children) {
			for (int i = 0; i <= level; i++) {
				builder.append("\t");
			}
			builder.append(child.formatString(level + 1));
		}

		return builder.toString();
	}

	@Override
	public String toString() {
		return formatString(0);
	}
}
