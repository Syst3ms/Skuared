package fr.syst3ms.skuared.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import fr.syst3ms.skuared.util.Algorithms;
import fr.syst3ms.skuared.util.SkriptUtil;
import fr.syst3ms.skuared.util.StringUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ExprEvaluateWithConstant extends SimpleExpression<Number> {
	private static final String[] letters = "abcdefghijklmnopqrstuvwxyz".split("");
	private Expression<String> expr;
	private Variable<Number> unknownData;

	static {
		Skript.registerExpression(ExprEvaluateWithConstant.class, Number.class, ExpressionType.COMBINED, "eval[uate] [[math[ematic]] expr[ession]] %string% with %~numbers%");
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		expr = (Expression<String>) expressions[0];
		Expression<Number> e = (Expression<Number>) expressions[1];
		if (!(e instanceof Variable)) {
			Skript.error("Unknown data must be put inside a list variable");
			return false;
		}
		unknownData = (Variable<Number>) e;
		return true;
	}

	@SuppressWarnings("unchecked")
	@Nullable
	@Override
	protected Number[] get(Event event) {
		String e = expr.getSingle(event);
		Map<String, Object> data = (Map<String, Object>) Variables.getVariable(SkriptUtil.getVariableName(unknownData)
																						 .toString(event),
																			   event,
																			   unknownData.isLocal()
		);

		if (e == null || data == null) {
			return null;
		}
		Map<String, Number> numberData = new HashMap<>();
		if (data.keySet().stream().anyMatch(StringUtils::isInteger)) {
			for (int i = 0; i < data.size(); i++) {
				numberData.put(letters[i], (Number) new ArrayList<>(data.values()).get(i));
			}
		} else {
			for (Map.Entry<String, Object> entry : data.entrySet()) {
				numberData.put(entry.getKey(), (Number) entry.getValue());
			}
		}
		Number res = Algorithms.evaluate(e, numberData);
		if (res == null) {
			return null;
		}
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
		return "evaluate expr " + expr.toString(event, b) + " with " + unknownData.toString(event, b);
	}
}
