package fr.syst3ms.skuared.util.bfs;

import org.bukkit.Location;

public class Node {
	private final Location loc;
	private Node parent;
	private double costFromParent;

	public Node(Location location, Node parent, double costFromParent) {
		this.loc = location;
		this.parent = parent;
		this.costFromParent = costFromParent;
	}

	public Location getLocation() {
		return loc;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public boolean isInRange(int range){
		return ((range - Math.abs(loc.getX()) >= 0) && (range - Math.abs(loc.getY()) >= 0) && (range - Math.abs(loc.getZ()) >= 0));
	}

	public double getCostFromParent() {
		return costFromParent;
	}

	public void setCostFromParent(double costFromParent) {
		this.costFromParent = costFromParent;
	}
}
