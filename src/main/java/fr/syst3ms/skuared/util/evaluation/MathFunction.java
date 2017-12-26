package fr.syst3ms.skuared.util.evaluation;

import ch.njol.skript.lang.function.Function;
import fr.syst3ms.skuared.util.Algorithms;
import fr.syst3ms.skuared.util.ReflectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

public class MathFunction implements MathTerm {
	private Function<Number> function;
	private List<MathTerm> params;

	public MathFunction(Function<Number> function, List<MathTerm> params) {
		this.function = function;
		this.params = params;
	}

	public Function<Number> getFunction() {
		return function;
	}

	public List<MathTerm> getParams() {
		return params;
	}

	@Override
	public Number compute(Map<String, Number> unknowns) {
		Stack<Number> parameters = params.stream()
									.map(t -> t.compute(unknowns))
									.collect(Collectors.toCollection(Stack::new));
		if (function.getMaxParameters() > 1 &&
				!ReflectionUtils.isSingle(function.getParameter(0)) &&
				function.getMaxParameters() != parameters.size()) {
			Algorithms.evalError("Wrong number of parameters for the '" + function.getName() + "' function");
			return Double.NaN;
		}
		Number[][] params = new Number[function.getMaxParameters()][function.getMaxParameters()];
		if (function.getMaxParameters() == 1 && ReflectionUtils.isSingle(function.getParameter(0))) {
			for (int i = 0; i < function.getMaxParameters(); i++) {
				params[0][i] = parameters.pop();
			}
		} else {
			for (int i = 0; i < function.getMaxParameters(); i++) {
				params[i][0] = parameters.pop();
			}
		}
		return function.execute(params)[0];
	}

	@Override
	public boolean hasUnknown() {
		return params.stream().anyMatch(MathTerm::hasUnknown);
	}
}
