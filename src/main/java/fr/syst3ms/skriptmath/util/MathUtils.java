package fr.syst3ms.skriptmath.util;

import ch.njol.skript.lang.function.Function;
import ch.njol.skript.lang.function.Functions;
import fr.syst3ms.skriptmath.expressions.ExprSkriptMathError;
import fr.syst3ms.skriptmath.util.evaluation.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class MathUtils {
    public static final Double PHI = (1.0d + Math.sqrt(5.0d)) / 2.0d;

    public static Number plus(@NotNull Number a, @NotNull Number b) {
        if (a.doubleValue() == Double.POSITIVE_INFINITY || b.doubleValue() == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        } else if (a.doubleValue() == Double.NEGATIVE_INFINITY || b.doubleValue() == Double.NEGATIVE_INFINITY) {
            return Double.NEGATIVE_INFINITY;
        } else if (Double.isNaN(a.doubleValue()) || Double.isNaN(b.doubleValue())) {
            return Double.NaN;
        } else if (a instanceof Double || a instanceof Float || b instanceof Double || b instanceof Float) {
            return a.doubleValue() + b.doubleValue();
        } else if (a instanceof BigInteger || b instanceof BigInteger) {
            return new BigInteger(a.toString()).add(new BigInteger(b.toString()));
        } else if (a instanceof BigDecimal || b instanceof BigDecimal) {
            return new BigDecimal(a instanceof BigDecimal ? ((BigDecimal) a).toPlainString() : a.toString()).add(new BigDecimal(b instanceof BigDecimal ? ((BigDecimal) b)
                .toPlainString() : b.toString()));
        } else {
            try {
                return Math.addExact(a.longValue(), b.longValue());
            } catch (ArithmeticException e) {
                return Double.POSITIVE_INFINITY;
            }
        }
    }

    public static Number minus(@NotNull Number a, @NotNull Number b) {
        if (a.doubleValue() == Double.POSITIVE_INFINITY || b.doubleValue() == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        } else if (a.doubleValue() == Double.NEGATIVE_INFINITY || b.doubleValue() == Double.NEGATIVE_INFINITY) {
            return Double.NEGATIVE_INFINITY;
        } else if (Double.isNaN(a.doubleValue()) || Double.isNaN(b.doubleValue())) {
            return Double.NaN;
        } else if (a instanceof Double || a instanceof Float || b instanceof Double || b instanceof Float) {
            return a.doubleValue() - b.doubleValue();
        } else if (a instanceof BigInteger || b instanceof BigInteger) {
            return new BigInteger(a.toString()).subtract(new BigInteger(b.toString()));
        } else if (a instanceof BigDecimal || b instanceof BigDecimal) {
            return new BigDecimal(a instanceof BigDecimal ? ((BigDecimal) a).toPlainString() : a.toString()).subtract(new BigDecimal(b instanceof BigDecimal ? ((BigDecimal) b)
                .toPlainString() : b.toString()));
        } else {
            try {
                return Math.subtractExact(a.longValue(), b.longValue());
            } catch (ArithmeticException e) {
                if (Long.signum(a.longValue()) == -1 || Long.signum(b.longValue()) == -1) {
                    return Double.NEGATIVE_INFINITY;
                } else {
                    return Double.POSITIVE_INFINITY;
                }
            }
        }
    }

    public static Number times(@NotNull Number a, @NotNull Number b) {
        if (a.doubleValue() == Double.POSITIVE_INFINITY || b.doubleValue() == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        } else if (a.doubleValue() == Double.NEGATIVE_INFINITY || b.doubleValue() == Double.NEGATIVE_INFINITY) {
            return Double.NEGATIVE_INFINITY;
        } else if (Double.isNaN(a.doubleValue()) || Double.isNaN(b.doubleValue())) {
            return Double.NaN;
        } else if (a instanceof Double || a instanceof Float || b instanceof Double || b instanceof Float) {
            return a.doubleValue() * b.doubleValue();
        } else if (a instanceof BigInteger || b instanceof BigInteger) {
            return new BigInteger(a.toString()).multiply(new BigInteger(b.toString()));
        } else if (a instanceof BigDecimal || b instanceof BigDecimal) {
            return new BigDecimal(a instanceof BigDecimal ? ((BigDecimal) a).toPlainString() : a.toString()).multiply(new BigDecimal(b instanceof BigDecimal ? ((BigDecimal) b)
                .toPlainString() : b.toString()));
        } else {
            try {
                return Math.multiplyExact(a.longValue(), b.longValue());
            } catch (ArithmeticException e) {
                if (Long.signum(a.longValue()) == -1 || Long.signum(b.longValue()) == -1) {
                    return Double.NEGATIVE_INFINITY;
                } else {
                    return Double.POSITIVE_INFINITY;
                }
            }
        }
    }

    public static Number divide(@NotNull Number a, @NotNull Number b) {
        if (a.doubleValue() == Double.POSITIVE_INFINITY || b.doubleValue() == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        } else if (a.doubleValue() == Double.NEGATIVE_INFINITY || b.doubleValue() == Double.NEGATIVE_INFINITY) {
            return Double.NEGATIVE_INFINITY;
        } else if (Double.isNaN(a.doubleValue()) || Double.isNaN(b.doubleValue())) {
            return Double.NaN;
        } else if (a instanceof Double || a instanceof Float || b instanceof Double || b instanceof Float) {
            return a.doubleValue() / b.doubleValue();
        } else if (a instanceof BigInteger || b instanceof BigInteger) {
            return new BigInteger(a.toString()).divide(new BigInteger(b.toString()));
        } else if (a instanceof BigDecimal || b instanceof BigDecimal) {
            return new BigDecimal(a instanceof BigDecimal ? ((BigDecimal) a).toPlainString() : a.toString()).divide(new BigDecimal(b instanceof BigDecimal ? ((BigDecimal) b)
                .toPlainString() : b.toString()), RoundingMode.HALF_UP);
        } else {
            return a.doubleValue() / b.doubleValue();
        }
    }

    public static Number mod(@NotNull Number a, @NotNull Number b) {
        if (a.doubleValue() == Double.POSITIVE_INFINITY || b.doubleValue() == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        } else if (a.doubleValue() == Double.NEGATIVE_INFINITY || b.doubleValue() == Double.NEGATIVE_INFINITY) {
            return Double.NEGATIVE_INFINITY;
        } else if (Double.isNaN(a.doubleValue()) || Double.isNaN(b.doubleValue())) {
            return Double.NaN;
        } else if (a instanceof Double || a instanceof Float || b instanceof Double || b instanceof Float) {
            return a.doubleValue() % b.doubleValue();
        } else if (a instanceof BigInteger || b instanceof BigInteger) {
            return new BigInteger(a.toString()).mod(new BigInteger(b.toString()));
        } else if (a instanceof BigDecimal || b instanceof BigDecimal) {
            return new BigDecimal(a instanceof BigDecimal ? ((BigDecimal) a).toPlainString() : a.toString()).remainder(new BigDecimal(b instanceof BigDecimal ? ((BigDecimal) b)
                .toPlainString() : b.toString()));
        } else {
            return a.longValue() % b.longValue();
        }
    }

    public static Number pow(@NotNull Number a, @NotNull Number b) {
        if (a.doubleValue() == Double.POSITIVE_INFINITY || b.doubleValue() == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        } else if (a.doubleValue() == Double.NEGATIVE_INFINITY || b.doubleValue() == Double.NEGATIVE_INFINITY) {
            return Double.NEGATIVE_INFINITY;
        } else if (Double.isNaN(a.doubleValue()) || Double.isNaN(b.doubleValue())) {
            return Double.NaN;
        } else if (a instanceof BigInteger || b instanceof BigInteger) {
            return pow(new BigInteger(a.toString()), new BigInteger(b.toString()));
        } else if (a instanceof BigDecimal || b instanceof BigDecimal) {
            return new BigDecimal(a.toString()).pow(new BigDecimal(b.toString()).intValueExact());
        } else {
            return Math.pow(a.doubleValue(), b.doubleValue());
        }
    }

    public static Number shr(@NotNull Number a, @NotNull Number b) {
        if (a.doubleValue() == Double.POSITIVE_INFINITY || b.doubleValue() == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        } else if (a.doubleValue() == Double.NEGATIVE_INFINITY || b.doubleValue() == Double.NEGATIVE_INFINITY) {
            return Double.NEGATIVE_INFINITY;
        } else if (Double.isNaN(a.doubleValue()) || Double.isNaN(b.doubleValue())) {
            return Double.NaN;
        }
        return a.longValue() >> b.longValue();
    }

    public static Number shl(@NotNull Number a, @NotNull Number b) {
        if (a.doubleValue() == Double.POSITIVE_INFINITY || b.doubleValue() == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        } else if (a.doubleValue() == Double.NEGATIVE_INFINITY || b.doubleValue() == Double.NEGATIVE_INFINITY) {
            return Double.NEGATIVE_INFINITY;
        } else if (Double.isNaN(a.doubleValue()) || Double.isNaN(b.doubleValue())) {
            return Double.NaN;
        } else if (Long.bitCount(a.longValue()) + b.longValue() > 64) {
            return Double.POSITIVE_INFINITY;
        } else {
            return a.longValue() << b.longValue();
        }
    }

    public static Number ushr(@NotNull Number a, @NotNull Number b) {
        if (a.doubleValue() == Double.POSITIVE_INFINITY || b.doubleValue() == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        } else if (a.doubleValue() == Double.NEGATIVE_INFINITY || b.doubleValue() == Double.NEGATIVE_INFINITY) {
            return Double.NEGATIVE_INFINITY;
        } else if (Double.isNaN(a.doubleValue()) || Double.isNaN(b.doubleValue())) {
            return Double.NaN;
        }
        return a.longValue() >>> b.longValue();
    }

    @Contract("null -> false")
    public static boolean checkFunction(@Nullable Function<?> func) {
        return func != null && (func.getMaxParameters() == 1 && !ReflectionUtils.isSingle(func.getParameter(0)) || Stream
            .of(func.getParameters())
            .allMatch(p -> Number.class.isAssignableFrom(p.getType().getC()) && ReflectionUtils.isSingle(p)));
    }

    private static BigInteger pow(BigInteger base, BigInteger exponent) {
        BigInteger result = BigInteger.ONE;
        while (exponent.signum() > 0) {
            if (exponent.testBit(0)) result = result.multiply(base);
            base = base.multiply(base);
            exponent = exponent.shiftRight(1);
        }
        return result;
    }

    public static Number sigma(String expression, long start, long end) {
        BigDecimal result = BigDecimal.ZERO;
        for (long i = start; i <= end; i++) {
            Number n = Algorithms.evaluate(expression, Algorithms.getXMap(i));
            if (n == null) {
                ExprSkriptMathError.lastError = "Invalid sigma expression (Error : " + ExprSkriptMathError.lastError + ")";
                return Double.NaN;
            }
            result = result.add(new BigDecimal(n.toString()));
        }
        Algorithms.getConstants().remove("x");
        return result;
    }

    public static Number chainedProduct(String expression, long start, long end) {
        BigDecimal result = BigDecimal.ONE;
        for (long i = start; i <= end; i++) {
            Number n = Algorithms.evaluate(expression, Algorithms.getXMap(i));
            if (n == null) {
                ExprSkriptMathError.lastError = "Invalid product expression (Error : " + ExprSkriptMathError.lastError + ")";
                return Double.NaN;
            }
            result = result.multiply(new BigDecimal(n.toString()));
        }
        Algorithms.getConstants().remove("x");
        return result;
    }

    public static double gamma(double x) {
        double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
        double ser = 1.0 + 76.18009173 / (x + 0) - 86.50532033 / (x + 1) + 24.01409822 / (x + 2) - 1.231739516 / (x + 3) + 0.00120858003 / (x + 4) - 0.00000536382 / (x + 5);
        double log = tmp + Math.log(ser * Math.sqrt(2 * Math.PI));
        return Math.exp(log);
    }

    public static double digamma(double x) {
        double result = 0.0;
        assert(x > 0.0);
        for (; x < 7.0; ++x)
            result -= 1.0/x;
        x -= 1.0/2.0;
        double xx = 1.0/x;
        double xx2 = xx*xx;
        double xx4 = xx2*xx2;
        result += Math.log(x)+(1.0/24.0)*xx2-(7.0/960.0)*xx4+(31.0/8064.0)*xx4*xx2-(127.0/30720.0)*xx4*xx4;
        return result;
    }

    public static double csc(double x) {
        return 1 / Math.sin(Math.toRadians(x));
    }

    public static double sec(double x) {
        return 1 / Math.cos(Math.toRadians(x));
    }

    public static double cot(double x) {
        return 1 / Math.tan(Math.toRadians(x));
    }

    public static double acsc(double x) {
        return Math.asin(Math.toRadians(1.0d / x));
    }

    public static double asec(double x) {
        return Math.acos(Math.toRadians(1.0d / x));
    }

    public static double acot(double x) {
        return Math.atan(Math.toRadians(1.0d / x));
    }

    public static boolean equals(Number a, Number b) {
        if (a instanceof BigDecimal || b instanceof BigDecimal) {
            return new BigDecimal(a.toString()).equals(new BigDecimal(b.toString()));
        } else if (a instanceof BigInteger || b instanceof BigInteger) {
            return new BigInteger(a.toString()).equals(new BigInteger(b.toString()));
        } else if (a instanceof Double || b instanceof Double) {
            return a.doubleValue() == b.doubleValue();
        } else if (a instanceof Long || b instanceof Long) {
            return a.longValue() == b.longValue();
        } else {
            return a.doubleValue() == b.doubleValue();
        }
    }

    @SuppressWarnings("unchecked")
    public static MathTerm indefiniteDerivative(MathTerm term) {
        term = term.simplify();
        if (term instanceof Constant) {
            return Constant.ZERO;
        } else if (term instanceof Unknown) {
            return Constant.ONE;
        } else if (term instanceof Sum) {
            Sum sum = (Sum) term;
            return new Sum(indefiniteDerivative(sum.getFirst()), indefiniteDerivative(sum.getSecond()));
        } else if (term instanceof Difference) {
            Difference difference = (Difference) term;
            return new Difference(indefiniteDerivative(difference.getFirst()), indefiniteDerivative(difference.getSecond()));
        } else if (term instanceof Product) {
            Product product = (Product) term;
            MathTerm first = product.getFirst();
            MathTerm second = product.getSecond();
            if (first instanceof Constant || second instanceof Constant) {
                if (first instanceof Constant) {
                    return new Product(first, indefiniteDerivative(second)).simplify();
                } else {
                    return new Product(second, indefiniteDerivative(first)).simplify();
                }
            } else {
                return new Sum(new Product(first, indefiniteDerivative(second)), new Product(indefiniteDerivative(first), second));
            }
        } else if (term instanceof Division) {
            Division division = (Division) term;
            MathTerm first = division.getFirst();
            MathTerm second = division.getSecond();
            if (first instanceof Constant && second.hasUnknown()) {
                return new Division(first, second.getSquared()).getNegative();
            } else {
                return new Division(
                    new Difference(new Product(indefiniteDerivative(first), second), new Product(indefiniteDerivative(second), first)),
                    second.getSquared()
                ).simplify();
            }
        } else if (term instanceof Power) {
            Power power = (Power) term;
            MathTerm first = power.getFirst();
            MathTerm second = power.getSecond();
            if (first.hasUnknown() && !second.hasUnknown()) {
                return new Product(second, new Power(first, new Difference(second, Constant.ONE))).simplify();
            } else if (second.equals(Constant.getConstant(-1))) {
                return first.getSquared().getReciprocal().getNegative();
            } else if (first == Constant.E) {
                if (second instanceof Unknown) {
                    return term;
                } else if (!second.hasUnknown()) {
                    return Constant.ZERO;
                } else {
                    return new Product(term, indefiniteDerivative(second)).simplify();
                }
            } else if (first instanceof Constant) {
                if (second instanceof Unknown) {
                    return new Product(term, new MathFunction((Function<Number>) Functions.getFunction("ln"), Collections.singletonList(first)));
                } else if (second.hasUnknown()) {
                    new Product(new Product(new MathFunction((Function<Number>) Functions.getFunction("ln"), Collections.singletonList(first)), term), indefiniteDerivative(second));
                }
            }
        } else if (term instanceof MathFunction) {
            MathFunction func = (MathFunction) term;
            List<MathTerm> params = func.getParams();
            String name = func.getFunction().getName();
            MathTerm firstParam = params.get(0);
            MathTerm secondParam = params.size() == 2 ? params.get(1) : null;
            if (firstParam.hasUnknown()) {
                switch (name) {
                    case "sin":
                        return MathFunction.getFunctionByName("cos", params);
                    case "cos":
                        return MathFunction.getFunctionByName("sin", params).getNegative();
                    case "tan":
                        return MathFunction.getFunctionByName("sec", params).getSquared();
                    case "csc":
                        return new Product(MathFunction.getFunctionByName("csc", params).getNegative(), MathFunction.getFunctionByName("cot", params));
                    case "sec":
                        return new Product(MathFunction.getFunctionByName("sec", params), MathFunction.getFunctionByName("tan", params));
                    case "cot":
                        return MathFunction.getFunctionByName("csc", params).getSquared().getNegative();
                    case "asin":
                        return new Difference(Constant.ONE, firstParam.getSquared()).getSquareRoot().getReciprocal();
                    case "acos":
                        return new Difference(Constant.ONE, firstParam.getSquared()).getSquareRoot().getReciprocal().getNegative();
                    case "atan":
                        return new Sum(Constant.ONE, firstParam.getSquared()).getReciprocal();
                    case "acsc":
                        return new Product(firstParam, new Difference(firstParam.getSquared(), Constant.ONE).getSquareRoot()).getReciprocal().getNegative();
                    case "asec":
                        return new Product(firstParam, new Difference(firstParam.getSquared(), Constant.ONE).getSquareRoot()).getReciprocal();
                    case "acot":
                        return new Sum(Constant.ONE, firstParam.getSquared()).getReciprocal().getNegative();
                    case "ln":
                        if (firstParam instanceof Unknown)
                            return firstParam.getReciprocal();
                        else
                            return new Division(indefiniteDerivative(firstParam), firstParam);
                    case "log":
                        if (secondParam == null) {
                            if (firstParam instanceof Unknown)
                                return new Product(firstParam, MathFunction.getFunctionByName("ln", Collections.singletonList(Constant.getConstant(10)))).getReciprocal();
                            else
                                return new Division(indefiniteDerivative(firstParam), new Product(firstParam, MathFunction.getFunctionByName("ln", Collections.singletonList(Constant.getConstant(10)))));
                        } else {
                            if (firstParam instanceof Unknown)
                                return new Product(secondParam, MathFunction.getFunctionByName("ln", Collections.singletonList(firstParam))).getReciprocal();
                            else
                                return new Division(indefiniteDerivative(secondParam), new Product(secondParam, MathFunction.getFunctionByName("ln", Collections.singletonList(firstParam))));
                        }
                    case "abs":
                        return new Division(firstParam, term);
                    case "atan2":
                        assert secondParam != null;
                        return new Division(secondParam, new Sum(firstParam.getSquared(), secondParam.getSquared()));
                    case "gamma":
                        return new Product(term, MathFunction.getFunctionByName("digamma", params));
                    case "factorial":
                        return new Product(
                            MathFunction.getFunctionByName("gamma", Collections.singletonList(new Sum(firstParam, Constant.ONE))),
                            MathFunction.getFunctionByName("digamma", Collections.singletonList(new Sum(firstParam, Constant.ONE)))
                        );
                    case "digamma":
                        ExprSkriptMathError.lastError = "skript-math cannot compute the derivative of the digamma function !";
                        return null;
                    default:
                        ExprSkriptMathError.lastError = "Unknown function in derivative";
                        return null;
                }
            }
        }
        Algorithms.evalError("Invalid operator in derivative");
        return null;
    }
}