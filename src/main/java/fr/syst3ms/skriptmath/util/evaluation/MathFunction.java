package fr.syst3ms.skriptmath.util.evaluation;

import ch.njol.skript.lang.function.Function;
import ch.njol.skript.lang.function.Functions;
import fr.syst3ms.skriptmath.util.Algorithms;
import fr.syst3ms.skriptmath.util.ReflectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MathFunction implements MathTerm {
	private Function<Number> function;
	private List<MathTerm> params;

	public MathFunction(Function<Number> function, List<MathTerm> params) {
		this.function = function;
		this.params = params;
	}

	@SuppressWarnings("unchecked")
	public static MathFunction getFunctionByName(String name, List<MathTerm> params) {
		return new MathFunction((Function<Number>) Functions.getFunction(name), params);
	}

	public Function<Number> getFunction() {
		return function;
	}

	public List<MathTerm> getParams() {
		return params;
	}

	@Override
	public Number compute(Map<String, ? extends Number> unknowns) {
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
		for (int i = 0; i < function.getMaxParameters(); i++) {
			params[i][0] = parameters.pop();
		}
		return function.execute(params)[0];
	}

	@Override
	public boolean hasUnknown() {
		return params.stream().anyMatch(MathTerm::hasUnknown);
	}

	@Override
	public MathTerm simplify() {
		Stream<MathTerm> paramStream = params.stream();
		params = paramStream.map(MathTerm::simplify).collect(Collectors.toList());
		if (paramStream.noneMatch(MathTerm::hasUnknown)) {
			return Constant.getConstant(compute(null));
		}
		return this;
	}

	@Override
	public String asString() {
		String paramString = params.stream().map(MathTerm::asString).collect(Collectors.joining(", "));
		return function.getName() + "(" + paramString + ")";
	}

	@Override
	public int termCount() {
		return 1 + params.size();
	}

	@Override
	public MathTerm getNegative() {
		return new Product(Constant.getConstant(-1), this).simplify();
	}
	@Override
	public String toString() {
		return asString();
	}
}
