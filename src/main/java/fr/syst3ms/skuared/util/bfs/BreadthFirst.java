package fr.syst3ms.skuared.util.bfs;

import fr.syst3ms.skuared.expressions.ExprSkuaredError;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.material.Gate;

import java.util.*;
import java.util.stream.Collectors;

public class BreadthFirst {
	private Location start = null;
	private Location end = null;
	private boolean isPlayer = false;
	private boolean ignoreDamage = true;
	private int range;

	public BreadthFirst(Location start, Location end, boolean isPlayer, boolean ignoreDamage, int range) {
		this.range = range;
		if (start.getWorld() != end.getWorld()) {
			ExprSkuaredError.lastError = "[Breadth-First Search] Start and end do not belong to the same world";
			return;
		}
		boolean s =  this.isLocationWalkable(start), e = this.isLocationWalkable(end);
		if (!isPlayer && (!s || !e)) {
			ExprSkuaredError.lastError = "[Pathfinding] Start and/or end locations are not walkable.";
			return;
		}
		this.start = start;
		this.end = end;
		this.isPlayer = isPlayer;
		this.ignoreDamage = ignoreDamage;
	}

	private LinkedList<Node> OPEN = new LinkedList<>();
	private List<Node> CLOSED = new ArrayList<>();

	public List<Node> iterate() {
		if (start == null) {
			return Collections.emptyList();
		}
		OPEN.add(new Node(start, null, 0));
		while (canContinue()) {
			if (CLOSED.stream().map(Node::getLocation).collect(Collectors.toList()).contains(end))
				return CLOSED;
			for (Node n : OPEN) {
				processNeighbours(n);
			}
		}
		return null;
	}

	private boolean canContinue() {
		// check if open list is empty, if it is no path has been found
		return OPEN.size() != 0 && !CLOSED.stream().map(Node::getLocation).collect(Collectors.toList()).contains(end);
	}

	public List<Location> tracePath(List<Node> nodePath) {
		List<Location> path = new ArrayList<>();
		Node ref = nodePath.stream().filter(n -> n.getLocation().equals(end)).findFirst().orElse(null);
		assert ref != null;
		do {
			path.add(ref.getLocation());
		} while (!(ref = ref.getParent()).getLocation().equals(start));
		return path;
	}

	private void processNeighbours(Node current) {

		// set of possible walk to locations adjacent to current tile
		HashSet<Node> possible = new HashSet<>(26);

		for (byte x = -1; x <= 1; x++) {
			for (byte y = -1; y <= 1; y++) {
				for (byte z = -1; z <= 1; z++) {

					if (x == 0 && y == 0 && z == 0) {
						continue;// don't check current square
					}
					Location l = new Location(start.getWorld(),
						current.getLocation().getBlockX() + x,
						current.getLocation().getY() + y,
						current.getLocation().getZ() + z
					);
					Node n = new Node(l, current, current.getLocation().distance(l));


					if (!n.isInRange(this.range)) {
						// if block is out of bounds continue
						continue;
					}
					if (x != 0 && z != 0 && (y == 0 || y == 1)) {
						// check to stop jumping through diagonal blocks
						l = new Location(start.getWorld(),
							current.getLocation().getBlockX() + x,
							current.getLocation().getY() + y,
							current.getLocation().getZ()
						);
						Node xOff = new Node(l, current, current.getLocation().distance(l));
						l = new Location(start.getWorld(),
							current.getLocation().getBlockX(),
							current.getLocation().getY() + y,
							current.getLocation().getZ() + z
						);
						Node zOff = new Node(
							l,
							current,
							current.getLocation().distance(l)
						);
						if (isNodeValid(xOff) || isNodeValid(zOff)) {
							continue;
						}
					}

					if (CLOSED.contains(n)) {
						// ignore tile
						continue;
					}

					// only process the tile if it is valid
					if (isNodeValid(n)) {
						possible.add(n);
					}

				}
			}
		}
		for (Node n : possible) {
			// get the reference of the object in the array
			if (!OPEN.contains(n)) {
				// not on open list, so add
				OPEN.add(n);
			} else {
				// is on open list, check if path to that square is better using
				Node openRef = OPEN.get(OPEN.indexOf(n));
				if (n.getCostFromParent() < openRef.getCostFromParent()) {
					// if current path is better, change parent
					openRef.setParent(current);
					openRef.setCostFromParent(current.getLocation().distance(n.getLocation()));
				}
			}
		}
		OPEN.remove(current);
		CLOSED.add(current);
	}
	
	public boolean isNodeValid(Node n) {
		return isPlayer ? isNodeWalkable(n) : canBlockBeWalkedThrough(n.getLocation().getBlock().getTypeId());
	}

	private boolean isNodeWalkable(Node n) {
		Location l = new Location(start.getWorld(),
			(start.getBlockX() + n.getLocation().getBlockX()),
			(start.getBlockY() + n.getLocation().getY()),
			(start.getBlockZ() + n.getLocation().getZ())
		);
		Block b = l.getBlock();
		int i = b.getTypeId();

		// lava, fire, wheat and ladders cannot be walked on, and of course air
		// 85, 107 and 113 stops npcs climbing fences and fence gates
		if (i != 10 && i != 11 && i != 51 && i != 59 && i != 65 && i != 0 && i != 85 && i != 107 && i != 113
			&& !canBlockBeWalkedThrough(i)) {
			// make sure the blocks above are air

			if (b.getRelative(0, 1, 0).getType() == Material.FENCE_GATE) {
				// fench gate check, if closed continue
				Gate g = new Gate(b.getRelative(0, 1, 0).getData());
				return (g.isOpen() && (b.getRelative(0, 2, 0).getType() == Material.AIR));
			}
			return (canBlockBeWalkedThrough(b.getRelative(0, 1, 0).getTypeId())
					&& b.getRelative(0, 2, 0).getType() == Material.AIR);

		} else {
			return false;
		}
	}

	private boolean isLocationWalkable(Location l) {
		Block b = l.getBlock();
		int i = b.getTypeId();

		// make sure the blocks above are air or can be walked through
		return i != 10 && i != 11 && i != 51 && i != 59 && i != 0 && (i != 213 && !ignoreDamage) && (i == 65 && (
			isLocationWalkable(b.getRelative(0, -1, 0).getLocation())
			|| b.getRelative(0, -1, 0).getType() == Material.LADDER)) && !canBlockBeWalkedThrough(i) && (
				   canBlockBeWalkedThrough(b.getRelative(0, 1, 0).getTypeId())
				   && b.getRelative(0, 2, 0).getType() == Material.AIR
				   || b.getRelative(0, 2, 0).getType() == Material.LADDER);
	}

	private boolean canBlockBeWalkedThrough(int id) {
		return (id == 0 || id == 6 || id == 50 || id == 51 && ignoreDamage || id == 63 || id == 30 || id == 31
				|| id == 32 || id == 37 || id == 38 || id == 39 || id == 40 || id == 55 || id == 65 || id == 66
				|| id == 75 || id == 76 || id == 78);
	}

	public boolean isPlayer() {
		return isPlayer;
	}

	public Location getStart() {
		return start;
	}

	public Location getEnd() {
		return end;
	}

	public boolean ignoresDamage() {
		return ignoreDamage;
	}
}
