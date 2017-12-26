package fr.syst3ms.skuared.util.evaluation;

import ch.njol.skript.lang.function.Function;
import fr.syst3ms.skuared.util.Algorithms;
import fr.syst3ms.skuared.util.ReflectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

public class MathFunction implements MathTerm {
	private Function<Number> func;
	private List<MathTerm> params;

	public MathFunction(Function<Number> function, List<MathTerm> params) {
		this.func = function;
		this.params = params;
	}

	@Override
	public Number compute(Map<String, Number> unknowns) {
		Stack<Number> parameters = params.stream()
									.map(t -> t.compute(unknowns))
									.collect(Collectors.toCollection(Stack::new));
		if (func.getMaxParameters() > 1 &&
				!ReflectionUtils.isSingle(func.getParameter(0)) &&
				func.getMaxParameters() != parameters.size()) {
			Algorithms.evalError("Wrong number of parameters for the '" + func.getName() + "' function");
			return Double.NaN;
		}
		Number[][] params = new Number[func.getMaxParameters()][func.getMaxParameters()];
		if (func.getMaxParameters() == 1 && ReflectionUtils.isSingle(func.getParameter(0))) {
			for (int i = 0; i < func.getMaxParameters(); i++) {
				params[0][i] = parameters.pop();
			}
		} else {
			for (int i = 0; i < func.getMaxParameters(); i++) {
				params[i][0] = parameters.pop();
			}
		}
		return func.execute(params)[0];
	}
}
