package fr.syst3ms.skuared.util;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.function.Function;
import ch.njol.skript.lang.function.Functions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import fr.syst3ms.skuared.Skuared;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class Algorithms {
    @NotNull
    private static Map<String, Operator<Number, Number, Number>> arithmeticOperators = new HashMap<>();
    @NotNull
    private static Map<String, UnaryOperator<Number>> unaryOperators = new HashMap<>();
    @NotNull
    private static Map<String, Number> constants = new HashMap<>();
    @NotNull
    private static Pattern namePattern = Pattern.compile("[A-Za-z][A-Za-z\\d]*"),
                           binaryPattern = Pattern.compile("0[Bb][01]+"),
                           hexPattern = Pattern.compile("0[Xx]\\p{XDigit}+"),
                           octPattern = Pattern.compile("0[0-8]+");

    public static void registerOperator(@NotNull String symbol, @NotNull BiFunction<Number, Number, Number> operation, @NotNull Operator.Associativity associativity, int precedence) {
        arithmeticOperators.put(symbol, new Operator<>(symbol, precedence, associativity, operation));
    }

    public static void registerConstant(@NotNull String id, Number value) {
        constants.put(id.toLowerCase(), value);
    }

    @NotNull
    public static List<Operator<?, ?, ?>> getArithmeticOperators() {
        return new ArrayList<>(arithmeticOperators.values());
    }

    public static int levenshtein(@NotNull String s, @NotNull String t, boolean ignoreCase) {
        if (ignoreCase) {
            s = s.toLowerCase();
            t = t.toLowerCase();
        }
        if (s.length() == 0) return t.length();
        if (t.length() == 0) return s.length();
        if (s.charAt(0) == t.charAt(0))
            return levenshtein(s.substring(1), t.substring(1), ignoreCase);
        int a = levenshtein(s.substring(1), t.substring(1), ignoreCase);
        int b = levenshtein(s, t.substring(1), ignoreCase);
        int c = levenshtein(s.substring(1), t, ignoreCase);
        if (a > b) a = b;
        if (a > c) a = c;
        return a + 1;
    }

    @NotNull
    public static List<String> shuntingYard(@NotNull String orig) throws ArithmeticException {
        orig = orig.replace(" ", "").toLowerCase();
        List<String> tokens = StringUtils.getAllMatches(orig, "\\w+|[()]|[^\\w ()]+"),
                output = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        @Nullable String lastToken = null;
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i),
                   nextToken = i + 1 < stack.size() ? tokens.get(i + 1) : null;
            if (token.equals("-") && (lastToken == null || arithmeticOperators.containsKey(lastToken)) && nextToken != null && StringUtils.isNumeric(nextToken)) {
                tokens.add(i + 2, ")");
                tokens.add(i, "0");
                tokens.add(i--, "(");
                continue;
            } else if (arithmeticOperators.containsKey(token)) {
                Operator<?, ?, ?> currentOp = arithmeticOperators.get(token);
                if (!stack.isEmpty()) {
                    String top = stack.peek();
                    if (arithmeticOperators.containsKey(top)) {
                        while (!stack.isEmpty()) {
                            Operator<?, ?, ?> op = arithmeticOperators.get(top);
                            if (currentOp == null || op == null) {
                                parseError("Unknown operator : " + token);
                                return Collections.singletonList("NaN");
                            }
                            if (currentOp.getAssociativity() == Operator.Associativity.LEFT) {
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
            } else if (namePattern.matcher(token).matches() && !tokens.get(i + 1).equals("(")) {
                Number c = constants.get(token.toLowerCase());
                if (c == null) {
                    parseError("Unknown constant : " + token);
                    return Collections.singletonList("NaN");
                }
                output.add(StringUtils.toString(c));
            } else if (namePattern.matcher(token).matches()) {
                Function<?> func = Functions.getFunction(token);
                if (MathUtils.checkFunction(func)) {
                    stack.add(func.getName());
                    continue;
                }
                parseError("Unknown function : " + token);
                return Collections.singletonList("NaN");
            } else if (octPattern.matcher(token).matches()) {
                output.add(StringUtils.toString(StringUtils.parseOctal(token.substring(1))));
            }  else if (StringUtils.isNumeric(token)) {
                output.add(token);
            } else if (hexPattern.matcher(token).matches()) {
                output.add(StringUtils.toString(StringUtils.parseHex(token.substring(2))));
            } else if (binaryPattern.matcher(token).matches()) {
                output.add(StringUtils.toString(StringUtils.parseBin(token.substring(2))));
            } else if (token.equals("(")) {
                if (lastToken != null && (StringUtils.isNumeric(lastToken) || lastToken.equals(")"))) {
                    tokens.add(i--, "*");
                    continue;
                }
                stack.add("(");
            } else if (token.equals(")")) {
                if (!stack.isEmpty()) {
                    while (true) {
                        if (stack.isEmpty()) {
                            parseError("Mismatched parentheses");
                            return Collections.singletonList("NaN");
                        }
                        String s = stack.peek();
                        if (s.equals("(")) {
                            stack.pop();
                            if (!stack.isEmpty()) {
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
                        return Collections.singletonList("NaN");
                    }
                    String c = stack.peek();
                    if (c.equals("(") && namePattern.matcher(stack.elementAt(stack.size() - 2)).matches()) {
                        break;
                    } else {
                        output.add(stack.pop());
                    }
                }
            }
            lastToken = token;
        }
        for (String s : Lists.reverse(stack)) {
            if (s.equals("(")) {
                parseError("Mismatched parentheses");
                return Collections.singletonList("NaN");
            }
            output.add(s);
        }
        return output;
    }

    public static void parseError(String error) {
        Skript.error("[Skuared parsing] " + error);
    }

    public static void evalError(String error) {
        Skript.error("[Skuared evaluation] " + error);
    }

    public static Number evaluateRpn(@NotNull List<String> rpn) {
        if (rpn.size() == 1 && rpn.get(0).equals("NaN"))
            return Double.NaN;
        Stack<Number> stack = new Stack<>();
        for (String s : rpn) {
            if (StringUtils.isNumeric(s)) {
                stack.push(StringUtils.parseNumber(s));
            } else if (arithmeticOperators.containsKey(s)) {
                Number a = stack.pop(),
                       b = stack.pop();
                Operator<Number, Number, Number> op = arithmeticOperators.get(s);
                stack.push(op.getOperation().apply(a, b));
            } else if (namePattern.matcher(s).matches()) {
                Function<Number> func = (Function<Number>) Functions.getFunction(s);
                if ((func.getMaxParameters() > 1 && !ReflectionUtils.isSingle(func.getParameter(0))) && func.getMaxParameters() != stack.size()) {
                    evalError("Wrong number of parameters for the '" + func.getName() + "' function");
                    return Double.NaN;
                }
                Number[][] params = new Number[func.getMaxParameters()][func.getMaxParameters()];
                if (func.getMaxParameters() == 1 && ReflectionUtils.isSingle(func.getParameter(0))) {
                    for (int i = 0; i < func.getMaxParameters(); i++) {
                        params[0][i] =
                                stack.pop();
                    }
                } else {
                    for (int i = 0; i < func.getMaxParameters(); i++) {
                        params[i][0] = stack.pop();
                    }
                }
                if (func.getName().equals("tan") && (params.length == 1 && params[0][0].intValue() == 90)) {
                    stack.push(Double.POSITIVE_INFINITY);
                    continue;
                }
                stack.push(func.execute(params)[0]);
            }
        }
        return stack.pop();
    }

    public static String tokensToString(@NotNull List<String> list) {
        return list.stream().collect(Collectors.joining(" "));
    }
}
