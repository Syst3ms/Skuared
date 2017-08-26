package fr.syst3ms.skuared.util.math;

import org.jetbrains.annotations.NotNull;

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
			return a.doubleValue() - b.doubleValue();
		} else {
			return a.longValue() - b.longValue();
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
			return a.doubleValue() * b.doubleValue();
		} else {
			return a.longValue() * b.longValue();
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
		return Math.pow(a.doubleValue(), b.doubleValue());
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
		}
		return a.longValue() << b.longValue();
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

}