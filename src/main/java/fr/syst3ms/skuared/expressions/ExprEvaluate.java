package fr.syst3ms.skuared.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.skuared.util.Algorithms;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprEvaluate extends SimpleExpression<Number> {
    private Expression<String> expr;

    static {
        Skript.registerExpression(
                ExprEvaluate.class,
                Number.class,
                ExpressionType.COMBINED,
                "eval[uate] [[math[ematic]] expr[ession]] %string%"
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        expr = (Expression<String>) expressions[0];
        return true;
    }

    @Nullable
    @Override
    protected Number[] get(Event event) {
        String e = expr.getSingle(event);
        if (e == null)
            return null;
        Number res = Algorithms.evaluate(e);
        if (res == null)
            return null;
        return new Number[]{res};
    }

    @Nullable
    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    @Contract("-> true")
    public boolean isSingle() {
        return true;
    }

    @NotNull
    @Override
    public String toString(Event event, boolean b) {
        return "evaluate expr " + expr.toString(event, b);
    }
}
