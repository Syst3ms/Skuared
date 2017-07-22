package fr.syst3ms.skuared.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.syst3ms.skuared.util.Algorithms;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Created by ARTHUR on 22/07/2017.
 */
@SuppressWarnings("unchecked")
public class EffRegisterConstant extends Effect {
    private Literal<String> symbol;
    private Expression<Number> value;

    static {
        Skript.registerEffect(
                EffRegisterConstant.class,
                "register [new] constant [[with] symbol] %*string% [(and|with)] value %number%"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        assert exprs[0] instanceof Literal;
        symbol = (Literal<String>) exprs[0];
        String s = symbol.getSingle();
        if (Algorithms.NAME_PATTERN.matcher(s).matches()) {
            Skript.error("'"+ s + "' is not a valid constant symbol. A constant symbol must consist of either one letter or underscore optionally followed by multiple letters or digits");
            return false;
        }
        value = (Expression<Number>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        String s = symbol.getSingle();
        Number v = value.getSingle(e);
        if (v == null)
            return;
        Algorithms.registerConstant(s, v);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "register constant with symbol " + symbol.toString(e, debug) + " and value " + value.toString(e, debug);
    }
}
