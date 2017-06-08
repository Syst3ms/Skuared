package fr.syst3ms.skuared.util;

import ch.njol.skript.classes.data.DefaultFunctions;
import ch.njol.skript.lang.function.Function;
import ch.njol.skript.lang.function.Functions;
import com.google.common.collect.Lists;
import fr.syst3ms.skuared.Skuared;

import java.util.*;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class Algorithms {
    private static Map<String, Operator<?, ?, ?>> operators = new HashMap<>();

    public static <T, U, R> void registerOperator(String symbol, BiFunction<T, U, R> operation, Operator.Associativity associativity, int precedence) {
        operators.put(symbol, new Operator<>(symbol, precedence, associativity, operation));
    }

    public static List<Operator<?, ?, ?>> getOperators() {
        return new ArrayList<>(operators.values());
    }

    public static int levenshtein(String s, String t, boolean ignoreCase) {
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

    public static List<String> shuntingYard(String orig) throws ArithmeticException {
        orig = orig.replace(" ", "").toLowerCase();
        List<String> tokens = StringUtils.getAllMatches(orig, "\\w+|[()]|[^\\w ()]+"),
                output = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        Pattern funcNamePattern = Pattern.compile("[A-Za-z][A-Za-z\\d]+");
        for (String token : tokens) {
            if (operators.containsKey(token)) {
                Operator<?, ?, ?> currentOp = operators.get(token);
                if (!stack.isEmpty()) {
                    String top = stack.peek();
                    if (operators.containsKey(top)) {
                        while (!stack.isEmpty()) {
                            Operator<?, ?, ?> op = operators.get(top);
                            if (currentOp== null || op == null) {
                                throw new ArithmeticException("Unknown operator : " + token);
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
            } else if (funcNamePattern.matcher(token).matches()) {
                Function<?> func = Functions.getFunction(token);
                if (func != null && func.getReturnType() != null && Number.class.isAssignableFrom(func.getReturnType().getC())) {
                    if (Stream.of(func.getParameters()).allMatch(p -> Number.class.isAssignableFrom(p.getType().getC()))) {
                        stack.add(func.getName());
                        continue;
                    }
                }
                throw new ArithmeticException("Unknown function : " + token);
            } else if (StringUtils.isNumber(token)) {
                output.add(token);
            } else if (token.equals("(")) {
                stack.add("(");
            } else if (token.equals(")")) {
                if (!stack.isEmpty()) {
                    while (true) {
                        if (stack.isEmpty())
                            throw new ArithmeticException("Mismatched parentheses");
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
                    if (stack.isEmpty())
                        throw new ArithmeticException("Misplaced comma");
                    String c = stack.peek();
                    if (c.equals("(") && funcNamePattern.matcher(stack.elementAt(stack.size() - 2)).matches()) {
                        break;
                    } else {
                        output.add(stack.pop());
                    }
                }
            }
        }
        for (String s : Lists.reverse(stack)) {
            if (s.equals("("))
                throw new ArithmeticException("Mismatched parentheses");
            output.add(s);
        }
        return output;
    }

    public static String tokensToString(List<String> list) {
        return list.stream().collect(Collectors.joining(" "));
    }
}
