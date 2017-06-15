package fr.syst3ms.skuared.util;

import ch.njol.skript.lang.function.Parameter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

/**
 * Created by ARTHUR on 10/06/2017.
 */
public class ReflectionUtils {
    public static boolean isSingle(@NotNull Parameter<?> param) {
        Class<?> parameterClass = param.getClass();
        try {
            Field f = parameterClass.getDeclaredField("single");
            f.setAccessible(true);
            return f.getBoolean(param);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return true; // Doesn't make sense, but this most likely will never happen
        }
    }

}
