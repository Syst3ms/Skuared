package fr.syst3ms.skuared.util;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.function.Parameter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class ReflectionUtils {
    private static Field single;

    static {
        Field _single = null;
        try {
            _single = Parameter.class.getDeclaredField("single");
            _single.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Skript.error("Couldn't access Skript's 'single' field. Something fucked up basically.");
        }
        single = _single;
    }

    public static boolean isSingle(@NotNull Parameter<?> param) {
        Class<?> parameterClass = param.getClass();
        try {
            return single.getBoolean(param);
        } catch (@NotNull IllegalAccessException e) {
            return true; // Doesn't make sense, but this most likely will never happen
        }
    }

}
