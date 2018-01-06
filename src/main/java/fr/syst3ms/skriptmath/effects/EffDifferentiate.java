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
import fr.syst3ms.skriptmath.util.MathUtils;
import fr.syst3ms.skriptmath.util.evaluation.MathExpression;
import fr.syst3ms.skriptmath.util.evaluation.MathTerm;
import org.bukkit.event.Event;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Async effect concept, with courtesy of Lubbock/w00tmaster
 */
@Name("Derivative")
@Description({"Computes the derivative of a given expression. For more details, read the [extended description](https://github.com/Syst3ms/skript-math/wiki/Computational-Effects])"})
@Examples({"compute derivative of \"x^2\"",
        "set {_derivative} to last skript-math result # \"2x\""})
@Since("1.1")
public class EffDifferentiate extends Effect {
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
                EffDifferentiate.class,
                "((calculate|compute) derivative of|differentiate) %string% [at %-number%] (1¦online)",
                "d/dx\\(%string%\\) [at %-number%] (1¦online)"
        );
    }

    private Expression<String> expression;
    private boolean hasPoint;
    private boolean useWolfram;
    private Expression<Number> point;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        expression = (Expression<String>) exprs[0];
        hasPoint = exprs[1] != null;
        useWolfram = parseResult.mark == 1;
        if (hasPoint)
            point = (Expression<Number>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        String expr = expression.getSingle(e);
        Number p = point == null ? null : point.getSingle(e);
        if (expr == null || hasPoint && p == null)
            return;
        CompletableFuture<Object> request;
        if (useWolfram) {
            String wolframRequest = hasPoint ? String.format("differentiate %s at x=%s", expr, p) : String.format("differentiate %s", expr);
            request = CompletableFuture.supplyAsync(() -> Algorithms.sendWolframApiRequest(wolframRequest), threadPool);
        } else {
            MathExpression pair = Algorithms.parseMathExpression(expr, Collections.singletonList("x"), true);
            if (pair == null) {
                return;
            }
            MathTerm term = pair.getTerm();
            if (term == null) {
                return;
            }
            if (hasPoint) {
                request = CompletableFuture.supplyAsync(() -> {
                    MathTerm indefinite = MathUtils.indefiniteDerivative(term);
                    if (indefinite == null)
                        return null;
                    return indefinite.compute(Algorithms.getXMap(p));
                }, threadPool);
            } else {
                request = CompletableFuture.supplyAsync(() -> {
                    MathTerm indefinite = MathUtils.indefiniteDerivative(term);
                    if (indefinite == null)
                        return null;
                    return Algorithms.sendWolframApiRequest("simplify " + indefinite.asString());
                });
            }
        }
        request.whenComplete((res, err) -> {
            if (err != null) {
                err.printStackTrace();
            }
            // Guarantees that the last response will not be changed by another thread
            SKRIPT_EXECUTION.lock();
            try {
                if (res != null) {
                    ExprLastResult.lastResult = res.toString();
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
        return "derivate " + expression.toString(e, debug);
    }
}
