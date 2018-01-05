package fr.syst3ms.skriptmath.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import fr.syst3ms.skriptmath.util.SkriptUtil;
import fr.syst3ms.skriptmath.util.evaluation.MathExpression;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings("unchecked")
@Name("Evaluate Expression")
@Description({"Evaluates a mathematical expression.",
        "Check out [the advanced description](TUTORIAL_URL#evaluate-expression) for WAYYY more information !"})
@Examples({"set {_var} to 5",
        "set {_res} to evaluate expr \"2(3 + %{_var}%)\" # 16",
        "if last skript-math error is not set:",
        "   # proceed as usual",
        "else:",
        "   # something messed up, access the error through 'last skript-math error'"})
@Since("1.0")
public class ExprEvaluateExpression extends SimpleExpression<Number> {
    private Expression<MathExpression> mathExpression;
    private VariableString unknownVariable;
    private boolean isLocal;

    static {
        Skript.registerExpression(
                ExprEvaluateExpression.class,
                Number.class,
                ExpressionType.COMBINED,
                "eval[uate] [math] expr[ession] %mathexpression% [with unknown[s] %-~objects%]"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        mathExpression = (Expression<MathExpression>) exprs[0];
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
    protected Number[] get(Event e) {
        MathExpression expr = mathExpression.getSingle(e);
        if (expr == null) {
            return null;
        }
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
        return new Number[]{expr.evaluate()};
    }

    private Object getVariable(Event e, String name) {
        final Object val = Variables.getVariable(name, e, isLocal);
        if (val == null) {
            return Variables.getVariable((isLocal ? Variable.LOCAL_VARIABLE_TOKEN : "") + name, e, false);
        }
        return val;
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return null;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return null;
    }
}
