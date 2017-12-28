package fr.syst3ms.skuared.util;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder<K, V> {
	private Map<K, V> inner = new HashMap<>();

	private MapBuilder(K key, V value) {
		inner.put(key, value);
	}

	public static <K, V> MapBuilder<K, V> builder(K key, V value) {
		return new MapBuilder<>(key, value);
	}

	public MapBuilder<K, V> add(K key, V value) {
		inner.put(key, value);
		return this;
	}

	public Map<K, V> build() {
		return inner;
	}
}
