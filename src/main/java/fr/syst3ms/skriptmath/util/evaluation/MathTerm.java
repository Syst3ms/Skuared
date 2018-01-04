package fr.syst3ms.skriptmath.util.evaluation;

import fr.syst3ms.skriptmath.util.StringUtils;
import fr.syst3ms.skriptmath.util.Algorithms;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public interface MathTerm {

	static MathTerm parse(String s, List<String> unknownData) {
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
		} else if (unknownData.contains(s)) {
			return new Unknown(s, s.length() == 2);
		} else {
			return null;
		}
	}

	Number compute(Map<String, ? extends Number> unknowns);

	boolean hasUnknown();

	MathTerm simplify();

	String asString();

	int termCount();

	MathTerm getNegative();

	default MathTerm getSquared() {
		return new Power(this, Constant.TWO).simplify();
	}

	default MathTerm getReciprocal() {
		return new Division(Constant.ONE, this).simplify();
	}

	default MathTerm getSquareRoot() {
		return MathFunction.getFunctionByName("sqrt", Collections.singletonList(this)).simplify();
	}

	default boolean isSimple() {
		return this instanceof Constant || this instanceof Unknown;
	}

}
