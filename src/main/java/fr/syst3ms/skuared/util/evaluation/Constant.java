package fr.syst3ms.skuared.util.evaluation;

import fr.syst3ms.skuared.util.MathUtils;

import java.util.Map;

public class Constant extends MathTerm {
	public static final Constant NAN = new Constant(Double.NaN);
	public static final Constant ZERO = new Constant(0);
	public static final Constant ONE = new Constant(1);
	public static final Constant TWO = new Constant(2);
	public static final Constant E = new Constant(Math.E);
	public static final Constant PI = new Constant(Math.PI);
	private Number value;

	private Constant(Number value) {
		this.value = value;
	}

	public static Constant getConstant(Number value) {
		if (MathUtils.equals(value, 0)) {
			return ZERO;
		} else if (MathUtils.equals(value, 1)) {
			return ONE;
		} else if (MathUtils.equals(value, 2)) {
			return TWO;
		} else if (MathUtils.equals(value, Math.E)) {
			return E;
		} else if (MathUtils.equals(value, Math.PI)) {
			return PI;
		} else if (Double.isNaN(value.doubleValue())) {
			return NAN;
		} else {
			return new Constant(value);
		}
	}

	public Number getValue() {
		return value;
	}

	@Override
	public Number compute(Map<String, ? extends Number> unknowns) {
		return value;
	}

	@Override
	public boolean hasUnknown() {
		return false;
	}

	@Override
	public MathTerm simplify() {
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj instanceof Constant && (this == obj || MathUtils.equals(value, ((Constant) obj).value));
	}

	@Override
	public String asString() {
		return value.toString();
	}
}
