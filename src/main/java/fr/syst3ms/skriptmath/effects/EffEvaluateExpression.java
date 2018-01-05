package fr.syst3ms.skriptmath.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.effects.Delay;
import ch.njol.skript.lang.*;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import fr.syst3ms.skriptmath.expressions.ExprLastResult;
import fr.syst3ms.skriptmath.util.SkriptUtil;
import fr.syst3ms.skriptmath.util.evaluation.MathExpression;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("unchecked")
public class EffEvaluateExpression extends Effect {
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

    private Expression<MathExpression> mathExpr;
    private VariableString unknownVariable;
    private boolean isLocal;

    static {
        Skript.registerEffect(
                EffEvaluateExpression.class,
                "evaluate [math] expression %mathexpression% [with unknown[s] %-~objects%]"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        mathExpr = (Expression<MathExpression>) exprs[0];
        if (exprs[1] != null) {
            if (!(exprs[1] instanceof Variable)) {
                Skript.error("Unknown data must be a list variable");
                return false;
            } else if (!((Variable<Number>) exprs[1]).isList()) {
                Skript.error("Unknown data must be a list variable");
                return false;
            }
        }
        Variable<?> var = (Variable<?>) exprs[1];
        if (var != null) {
            unknownVariable = SkriptUtil.getVariableName((Variable<Number>) exprs[1]);
            isLocal = var.isLocal();
        } else {
            unknownVariable = null;
        }
        return true;
    }

    @Override
    protected void execute(Event e) {
        MathExpression expr = mathExpr.getSingle(e);
        if (expr == null)
            return;
        Map<String, ? super Number> unknownMap;
        if (unknownVariable == null) {
            unknownMap = Collections.emptyMap();
        } else {
            String var = unknownVariable.toString(e).toLowerCase(Locale.ENGLISH);
            unknownMap = (Map<String, ? super Number>) getVariable(e, var.substring(0, var.length() - 1));
        }
        for (Map.Entry<String, ? super Number> entry : unknownMap.entrySet()) {
            expr.map(entry.getKey(), (Number) entry.getValue());
        }
        CompletableFuture<Number> request = CompletableFuture.supplyAsync(expr::evaluate);
        request.whenComplete((res, err) -> {
            if (err != null) {
                err.printStackTrace();
            }
            // Guarantees that the last response will not be changed by another thread
            SKRIPT_EXECUTION.lock();
            try {
                if (res != null) {
                    ExprLastResult.lastResult = res.toString();
                } else {
                    ExprLastResult.lastResult = null;
                }
                if (getNext() != null) {
                    TriggerItem.walk(getNext(), e);
                }
            } finally {
                SKRIPT_EXECUTION.unlock();
            }
        });
    }

    private Object getVariable(Event e, String name) {
        final Object val = Variables.getVariable(name, e, isLocal);
        if (val == null) {
            return Variables.getVariable((isLocal ? Variable.LOCAL_VARIABLE_TOKEN : "") + name, e, false);
        }
        return val;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "evaluate expression " + mathExpr.toString(e, debug);
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
}
