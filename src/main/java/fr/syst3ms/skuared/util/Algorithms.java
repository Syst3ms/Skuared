package fr.syst3ms.skuared.util;

import ch.njol.skript.lang.function.Function;
import ch.njol.skript.lang.function.Functions;
import ch.njol.util.Pair;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import fr.syst3ms.skuared.Skuared;
import fr.syst3ms.skuared.expressions.ExprSkuaredError;
import fr.syst3ms.skuared.util.evaluation.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class Algorithms {
	@NotNull
	public static Pattern NAME_PATTERN = Pattern.compile("[A-Za-z][A-Za-z\\d]*");
	@NotNull
	public static String TOKEN_PATTERN = "(?i)(?:(?<=[^\\w]|^)[+-])?(?:0[0-7]+|0x[0-9a-f]+|0b[01]+|\\d+(?:\\.\\d+)?)|[()]|([^\\w ()])\\1*|[a-z_][a-z\\d]*";
	@NotNull
	private static Map<String, Operator> arithmeticOperators = new HashMap<>();
	@NotNull
	private static Map<String, Number> constants = new HashMap<>();
	@NotNull
	private static Pattern binaryPattern = Pattern.compile("0[Bb][01]+"), hexPattern = Pattern.compile("0[Xx]\\p{XDigit}+"), octPattern = Pattern
		.compile("0[0-8]+");

	@NotNull
	public static Map<String, Number> getConstants() {
		return constants;
	}

	public static Map<String, Operator> getOperators() {
		return arithmeticOperators;
	}

	public static void registerOperator(@NotNull String symbol, @NotNull Class<? extends DoubleOperandTerm> operation, @NotNull Associativity associativity, int precedence) {
		arithmeticOperators.put(symbol, new Operator(symbol, precedence, associativity, operation));
	}

	public static void registerConstant(@NotNull String id, Number value) {
		constants.put(id.toLowerCase(), value);
	}

	public static Map<String, Number> getXMap(Number value) {
		return MapBuilder.builder("x", value).build();
	}

	public static int levenshtein(@NotNull String s, @NotNull String t, boolean ignoreCase) {
		if (ignoreCase) {
			s = s.toLowerCase();
			t = t.toLowerCase();
		}
		if (s.length() == 0) {
			return t.length();
		}
		if (t.length() == 0) {
			return s.length();
		}
		if (s.charAt(0) == t.charAt(0)) {
			return levenshtein(s.substring(1), t.substring(1), ignoreCase);
		}
		int a = levenshtein(s.substring(1), t.substring(1), ignoreCase);
		int b = levenshtein(s, t.substring(1), ignoreCase);
		int c = levenshtein(s.substring(1), t, ignoreCase);
		if (a > b) {
			a = b;
		}
		if (a > c) {
			a = c;
		}
		return a + 1;
	}

	public static String soundex(String s) {
		if (s.isEmpty()) {
			ExprSkuaredError.lastError = "[Soundex] The input cannot be empty";
			return null;
		} else if (s.contains(" ")) {
			ExprSkuaredError.lastError = "[Soundex] The input can't contain spaces";
			return null;
		}
		char[] x = s.toUpperCase().toCharArray();
		char firstLetter = x[0];
		for (int i = 0; i < x.length; i++) {
			switch (x[i]) {
				case 'B':
				case 'F':
				case 'P':
				case 'V':
					x[i] = '1';
					break;
				case 'C':
				case 'G':
				case 'J':
				case 'K':
				case 'Q':
				case 'S':
				case 'X':
				case 'Z':
					x[i] = '2';
					break;
				case 'D':
				case 'T':
					x[i] = '3';
					break;
				case 'L':
					x[i] = '4';
					break;
				case 'M':
				case 'N':
					x[i] = '5';
					break;
				case 'R':
					x[i] = '6';
					break;
				default:
					x[i] = '0';
					break;
			}
		}
		StringBuilder output = new StringBuilder(Character.toString(firstLetter));
		for (int i = 1; i < x.length; i++)
			if (x[i] != x[i - 1] && x[i] != '0') {
				output.append(x[i]);
			}
		output.append("0000");
		return output.substring(0, 4);
	}

	private static List<String> processImplicit(@NotNull String orig, List<String> unknownNames) {
		List<String> tokens = StringUtils.getAllMatches(orig.replace(" ", ""), TOKEN_PATTERN);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tokens.size(); i++) {
			String token = tokens.get(i), nextToken = Strings.nullToEmpty(i + 1 < tokens.size() ? tokens.get(i + 1) : null);
			boolean a = StringUtils.isNumeric(token) && ("(".equals(nextToken) || constants.containsKey(nextToken) ||
				(unknownNames.contains(nextToken)));
			boolean b = (constants.containsKey(token) || (unknownNames.contains(token))) && "(".equals(nextToken);
			boolean c = ")".equals(token) && "(".equals(nextToken);
			boolean hasImplicit = a || b || c;
			if (hasImplicit) {
				sb.append(token).append("*");
			} else {
				sb.append(token);
			}
		}
		return StringUtils.getAllMatches(sb.toString(), TOKEN_PATTERN);
	}

	@Nullable
	public static Pair<@Nullable MathTerm, List<String>> parseMathExpression(@NotNull String orig, List<String> unknownNames, boolean simplify) {
		return parseMathExpression(processImplicit(orig, unknownNames), unknownNames, simplify);
	}

	@Nullable
	private static Pair<@Nullable MathTerm, List<String>> parseMathExpression(@NotNull List<String> tokens, List<String> unknownNames, boolean simplify) throws ArithmeticException {
		List<String> output = new ArrayList<>();
		Stack<String> stack = new Stack<>();
		for (int i = 0; i < tokens.size(); i++) {
			String token = tokens.get(i), nextToken = i + 1 < tokens.size() ? tokens.get(i + 1) : null;
			if (arithmeticOperators.containsKey(token)) {
				Operator currentOp = arithmeticOperators.get(token);
				if (!stack.isEmpty()) {
					String top = stack.peek();
					if (arithmeticOperators.containsKey(top)) {
						while (!stack.isEmpty()) {
							Operator op = arithmeticOperators.get(top);
							if (currentOp == null || op == null) {
								break;
							}
							if (currentOp.getAssociativity() == Associativity.LEFT) {
								if (currentOp.getPrecedence() >= op.getPrecedence()) {
									output.add(stack.pop());
								} else {
									break;
								}
							} else {
								if (currentOp.getPrecedence() > op.getPrecedence()) {
									output.add(stack.pop());
								} else {
									break;
								}
							}
						}
					}
				}
				stack.add(token);
			} else if (NAME_PATTERN.matcher(token).matches() && !"(".equals(nextToken)) {
				String name = token.toLowerCase();
				if (unknownNames.contains(token)) {
					output.add(token);
				} else if (constants.containsKey(name)) {
					Number c = constants.get(name);
					output.add(StringUtils.toString(c));
				} else {
					parseError("Unknown constant : " + token);
					return null;
				}
			} else if (NAME_PATTERN.matcher(token).matches()) {
				Function<?> func = Functions.getFunction(token);
				if (MathUtils.checkFunction(func)) {
					stack.add(func.getName());
					continue;
				}
				parseError("Unknown function : " + token);
				return null;
			} else if (octPattern.matcher(token).matches()) {
				output.add(StringUtils.toString(StringUtils.parseOctal(token.substring(1))));
			} else if (StringUtils.isNumeric(token)) {
				output.add(token);
			} else if (hexPattern.matcher(token).matches()) {
				output.add(StringUtils.toString(StringUtils.parseHex(token.substring(2))));
			} else if (binaryPattern.matcher(token).matches()) {
				output.add(StringUtils.toString(StringUtils.parseBin(token.substring(2))));
			} else if (token.equals("(")) {
				stack.add("(");
			} else if (token.equals(")")) {
				if (!stack.isEmpty()) {
					while (true) {
						if (stack.isEmpty()) {
							parseError("Mismatched parentheses");
							return null;
						}
						String s = stack.peek();
						if (s.equals("(")) {
							stack.pop();
							if (!stack.isEmpty() && NAME_PATTERN.matcher(stack.peek()).matches()) {
								output.add(stack.pop());
							}
							break;
						} else {
							output.add(stack.pop());
						}
					}
				}
			} else if (token.equals(",")) {
				while (true) {
					if (stack.isEmpty()) {
						parseError("Misplaced comma");
						return null;
					}
					String c = stack.peek();
					if (c.equals("(") && NAME_PATTERN.matcher(stack.elementAt(stack.size() - 2)).matches()) {
						break;
					} else {
						output.add(stack.pop());
					}
				}
			} else {
				parseError("Unknown operator : " + token);
				return null;
			}
		}
		for (String s : Lists.reverse(stack)) {
			if (s.equals("(")) {
				parseError("Mismatched parentheses");
				return null;
			}
			output.add(s);
		}
		parseError(null);
		MathTerm term = convertToMathTerm(output, unknownNames);
		return new Pair<>(simplify ? term.simplify() : term, output);
	}

	@Contract("null, _ -> null")
	private static MathTerm convertToMathTerm(List<String> tokens, List<String> unknownData) {
		if (tokens == null) {
			return null;
		}
		Stack<Object> parts = prepareForTree(tokens, unknownData);
		Stack<Object> stack = new Stack<>();
		for (Object part : parts) {
			if (part instanceof Constant || part instanceof Unknown) {
				stack.push(part);
			} else {
				String s = (String) part;
				if (arithmeticOperators.containsKey(s)) {
					MathTerm b = (MathTerm) stack.pop(), a = (MathTerm) stack.pop();
					Operator op = arithmeticOperators.get(s);
					try {
						Constructor<? extends DoubleOperandTerm> constructor = (Constructor<? extends DoubleOperandTerm>) op.getOperation().getConstructors()[0];
						DoubleOperandTerm operator = constructor.newInstance(a, b);
						stack.push(operator);
					} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
						parseError("Failed to instantiate class for operator '" + op.getSymbol() + "'");
						return null;
					}
				} else if (NAME_PATTERN.matcher(s).matches()) {
					Function<Number> func = (Function<Number>) Functions.getFunction(s);
					assert func != null && MathUtils.checkFunction(func);
					List<MathTerm> params = new ArrayList<>();
					for (int i = 0; i < func.getMaxParameters(); i++) {
						params.add(0, (MathTerm) stack.pop());
					}
					stack.push(new MathFunction(func, params));
				}
			}
		}
		assert stack.size() == 1;
		return (MathTerm) stack.pop();
	}

	private static Stack<Object> prepareForTree(List<String> tokens, List<String> unknownData) {
		Stack<Object> converted = new Stack<>();
		for (String token : tokens) {
			MathTerm term = MathTerm.parse(token, unknownData);
			if (term != null) {
				converted.add(term);
			} else {
				converted.add(token);
			}
		}
		return converted;
	}

	public static void parseError(@Nullable String error) {
		ExprSkuaredError.lastError = error != null ? "[Skuared parsing] " + error : null;
	}

	public static void evalError(@Nullable String error) {
		ExprSkuaredError.lastError = error != null ? "[Skuared evaluation] " + error : null;
	}

	public static Number evaluate(@NotNull String expr, Map<String, ? extends Number> unknownData) {
		return evaluatePostfix(parseMathExpression(expr, new ArrayList<>(unknownData.keySet()), true), unknownData);
	}

	@Nullable
	@Contract("null, _ -> null")
	private static Number evaluatePostfix(Pair<@Nullable MathTerm, List<String>> pair, Map<String, ? extends Number> unknownData) {
		MathTerm term = pair.getFirst();
		if (term == null) {
			return null;
		}
		return term.compute(unknownData);
	}

	public static String tokensToString(@NotNull List<String> list) {
		return list.stream().collect(Collectors.joining(" "));
	}

	public static String sendWolframApiRequest(String message) {
		String wolframID;
		if ((wolframID = Skuared.getInstance().getWolframId()) == null) {
			ExprSkuaredError.lastError = "No valid WolframAlpha App ID was provided";
			return null;
		}
		URL apiUrl;
		try {
			String urlEncoded = URLEncoder.encode(message, "UTF-8");
			apiUrl = new URL(String.format("https://api.wolframalpha.com/v1/result?i=%s&appid=%s", urlEncoded, wolframID));
		} catch (MalformedURLException | UnsupportedEncodingException ex) {
			ex.printStackTrace();
			return null;
		}
		try (BufferedReader in = new BufferedReader(new InputStreamReader(apiUrl.openStream()))) {
			return in.readLine();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String sendLimitRequest(String message) {
		String wolframID;
		if ((wolframID = Skuared.getInstance().getWolframId()) == null) {
			ExprSkuaredError.lastError = "No valid WolframAlpha App ID was provided";
			return null;
		}
		URL apiUrl;
		try {
			String urlEncoded = URLEncoder.encode(message, "UTF-8");
			apiUrl = new URL(String.format("https://api.wolframalpha.com/v2/query?input=%s&output=JSON&appid=%s", urlEncoded, wolframID));
		} catch (MalformedURLException | UnsupportedEncodingException ex) {
			ex.printStackTrace();
			return null;
		}
		try (BufferedReader in = new BufferedReader(new InputStreamReader(apiUrl.openStream()))) {
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			String content = sb.toString();
			Gson gson = new Gson();
			JsonObject obj = gson.fromJson(content, JsonObject.class);
			String limitResult = obj.getAsJsonObject("queryresult")
									.getAsJsonArray("pods")
									.get(0)
									.getAsJsonObject()
									.getAsJsonArray("subpods")
									.get(0)
									.getAsJsonObject()
									.get("plaintext")
									.getAsString();
			return limitResult.split("=")[1].trim();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
