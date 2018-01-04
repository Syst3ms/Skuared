package fr.syst3ms.skriptmath.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.skriptmath.util.evaluation.MathExpression;
import fr.syst3ms.skriptmath.util.Algorithms;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExprCompileExpression extends SimpleExpression<MathExpression> {
    private Expression<String> expression;
    private Expression<String> unknowns;

    static {
        Skript.registerExpression(
                ExprCompileExpression.class,
                MathExpression.class,
                ExpressionType.COMBINED,
                "compile[d] [math] expression[ession] %string% [with unknown[ name][s] %-strings%]"
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        expression = (Expression<String>) exprs[0];
        unknowns = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    protected MathExpression[] get(Event e) {
        String expr = expression.getSingle(e);
        List<String> u = unknowns == null ? Collections.emptyList() : Arrays.asList(unknowns.getArray(e));
        if (expr == null) {
            ExprSkuaredError.lastError = "Expression to compile was null";
            return null;
        }
        MathExpression mathExpr = Algorithms.parseMathExpression(expr, u, false);
        return new MathExpression[]{mathExpr};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends MathExpression> getReturnType() {
        return MathExpression.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "compile math expression " + expression.toString(e, debug);
    }
}
