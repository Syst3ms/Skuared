package fr.syst3ms.skuared.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
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

@Name("Evaluate Expression With Constant")
@Description({"Evaluates a mathematical expression with a provided number. The number can be referred to in the expression using 'x'",
        "Check out [the advanced description](TUTORIAL_URL#evaluate-expression) for WAYYY more information !"})
@Examples({"set {_res} to evaluate expr \"2(3 + x)\" with 5 # 16",
        "if last skuared error is not set:",
        "   # proceed as usual",
        "else:",
        "   # something messed up, access the error through 'last skuared error'"})
@Since("1.1")
public class ExprEvaluateWithConstant extends SimpleExpression<Number> {
    private Expression<String> expr;
    private Expression<Number> constant;

    static {
        Skript.registerExpression(
                ExprEvaluateWithConstant.class,
                Number.class,
                ExpressionType.COMBINED,
                "eval[uate] [[math[ematic]] expr[ession]] %string% with %number%"
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        expr = (Expression<String>) expressions[0];
        constant = (Expression<Number>) expressions[1];
        return true;
    }

    @Nullable
    @Override
    protected Number[] get(Event event) {
        String e = expr.getSingle(event);
        Number x = constant.getSingle(event);
        if (e == null || x == null)
            return null;
        Number res = Algorithms.evaluate(e, x);
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
        return "evaluate expr " + expr.toString(event, b) + " with x=" + constant.toString(event, b);
    }
}
