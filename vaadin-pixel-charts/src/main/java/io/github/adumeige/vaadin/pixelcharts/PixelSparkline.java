package io.github.adumeige.vaadin.pixelcharts;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.shared.Registration;

import java.util.List;

/**
 * Pixel-art sparkline — line chart where each point is a pixel block,
 * connected via Bresenham interpolation. Optional area fill below the line.
 */
@Tag("pixel-sparkline")
@JsModule("./pixel-charts/pixel-sparkline.ts")
public class PixelSparkline extends Component implements HasSize, HasStyle {

    public Registration addClickListener(ComponentEventListener<PixelChartClickEvent> listener) {
        return addListener(PixelChartClickEvent.class, listener);
    }

    public Registration addHoverListener(ComponentEventListener<PixelChartEvent> listener) {
        return addListener(PixelChartEvent.class, listener);
    }

    public void setColors(String... colors) {
        getElement().setPropertyJson("colors", PixelChartUtils.stringsToJsonArray(colors));
    }

    /** When true, fills the area below the line at 15% opacity. Default true. */
    public void setFillArea(boolean fill) {
        getElement().setProperty("fillArea", fill);
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

    /** Sets the data as a list of y-values (evenly spaced on the x-axis). */
    public void setItems(List<Double> values) {
        getElement().setPropertyJson("items", PixelChartUtils.toJsonArray(values));
    }

    /** Sets the data as a varargs array of y-values. */
    public void setItems(double... values) {
        getElement().setPropertyJson("items", PixelChartUtils.toJsonArray(values));
    }

    public void setPixelRadius(int radius) {
        getElement().setProperty("pixelRadius", radius);
    }

    public void setPixelSize(int px) {
        getElement().setProperty("pixelSize", px);
    }
}
