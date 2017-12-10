package fr.syst3ms.skuared.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.effects.Delay;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import fr.syst3ms.skuared.expressions.ExprLastResult;
import fr.syst3ms.skuared.util.Algorithms;
import fr.syst3ms.skuared.util.MathUtils;
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
public class EffProduct extends Effect {
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
            Skript.warning("Skript's 'delayed' method could not be resolved. Some Skript warnings may " +
                           "not be available.");
        }
        DELAYED = _DELAYED;
    }

    static {
        Skript.registerEffect(EffSigma.class, "(product|pi) %string% to %number% starting at [x=]%number%", "(product|pi) %string% to infinity starting at [x=]%number%"); // Would've been so cool to put Π, wouldn't it ?
    }

    private Expression<String> mathExpression;
    private Expression<Number> lastNumber, start;
    private boolean isInfinite;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        mathExpression = (Expression<String>) exprs[0];
        isInfinite = matchedPattern == 1;
        if (isInfinite) {
            start = (Expression<Number>) exprs[1];
        } else {
            lastNumber = (Expression<Number>) exprs[1];
            start = (Expression<Number>) exprs[2];
        }
        return true;
    }

    @Override
    protected void execute(Event e) {
        String expr = mathExpression.getSingle(e);
        Number end = isInfinite ? -1 : lastNumber.getSingle(e), s = start.getSingle(e);
        if (expr == null || end == null || s == null)
            return;
        boolean useWolfram = isInfinite || end.intValue() > Short.MAX_VALUE;
        CompletableFuture<String> request = CompletableFuture.supplyAsync(() ->
                useWolfram
                        ? Algorithms.sendWolframApiRequest(String.format("product %s,x=%s to %s", expr, start, isInfinite ? "infinity" : end))
                        : MathUtils.chainedProduct(expr, s.longValue(), end.longValue()).toString(), threadPool
        );
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
        return "product " +
               mathExpression.toString(e, debug) +
               " to " +
               (isInfinite ? "infinity" : lastNumber.toString(e, debug)) +
               " starting at " +
               start.toString(e, debug);
    }
}
