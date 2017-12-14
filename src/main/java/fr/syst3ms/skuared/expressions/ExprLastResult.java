package fr.syst3ms.skuared.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.skuared.util.Algorithms;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

public class ExprLastResult extends SimpleExpression<Object> {
    public static String lastResult;

    static {
        Skript.registerExpression(
                ExprLastResult.class,
                Object.class,
                ExpressionType.SIMPLE,
                "[the] last skuared result"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected Object[] get(Event e) {
        if (lastResult.contains("x")) {
            return new String[]{lastResult};
        } else {
            Number result = Algorithms.evaluate(lastResult, null);
            return result == null ? null : new Number[]{result};
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<?> getReturnType() {
        return Object.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "last skuared result";
    }
}
