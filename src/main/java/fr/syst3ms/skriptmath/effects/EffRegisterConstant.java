package fr.syst3ms.skriptmath.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.syst3ms.skriptmath.util.Algorithms;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Register Constant")
@Description({"Registers a constant (duh), which can be used in the evaluate expression (described in [Evaluating Expressions](https://github.com/Syst3ms/Skuared/wiki/Evaluating-Expressions)).",
		" A constant name must consist of one letter optionally followed by one or more letters or digits",
		" If that's any clearer to you, the regex skript-math uses is '[A-Za-z][A-Za-z\\\\d]*.'"})
@Examples("register constant \"G\" with value (eval expr \"6.67408* 10^−11\")")
@Since("1.0")
@SuppressWarnings("unchecked")
public class EffRegisterConstant extends Effect {
	private Expression<String> symbol;
	private Expression<Number> value;

	static {
		Skript.registerEffect(
			EffRegisterConstant.class,
			"register [new] constant [[with] symbol] %string% [(and|with)] value %number%"
		);
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		symbol = (Expression<String>) exprs[0];
		if (symbol instanceof Literal) {
			String s = ((Literal<String>) symbol).getSingle();
			if (!Algorithms.NAME_PATTERN.matcher(s).matches()) {
				Skript.error("'" + s +
							 "' is not a valid constant symbol. A constant symbol must consist of either one letter optionally followed by multiple letters or digits");
				return false;
			}
		}
		value = (Expression<Number>) exprs[1];
		return true;
	}

	@Override
	protected void execute(Event e) {
		String s = symbol.getSingle(e);
		Number v = value.getSingle(e);
		if (v == null || s == null)
			return;
		if (!Algorithms.NAME_PATTERN.matcher(s).matches()) {
			Skript.warning("'" + s +
						   "' is not a valid constant symbol. A constant symbol must consist of either one letter optionally followed by multiple letters or digits");
			return;
		}
		Algorithms.registerConstant(s, v);
	}

	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "register constant with symbol " + symbol.toString(e, debug) + " and value " + value.toString(e, debug);
	}
}
