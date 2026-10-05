package io.github.adumeige.vaadin.pixelcharts;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

import java.util.List;
import java.util.function.Function;

/** Internal helpers for serializing chart data to Vaadin's elemental JSON format. */
public final class PixelChartUtils {

    private PixelChartUtils() {}

    public static JsonArray stringsToJsonArray(String... values) {
        var array = Json.createArray();
        for (int i = 0; i < values.length; i++) {
            array.set(i, values[i]);
        }
        return array;
    }

    public static JsonArray toJsonArray(double[] values) {
        var array = Json.createArray();
        for (int i = 0; i < values.length; i++) {
            array.set(i, values[i]);
        }
        return array;
    }

    public static JsonArray toJsonArray(List<Double> values) {
        var array = Json.createArray();
        for (int i = 0; i < values.size(); i++) {
            array.set(i, values.get(i));
        }
        return array;
    }

    /** Maps a list of objects to a JsonArray using a per-item mapper. */
    public static <T> JsonArray toJsonArray(List<T> items, Function<T, JsonObject> mapper) {
        var array = Json.createArray();
        for (int i = 0; i < items.size(); i++) {
            array.set(i, mapper.apply(items.get(i)));
        }
        return array;
    }
}
