package fr.syst3ms.skuared.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.skuared.util.Algorithms;
import org.bukkit.event.Event;

import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class ExprAsRPN extends SimpleExpression<String> {
    private Expression<String> e;

    static {
        Skript.registerExpression(
                ExprAsRPN.class,
                String.class,
                ExpressionType.COMBINED,
                "r[[everse] ]p[[olish] ]n[otation] of %string%",
                "%string% (as|converted to) r[[everse] ]p[[olish] ]n[otation]");
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        e = (Expression<String>) expressions[0];
        return true;
    }

    @Override
    protected String[] get(Event event) {
        String expr = e.getSingle(event);
        String rpn = Algorithms.tokensToString(Algorithms.shuntingYard(expr));
        return new String[]{rpn};
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(Event event, boolean b) {
        return "RPN of " + e.toString(event, b);
    }
}
