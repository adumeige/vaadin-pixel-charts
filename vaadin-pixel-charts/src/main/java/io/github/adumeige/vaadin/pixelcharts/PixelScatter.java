package io.github.adumeige.vaadin.pixelcharts;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.shared.Registration;
import elemental.json.Json;

import java.util.List;

/**
 * Pixel-art scatter plot — dots placed at normalized (0–1) coordinates,
 * with variable size (NxN pixel blocks) and color by index into the palette.
 */
@Tag("pixel-scatter")
@JsModule("./pixel-charts/pixel-scatter.ts")
public class PixelScatter extends Component implements HasSize, HasStyle {

    public Registration addClickListener(ComponentEventListener<PixelChartClickEvent> listener) {
        return addListener(PixelChartClickEvent.class, listener);
    }
 
    public Registration addHoverListener(ComponentEventListener<PixelChartEvent> listener) {
        return addListener(PixelChartEvent.class, listener);
    }

    public void setColors(String... colors) {
        getElement().setPropertyJson("colors", PixelChartUtils.stringsToJsonArray(colors));
    }

    public void setGap(int gap) {
        getElement().setProperty("gap", gap);
    }

    public void setGlowIntensity(double intensity) {
        getElement().setProperty("glowIntensity", intensity);
    }

    public void setHighlightEnabled(boolean enabled) {
        getElement().setProperty("highlightEnabled", enabled);
    }

    public void setHighlightRadius(int radius) {
        getElement().setProperty("highlightRadius", radius);
    }

    /** Sets the scatter data. */
    public void setItems(List<ScatterPoint> items) {
        getElement().setPropertyJson("items",
                                     PixelChartUtils.toJsonArray(items, point -> {
                                         var obj = Json.createObject();
                                         obj.put("x", point.x());
                                         obj.put("y", point.y());
                                         obj.put("size", point.size());
                                         obj.put("colorIndex", point.colorIndex());
                                         return obj;
                                     }));
    }

    public void setPixelRadius(int radius) {
        getElement().setProperty("pixelRadius", radius);
    }

    public void setPixelSize(int px) {
        getElement().setProperty("pixelSize", px);
    }

    /**
     * A single dot in the scatter plot.
     *
     * @param x
     *         horizontal position (0 = left, 1 = right)
     * @param y
     *         vertical position (0 = bottom, 1 = top)
     * @param size
     *         dot size in pixel blocks (1 = single block, 2 = 2x2, etc.)
     * @param colorIndex
     *         index into the resolved color palette (wraps)
     */
    public record ScatterPoint(double x, double y, double size, int colorIndex) {}
}
