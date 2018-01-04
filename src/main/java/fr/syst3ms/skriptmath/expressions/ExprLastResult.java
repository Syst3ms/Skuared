package fr.syst3ms.skriptmath.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.skriptmath.util.Algorithms;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Collections;

public class ExprLastResult extends SimpleExpression<Object> {
    public static String lastResult;

    static {
        Skript.registerExpression(
                ExprLastResult.class,
                Object.class,
                ExpressionType.SIMPLE,
                "[the] last skript-math result"
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
            Number result = Algorithms.parseMathExpression(lastResult, Collections.emptyList(), true).evaluate();
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
        return "last skript-math result";
    }
}
