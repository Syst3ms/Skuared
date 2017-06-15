package fr.syst3ms.skuared.util;

import ch.njol.skript.lang.function.Function;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

/**
 * Created by ARTHUR on 10/06/2017.
 */
public class MathUtils {
    public static Number plus(@NotNull Number a, @NotNull Number b) {
        if (a.doubleValue() == Double.POSITIVE_INFINITY || b.doubleValue() == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        } else if (a.doubleValue() == Double.NEGATIVE_INFINITY || b.doubleValue() == Double.NEGATIVE_INFINITY) {
            return Double.NEGATIVE_INFINITY;
        } else if (Double.isNaN(a.doubleValue()) || Double.isNaN(b.doubleValue())) {
            return Double.NaN;
        }
        if ((a instanceof Double || a instanceof Float) && (b instanceof Double || b instanceof Float)) {
            return a.doubleValue() + b.doubleValue();
        } else {
            return a.longValue() + b.longValue();
        }
    }

    public static Number minus(@NotNull Number a, @NotNull Number b) {
        if (a.doubleValue() == Double.POSITIVE_INFINITY || b.doubleValue() == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        } else if (a.doubleValue() == Double.NEGATIVE_INFINITY || b.doubleValue() == Double.NEGATIVE_INFINITY) {
            return Double.NEGATIVE_INFINITY;
        } else if (Double.isNaN(a.doubleValue()) || Double.isNaN(b.doubleValue())) {
            return Double.NaN;
        }
        if ((a instanceof Double || a instanceof Float) && (b instanceof Double || b instanceof Float)) {
            return b.doubleValue() - a.doubleValue();
        } else {
            return b.longValue() - a.longValue();
        }
    }

    public static Number times(@NotNull Number a, @NotNull Number b) {
        if (a.doubleValue() == Double.POSITIVE_INFINITY || b.doubleValue() == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        } else if (a.doubleValue() == Double.NEGATIVE_INFINITY || b.doubleValue() == Double.NEGATIVE_INFINITY) {
            return Double.NEGATIVE_INFINITY;
        } else if (Double.isNaN(a.doubleValue()) || Double.isNaN(b.doubleValue())) {
            return Double.NaN;
        }
        if ((a instanceof Double || a instanceof Float) && (b instanceof Double || b instanceof Float)) {
            return b.doubleValue() * a.doubleValue();
        } else {
            return b.longValue() * a.longValue();
        }
    }

    public static Number divide(@NotNull Number a, @NotNull Number b) {
        if (a.doubleValue() == Double.POSITIVE_INFINITY || b.doubleValue() == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        } else if (a.doubleValue() == Double.NEGATIVE_INFINITY || b.doubleValue() == Double.NEGATIVE_INFINITY) {
            return Double.NEGATIVE_INFINITY;
        } else if (Double.isNaN(a.doubleValue()) || Double.isNaN(b.doubleValue())) {
            return Double.NaN;
        }
        if ((a instanceof Double || a instanceof Float) && (b instanceof Double || b instanceof Float)) {
            return a.doubleValue() / b.doubleValue();
        } else {
            return a.longValue() / b.longValue();
        }
    }

    public static Number pow(@NotNull Number a, @NotNull Number b) {
        if (a.doubleValue() == Double.POSITIVE_INFINITY || b.doubleValue() == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        } else if (a.doubleValue() == Double.NEGATIVE_INFINITY || b.doubleValue() == Double.NEGATIVE_INFINITY) {
            return Double.NEGATIVE_INFINITY;
        } else if (Double.isNaN(a.doubleValue()) || Double.isNaN(b.doubleValue())) {
            return Double.NaN;
        }
        return Math.pow(b.doubleValue(), a.doubleValue());
    }

    public static Number shr(@NotNull Number a, @NotNull Number b) {
        if (a.doubleValue() == Double.POSITIVE_INFINITY || b.doubleValue() == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        } else if (a.doubleValue() == Double.NEGATIVE_INFINITY || b.doubleValue() == Double.NEGATIVE_INFINITY) {
            return Double.NEGATIVE_INFINITY;
        } else if (Double.isNaN(a.doubleValue()) || Double.isNaN(b.doubleValue())) {
            return Double.NaN;
        }
        return b.longValue() >> a.longValue();
    }

    public static Number shl(@NotNull Number a, @NotNull Number b) {
        if (a.doubleValue() == Double.POSITIVE_INFINITY || b.doubleValue() == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        } else if (a.doubleValue() == Double.NEGATIVE_INFINITY || b.doubleValue() == Double.NEGATIVE_INFINITY) {
            return Double.NEGATIVE_INFINITY;
        } else if (Double.isNaN(a.doubleValue()) || Double.isNaN(b.doubleValue())) {
            return Double.NaN;
        }
        return b.longValue() << a.longValue();
    }

    public static Number ushr(@NotNull Number a, @NotNull Number b) {
        if (a.doubleValue() == Double.POSITIVE_INFINITY || b.doubleValue() == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        } else if (a.doubleValue() == Double.NEGATIVE_INFINITY || b.doubleValue() == Double.NEGATIVE_INFINITY) {
            return Double.NEGATIVE_INFINITY;
        } else if (Double.isNaN(a.doubleValue()) || Double.isNaN(b.doubleValue())) {
            return Double.NaN;
        }
        return b.longValue() >>> a.longValue();
    }

    @Contract("null -> false")
    public static boolean checkFunction(@Nullable Function<?> func) {
        if (func == null)
            return false;
        if (func.getMaxParameters() == 1 && !ReflectionUtils.isSingle(func.getParameter(0)))
            return true;
        else if (Stream.of(func.getParameters()).allMatch(p -> Number.class.isAssignableFrom(p.getType().getC())
                && ReflectionUtils.isSingle(p)))
            return true;
        else
            return false;
    }
}
