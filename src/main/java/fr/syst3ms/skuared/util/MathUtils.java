package fr.syst3ms.skuared.util;

import ch.njol.skript.lang.function.Function;
import fr.syst3ms.skuared.Skuared;
import fr.syst3ms.skuared.expressions.ExprSkuaredError;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.logging.Level;
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
            return new BigDecimal(
                    a instanceof BigDecimal ? ((BigDecimal) a).toPlainString() : a.toString()).add(new BigDecimal(
                    b instanceof BigDecimal ? ((BigDecimal) b)
                            .toPlainString() : b.toString())
            );
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
            return new BigDecimal(
                    a instanceof BigDecimal ? ((BigDecimal) a).toPlainString() : a.toString()).subtract(new BigDecimal(
                    b instanceof BigDecimal ? ((BigDecimal) b)
                            .toPlainString() : b.toString())
            );
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
            return new BigDecimal(
                    a instanceof BigDecimal ? ((BigDecimal) a).toPlainString() : a.toString()).multiply(new BigDecimal(
                    b instanceof BigDecimal ? ((BigDecimal) b)
                            .toPlainString() : b.toString())
            );
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
            return new BigDecimal(
                    a instanceof BigDecimal ? ((BigDecimal) a).toPlainString() : a.toString()).divide(new BigDecimal(
                    b instanceof BigDecimal ? ((BigDecimal) b)
                            .toPlainString() : b.toString()), RoundingMode.HALF_UP
            );
        } else {
            return a.longValue() / b.longValue();
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
            return new BigDecimal(
                    a instanceof BigDecimal ? ((BigDecimal) a).toPlainString() : a.toString()).remainder(new BigDecimal(
                    b instanceof BigDecimal ? ((BigDecimal) b)
                            .toPlainString() : b.toString())
            );
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
    static boolean checkFunction(@Nullable Function<?> func) {
        return func != null && (func.getMaxParameters() == 1 && !ReflectionUtils.isSingle(func.getParameter(0)) ||
                Stream.of(func.getParameters())
                        .allMatch(p -> Number.class.isAssignableFrom(p.getType().getC()) &&
                                ReflectionUtils.isSingle(p)));
    }

    private static BigInteger pow(BigInteger base, BigInteger exponent) {
        BigInteger result = BigInteger.ONE;
        while (exponent.signum() > 0) {
            if (exponent.testBit(0))
                result = result.multiply(base);
            base = base.multiply(base);
            exponent = exponent.shiftRight(1);
        }
        return result;
    }

    public static Number sigma(String expression, long start, long end) {
        Skuared.getInstance().getLogger().log(Level.INFO, "Starting sigma");
        BigDecimal result = BigDecimal.ZERO;
        for (long i = start; i <= end; i++) {
            Algorithms.registerConstant("x", i);
            Number n = Algorithms.evaluate(expression);
            if (n == null) {
                ExprSkuaredError.lastError = "Invalid sigma expression (Error : " + ExprSkuaredError.lastError + ")";
                return Double.NaN;
            }
            result = result.add(new BigDecimal(n.toString()));
        }
        Algorithms.getConstants().remove("x");
        return result;
    }

    public static Number chainedProduct(String expression, long start, long end) {
        Skuared.getInstance().getLogger().log(Level.INFO, "Starting chain product");
        BigDecimal result = BigDecimal.ONE;
        for (long i = start; i <= end; i++) {
            Algorithms.registerConstant("x", i);
            Number n = Algorithms.evaluate(expression);
            if (n == null) {
                ExprSkuaredError.lastError = "Invalid product expression (Error : " + ExprSkuaredError.lastError + ")";
                return Double.NaN;
            }
            result = result.multiply(new BigDecimal(n.toString()));
        }
        Algorithms.getConstants().remove("x");
        return result;
    }

    public static double gamma(double x) {
        double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
        double ser = 1.0 + 76.18009173    / (x + 0)   - 86.50532033    / (x + 1)
                     + 24.01409822    / (x + 2)   -  1.231739516   / (x + 3)
                     +  0.00120858003 / (x + 4)   -  0.00000536382 / (x + 5);
        double log = tmp + Math.log(ser * Math.sqrt(2 * Math.PI));
        return Math.exp(log);
    }
}
