package fr.syst3ms.skuared.util.evaluation;

import fr.syst3ms.skuared.util.Algorithms;
import fr.syst3ms.skuared.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@FunctionalInterface
public interface MathTerm {
	Number compute(Map<String, Number> unknowns);

	static MathTerm parse(String s, List<String> unknownData) {
		Pattern binaryPattern = Pattern.compile("0[Bb][01]+"),
			hexPattern = Pattern.compile("0[Xx]\\p{XDigit}+"),
			octPattern = Pattern.compile("0[0-8]+");
		if (StringUtils.isNumeric(s)) {
			return new Constant(StringUtils.parseNumber(s));
		} else if (binaryPattern.matcher(s).matches()) {
			return new Constant(StringUtils.parseBin(s));
		} else if (octPattern.matcher(s).matches()) {
			return new Constant(StringUtils.parseOctal(s));
		} else if (hexPattern.matcher(s).matches()) {
			return new Constant(StringUtils.parseHex(s));
		} else if (Algorithms.getConstants().containsKey(s)) {
			return new Constant(Algorithms.getConstants().get(s));
		} else if (s.length() == 1 && unknownData.contains(s)) {
			return new Unknown(s);
		} else {
			return null;
		}
	}
}
