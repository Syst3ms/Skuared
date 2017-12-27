package fr.syst3ms.skuared.util.evaluation;

import ch.njol.skript.lang.function.Function;
import ch.njol.skript.lang.function.Functions;
import fr.syst3ms.skuared.util.MathUtils;

import java.util.Collections;
import java.util.function.BinaryOperator;

public class Power extends DoubleOperandTerm {
	private MathTerm first, second;

	public Power(MathTerm first, MathTerm second) {
		super(first, second);
	}

	@Override
	BinaryOperator<Number> getFunction() {
		return MathUtils::pow;
	}

	@Override
	public String toString() {
		return "(" + getFirst().toString() + " ^ " + getSecond().toString() + ")";
	}

	@SuppressWarnings("unchecked")
	@Override
	public MathTerm simplify() {
		MathTerm sup = super.simplify();
		if (sup != this) {
			return sup;
		} else if (first == Constant.ZERO && second instanceof Constant && Math.signum(((Constant) second).getValue().doubleValue()) < 1) {
			return Constant.NAN;
		} else if (first == Constant.ZERO) {
			return Constant.ZERO;
		} else if (first == Constant.ONE) {
			return Constant.ONE;
		} else if (second instanceof Constant) {
			Constant c = (Constant) this.second;
			if (c.getValue().doubleValue() == 0.5d) {
				return new MathFunction((Function<Number>) Functions.getFunction("sqrt"), Collections.singletonList(first));
			} else if (c == Constant.ONE) {
				return first;
			} else if (c == Constant.ZERO) {
				return Constant.ONE;
			} else if (c.getValue().doubleValue() == -1.0d) {
				return new Division(Constant.ONE, c);
			}
		}
		return this;
	}
}
