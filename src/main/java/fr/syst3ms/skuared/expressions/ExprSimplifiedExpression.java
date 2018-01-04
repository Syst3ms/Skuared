package fr.syst3ms.skuared.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.skuared.util.evaluation.MathExpression;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

public class ExprSimplifiedExpression extends SimpleExpression<MathExpression> {
    private Expression<MathExpression> mathExpr;

    static {
        Skript.registerExpression(
                ExprSimplifiedExpression.class,
                MathExpression.class,
                ExpressionType.SIMPLE,
                "simplified [form of] %mathexpression%"
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        mathExpr = (Expression<MathExpression>) exprs[0];
        return true;
    }

    @Override
    protected MathExpression[] get(Event e) {
        MathExpression expr = mathExpr.getSingle(e);
        if (expr == null)
            return null;
        return new MathExpression[]{expr.simplified()};
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
        return "simplified " + mathExpr.toString(e, debug);
    }
}
