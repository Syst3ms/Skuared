package fr.syst3ms.skriptmath.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.effects.Delay;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import fr.syst3ms.skriptmath.expressions.ExprLastResult;
import fr.syst3ms.skriptmath.util.Algorithms;
import org.bukkit.event.Event;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Async effect concept, with courtesy of Lubbock/w00tmaster
 */
@Name("Integral")
@Description({"Computes the integral of a given expression. For more details, read the [extended description](https://github.com/Syst3ms/Skuared/wiki/Computational-Effects])"})
@Examples({"compute integral of \"x^2\"",
        "set {_integral} to last skuared result # \"x^3/3\""})
@Since("1.1")
public class EffIntegrate extends Effect {
    private static final ReentrantLock SKRIPT_EXECUTION = new ReentrantLock(true);
    private static final Field DELAYED;
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    static {
        Field _DELAYED = null;
        try {
            _DELAYED = Delay.class.getDeclaredField("delayed");
            _DELAYED.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Skript.warning("Skript's 'delayed' method could not be resolved. Some Skript warnings may not be available.");
        }
        DELAYED = _DELAYED;
    }

    static {
        Skript.registerEffect(EffIntegrate.class, "((calculate|compute) integral of|integrate) %string% [from %-number% to %-number%]");
    }

    private Expression<String> expression;
    private boolean isDefinite;
    private Expression<Number> upperBound, lowerBound;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        expression = (Expression<String>) exprs[0];
        isDefinite = exprs[1] != null && exprs[2] != null;
        if (isDefinite) {
            lowerBound = (Expression<Number>) exprs[1];
            upperBound = (Expression<Number>) exprs[2];
        }
        return true;
    }

    @Override
    protected void execute(Event e) {
        String expr = expression.getSingle(e);
        Number u = upperBound == null ? null : upperBound.getSingle(e), l = lowerBound == null ? null : lowerBound.getSingle(e);
        if (expr == null || isDefinite && (u == null || l == null))
            return;
        CompletableFuture<String> request = CompletableFuture.supplyAsync(() -> Algorithms.sendWolframApiRequest(isDefinite ? String.format("integrate %s from %s to %s", expr, u, l) : String.format("integrate %s", expr)), threadPool);
        request.whenComplete((res, err) -> {
            if (err != null) {
                err.printStackTrace();
            }
            // Guarantees that the last response will not be changed by another thread
            SKRIPT_EXECUTION.lock();
            try {
                if (res != null) {
                    ExprLastResult.lastResult = res;
                }
                if (getNext() != null) {
                    TriggerItem.walk(getNext(), e);
                }
            } finally {
                SKRIPT_EXECUTION.unlock();
            }
        });
    }

    @Override
    protected TriggerItem walk(Event e) {
        debug(e, true);
        delay(e);
        execute(e);
        return null;
    }

    @SuppressWarnings("unchecked")
    private void delay(Event e) {
        if (DELAYED != null) {
            try {
                ((Set<Event>) DELAYED.get(null)).add(e);
            } catch (IllegalAccessException ignored) {
            }
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "integrate " + expression.toString(e, debug);
    }
}
