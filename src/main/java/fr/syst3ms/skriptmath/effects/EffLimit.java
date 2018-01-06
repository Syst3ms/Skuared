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
@Name("Limit")
@Description({"Computes the limit of a given expression. For more details, read the [extended description](https://github.com/Syst3ms/skript-math/wiki/Computational-Effects])"})
@Examples({"limit \"1/x\" as x approaches 0",
        "set {_limit} to last skript-math result # Infinity"})
@Since("1.1")
public class EffLimit extends Effect {
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
        Skript.registerEffect(
                EffLimit.class,
                "limit %string% as x approaches %number% [(1¦from above)]",
                "limit %string% as x approaches [(1¦-)]infinity"
        );
    }

    private Expression<String> expression;
    private byte args;
    private Expression<Number> approach;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        expression = (Expression<String>) exprs[0];
        args = (byte) matchedPattern;
        if ((args & 2) == 0)
            approach = (Expression<Number>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        String expr = expression.getSingle(e);
        Number a = approach == null ? null : approach.getSingle(e);
        if (expr == null || (args & 2) == 0 && approach == null)
            return;
        String req;
        if ((args & 2) == 2) { // limit towards infinity
            req = String.format("limit %s as x->%s", expr, ((args & 1) == 1 ? "-" : "") + "infinity");
        } else { // limit towards real number
            assert a != null;
            req = String.format("limit %s as x->%s", expr, a.intValue() + ((args & 1) == 1 ? "-" : "+"));
        }
        CompletableFuture<String> request = CompletableFuture.supplyAsync(() -> Algorithms.sendLimitRequest(req), threadPool);
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
        if ((args & 2) == 0) {
            return String.format("limit %s as x->%s", expression.toString(e, debug), approach.toString(e, debug) + ((args & 1) == 1 ? "-" : "+"));
        } else {
            return String.format("limit %s as x->%s", expression.toString(e, debug), ((args & 1) == 1 ? "-" : "") + "infinity");
        }
    }
}
