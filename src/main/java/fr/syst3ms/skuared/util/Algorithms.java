package fr.syst3ms.skuared.util;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.function.Function;
import ch.njol.skript.lang.function.Functions;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class Algorithms {
    @NotNull
    public static Pattern NAME_PATTERN = Pattern.compile("[A-Za-z][A-Za-z\\d]*");
    @NotNull
    private static String TOKEN_PATTERN = "(?i)(?:(?<=[^\\w]|^)[+-])?(?:0[0-7]+|0x[0-9a-f]+|0b[01]+|\\d+(?:\\.\\d+)?)|[()]|([^\\w ()])\\1*|[a-z_][a-z\\d]*";
    @NotNull
    private static Map<String, Operator<Number, Number, Number>> arithmeticOperators = new HashMap<>();
    @NotNull
    private static Map<String, Number> constants = new HashMap<>();
    @NotNull
    private static Pattern binaryPattern = Pattern.compile("0[Bb][01]+"),
            hexPattern = Pattern.compile("0[Xx]\\p{XDigit}+"),
            octPattern = Pattern.compile("0[0-8]+");

    public static void registerOperator(@NotNull String symbol, @NotNull BiFunction<Number, Number, Number> operation, @NotNull Associativity associativity, int precedence) {
        arithmeticOperators.put(symbol, new Operator<>(symbol, precedence, associativity, operation));
    }

    public static void registerConstant(@NotNull String id, Number value) {
        constants.put(id.toLowerCase(), value);
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

    private static List<String> processImplicit(@NotNull String orig) {
        List<String> tokens = StringUtils.getAllMatches(orig.replace(" ", ""), TOKEN_PATTERN);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i), nextToken = i + 1 < tokens.size() ? tokens.get(i + 1) : null;
            if (StringUtils.isNumeric(token) &&
                (nextToken != null && NAME_PATTERN.matcher(nextToken).matches() || "(".equals(nextToken)) ||
                ")".equals(token) && "(".equals(nextToken)) {
                sb.append(token).append("*");
            } else {
                sb.append(token);
            }
        }
        return StringUtils.getAllMatches(sb.toString(), TOKEN_PATTERN);
    }

    @Nullable
    public static List<String> shuntingYard(@NotNull String orig) {
        return shuntingYard(processImplicit(orig));
    }

    @Nullable
    private static List<String> shuntingYard(@NotNull List<String> tokens) throws ArithmeticException {
        List<String> output = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i), nextToken = i + 1 < tokens.size() ? tokens.get(i + 1) : null;
            if (arithmeticOperators.containsKey(token)) {
                Operator<?, ?, ?> currentOp = arithmeticOperators.get(token);
                if (!stack.isEmpty()) {
                    String top = stack.peek();
                    if (arithmeticOperators.containsKey(top)) {
                        while (!stack.isEmpty()) {
                            Operator<?, ?, ?> op = arithmeticOperators.get(top);
                            if (currentOp == null || op == null)
                                break;
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
                Number c = constants.get(token.toLowerCase());
                if (c == null) {
                    parseError("Unknown constant : " + token);
                    return null;
                }
                output.add(StringUtils.toString(c));
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
        return output;
    }

    private static void parseError(String error) {
        Skript.error("[Skuared parsing] " + error);
    }

    private static void evalError(String error) {
        Skript.error("[Skuared evaluation] " + error);
    }

    @Nullable
    public static Number evaluate(@NotNull String expr) {
        return evaluateRpn(shuntingYard(expr));
    }

    @Nullable
    @Contract("null -> null")
    public static Number evaluateRpn(@Nullable List<String> rpn) {
        if (rpn == null)
            return null;
        Stack<Number> stack = new Stack<>();
        for (String s : rpn) {
            if (StringUtils.isNumeric(s)) {
                stack.push(StringUtils.parseNumber(s));
            } else if (arithmeticOperators.containsKey(s)) {
                Number a = stack.pop(),
                        b = stack.pop();
                Operator<Number, Number, Number> op = arithmeticOperators.get(s);
                stack.push(op.getOperation().apply(b, a));
            } else if (NAME_PATTERN.matcher(s).matches()) {
                Function<Number> func = (Function<Number>) Functions.getFunction(s);
                if (func.getMaxParameters() > 1 && !ReflectionUtils.isSingle(func.getParameter(0)) &&
                    func.getMaxParameters() != stack.size()) {
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
                if (func.getName().equals("tan") && params.length == 1 && params[0][0].intValue() == 90) {
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
