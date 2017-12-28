package fr.syst3ms.skuared.util.evaluation;

import fr.syst3ms.skuared.util.Algorithms;
import fr.syst3ms.skuared.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class MathTerm {

	public static MathTerm parse(String s, List<String> unknownData) {
		Pattern binaryPattern = Pattern.compile("0[Bb][01]+"),
			hexPattern = Pattern.compile("0[Xx]\\p{XDigit}+"),
			octPattern = Pattern.compile("0[0-8]+");
		if (StringUtils.isNumeric(s)) {
			return Constant.getConstant(StringUtils.parseNumber(s));
		} else if (binaryPattern.matcher(s).matches()) {
			return Constant.getConstant(StringUtils.parseBin(s));
		} else if (octPattern.matcher(s).matches()) {
			return Constant.getConstant(StringUtils.parseOctal(s));
		} else if (hexPattern.matcher(s).matches()) {
			return Constant.getConstant(StringUtils.parseHex(s));
		} else if (Algorithms.getConstants().containsKey(s)) {
			return Constant.getConstant(Algorithms.getConstants().get(s));
		} else if (s.length() == 1 && unknownData.contains(s)) {
			return new Unknown(s);
		} else {
			return null;
		}
	}

	public abstract Number compute(Map<String, ? extends Number> unknowns);

	public abstract boolean hasUnknown();

	public abstract MathTerm simplify();

	public abstract String asString();

	@Override
	public String toString() {
		return asString();
	}

	public MathTerm getNegative() {
		return new Difference(Constant.ZERO, this).simplify();
	}

	public MathTerm getSquared() {
		return new Power(this, Constant.TWO).simplify();
	}

	public MathTerm getReciprocal() {
		return new Division(Constant.ONE, this).simplify();
	}

	public MathTerm getSquareRoot() {
		return MathFunction.getFunctionByName("sqrt", Collections.singletonList(this)).simplify();
	}
}
