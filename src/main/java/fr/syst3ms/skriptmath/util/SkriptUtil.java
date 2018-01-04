package fr.syst3ms.skriptmath.util;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Variable;
import ch.njol.skript.lang.VariableString;

import java.lang.reflect.Field;

/**
 * With courtesy of Lubbock
 */
public class SkriptUtil {
	static {
		Field _VARIABLE_NAME = null;
		try {
			_VARIABLE_NAME = Variable.class.getDeclaredField("name");
			_VARIABLE_NAME.setAccessible(true);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			Skript.error("Skript's 'variable name' method could not be resolved.");
		}
		VARIABLE_NAME = _VARIABLE_NAME;
	}

	private static final Field VARIABLE_NAME;

	public static VariableString getVariableName(Variable<?> var) {
		try {
			return (VariableString) VARIABLE_NAME.get(var);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}