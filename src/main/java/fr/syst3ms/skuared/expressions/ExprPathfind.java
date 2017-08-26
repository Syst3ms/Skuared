package fr.syst3ms.skuared.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.skuared.util.PathfindAlgorithm;
import fr.syst3ms.skuared.util.astar.AStar;
import fr.syst3ms.skuared.util.astar.Tile;
import fr.syst3ms.skuared.util.bfs.BreadthFirst;
import fr.syst3ms.skuared.util.bfs.Node;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.List;

public class ExprPathfind extends SimpleExpression<Location> {
	private Expression<PathfindAlgorithm> algorithm;
	private boolean isPlayer, ignoreDamage;
	private Expression<Location> start, goal;
	private Expression<Number> range;

	static {
		Skript.registerExpression(ExprPathfind.class,
			Location.class,
			ExpressionType.COMBINED,
			"[(1¦player)] path between %location% and %location% using %algorithm% [(2¦ignoring damage)] (with[in]|in) range %number%"
		);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		start = (Expression<Location>) exprs[0];
		goal = (Expression<Location>) exprs[1];
		isPlayer = (parseResult.mark & 1) == 1;
		ignoreDamage = (parseResult.mark & 2) == 2;
		algorithm = (Expression<PathfindAlgorithm>) exprs[2];
		range = (Expression<Number>) exprs[3];
		return true;
	}

	@Override
	protected Location[] get(Event e) {
		Location s = start.getSingle(e), g = goal.getSingle(e);
		PathfindAlgorithm alg = algorithm.getSingle(e);
		Number r = range.getSingle(e);
		if (s == null || g == null || alg == null || r == null) {
			return null;
		}
		if (r.intValue() < 1) {
			ExprSkuaredError.lastError = "[Pathfinding] The maximum range for pathfinding must be above 0.";
			return null;
		}
		if (alg == PathfindAlgorithm.BREADTH_FIRST) {
			BreadthFirst bfInstance = new BreadthFirst(s, g, isPlayer, ignoreDamage, r.intValue());
			List<Node> path = bfInstance.iterate();
			if (path == null) {
				ExprSkuaredError.lastError = "[Pathfinding] No path was found";
				return null;
			} else if (path.isEmpty()) {
				return null;
			}
			return bfInstance.tracePath(path).toArray(new Location[0]);
		} else {
			AStar aStarInstance = new AStar(s,
				g,
				r.intValue(),
				ignoreDamage,
				isPlayer,
				alg == PathfindAlgorithm.BEST_FIRST
			);
			List<Tile> path = aStarInstance.iterate();
			if (path == null) {
				ExprSkuaredError.lastError = "[Pathfinding] No path was found";
				return null;
			} else if (path.isEmpty()) {
				return null;
			}
			return path.stream().map(t -> t.getLocation(aStarInstance.getStart())).toArray(Location[]::new);
		}
	}

	@Override
	public Class<? extends Location> getReturnType() {
		return Location.class;
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return (isPlayer ? "player " : "")
			   + "path between "
			   + start.toString(e, debug)
			   + " and "
			   + goal.toString(e, debug)
			   + " using "
			   + algorithm.toString(e, debug)
			   + (ignoreDamage ? " ignoring damage" : "")
			   + " within range "
			   + range.toString(e, debug);
	}
}
