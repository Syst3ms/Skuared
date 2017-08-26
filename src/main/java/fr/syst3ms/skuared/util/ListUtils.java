package fr.syst3ms.skuared.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Stack;

public class ListUtils {
	@Nullable
	public static <T> T peekOrNull(@NotNull Stack<T> stack) {
		return stack.isEmpty() ? null : stack.peek();
	}

	@Nullable
	public static <T> T getOrNull(@NotNull List<T> list, int i) {
		return i < list.size() ? list.get(i) : null;
	}
}
