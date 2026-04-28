package org.antoined.vaadin.pixelcharts;

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
 * Pixel-art bar chart — equalizer-style columns of stacked pixel blocks.
 * <p>
 * Each bar is a column of square pixels drawn bottom-up, with height proportional
 * to the item's value (auto-normalized to the max). Colors cycle through the
 * resolved palette.
 * <p>
 * Colors resolve in order: {@link #setColors} &gt; {@code --pixel-chart-color-N} CSS vars
 * &gt; {@code --lumo-primary/success/error/warning-color}.
 *
 * <pre>{@code
 * var chart = new PixelBarChart();
 * chart.setHeight("200px");
 * chart.setItems(List.of(new BarItem("A", 0.8), new BarItem("B", 0.5)));
 * chart.setGlowIntensity(0.6);
 * }</pre>
 */
@Tag("pixel-bar-chart")
@JsModule("./pixel-charts/pixel-bar-chart.ts")
public class PixelBarChart extends Component implements HasSize, HasStyle {

    /** Fires on click with the nearest data point's index, value, and label. */
    public Registration addClickListener(ComponentEventListener<PixelChartClickEvent> listener) {
        return addListener(PixelChartClickEvent.class, listener);
    }

    /** Fires on mouse move over the chart with the nearest data point's index, value, and label. */
    public Registration addHoverListener(ComponentEventListener<PixelChartEvent> listener) {
        return addListener(PixelChartEvent.class, listener);
    }

    /** Overrides the color palette. Pass no arguments to reset to theme defaults. */
    public void setColors(String... colors) {
        getElement().setPropertyJson("colors", PixelChartUtils.stringsToJsonArray(colors));
    }

    /** Gap between pixel blocks in CSS pixels. Default 2. */
    public void setGap(int gap) {
        getElement().setProperty("gap", gap);
    }

    /** Glow intensity (0–1). Controls canvas {@code shadowBlur}. Default 0.5. */
    public void setGlowIntensity(double intensity) {
        getElement().setProperty("glowIntensity", intensity);
    }

    /** Enables the hover crosshair + radial glow highlight effect. Default true. */
    public void setHighlightEnabled(boolean enabled) {
        getElement().setProperty("highlightEnabled", enabled);
    }

    /** Number of cells affected by the radial hover glow. Default 3. */
    public void setHighlightRadius(int radius) {
        getElement().setProperty("highlightRadius", radius);
    }

    /**
     * Sets the bar data. Values are auto-normalized (tallest bar = full height).
     */
    public void setItems(List<BarItem> items) {
        getElement().setPropertyJson("items",
                                     PixelChartUtils.toJsonArray(items, item -> {
                                         var obj = Json.createObject();
                                         obj.put("label", item.label());
                                         obj.put("value", item.value());
                                         return obj;
                                     }));
    }

    /** Corner radius per pixel block. 0 = sharp square, pixelSize/2 = circle. Default 0. */
    public void setPixelRadius(int radius) {
        getElement().setProperty("pixelRadius", radius);
    }

    /** Side length of each pixel block in CSS pixels. Default 8. */
    public void setPixelSize(int px) {
        getElement().setProperty("pixelSize", px);
    }

    /**
     * A single bar in the chart.
     *
     * @param label
     *         display label (returned in hover/click events)
     * @param value
     *         bar height — auto-normalized against the max value in the dataset
     */
    public record BarItem(String label, double value) {}
}
