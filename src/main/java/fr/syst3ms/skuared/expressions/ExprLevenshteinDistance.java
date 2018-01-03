package fr.syst3ms.skuared.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.google.common.base.Strings;
import fr.syst3ms.skuared.util.Algorithms;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

@Name("Levenshtein/String distance")
@Description({"Represents the number of edits from one string to another. An edit can be adding, removing or replacing a letter.",
        "Adding 'ignor(e|ing) case' makes it so uppercase and lowercase letters don't count as an edit."})
@Examples("set {_dist} to levenshtein distance between \"Skript\" and \"Spigot\" # 4")
public class ExprLevenshteinDistance extends SimpleExpression<Number> {
    private Expression<String> first, second;
    private boolean ignoreCase;

    static {
        Skript.registerExpression(
                ExprLevenshteinDistance.class,
                Number.class,
                ExpressionType.COMBINED,
                "(levenshtein|string) distance between %string% and %string% [(1¦ignor(e|ing) case)]"
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, @NotNull SkriptParser.ParseResult parseResult) {
        first = (Expression<String>) expressions[0];
        second = (Expression<String>) expressions[1];
        ignoreCase = parseResult.mark == 1;
        return true;
    }

    @NotNull
    @Override
    protected Number[] get(Event event) {
        String f = Strings.nullToEmpty(first.getSingle(event)),
                s = Strings.nullToEmpty(second.getSingle(event));
        return new Number[]{Algorithms.levenshtein(f, s, ignoreCase)};
    }

    @NotNull
    @Override
    public Class<? extends Number> getReturnType() {
        return Integer.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @NotNull
    @Override
    public String toString(Event event, boolean b) {
        return "levenshtein distance between " + first.toString(event, b) + " and " + second.toString(event, b) +
               (ignoreCase ? " ignoring case" : "");
    }
}
