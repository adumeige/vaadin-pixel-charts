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
 * Pixel-art heatmap — 2D grid of pixels where color alpha represents value intensity.
 * Glow is applied only on high-value cells (&gt;0.5) for performance.
 */
@Tag("pixel-heatmap")
@JsModule("./pixel-charts/pixel-heatmap.ts")
public class PixelHeatmap extends Component implements HasSize, HasStyle {

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

    /** Sets the grid dimensions. Must match the row/col range in the data. */
    public void setGridSize(int rows, int cols) {
        getElement().setProperty("rows", rows);
        getElement().setProperty("cols", cols);
    }

    public void setHighlightEnabled(boolean enabled) {
        getElement().setProperty("highlightEnabled", enabled);
    }

    public void setHighlightRadius(int radius) {
        getElement().setProperty("highlightRadius", radius);
    }

    /** Sets the heatmap data as a sparse list of cells. Missing cells render empty. */
    public void setItems(List<HeatmapCell> items) {
        getElement().setPropertyJson("items",
                                     PixelChartUtils.toJsonArray(items, cell -> {
                                         var obj = Json.createObject();
                                         obj.put("row", cell.row());
                                         obj.put("col", cell.col());
                                         obj.put("value", cell.value());
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
     * A single cell in the heatmap grid.
     *
     * @param row
     *         row index (0-based, top to bottom)
     * @param col
     *         column index (0-based, left to right)
     * @param value
     *         intensity (0–1), mapped to color alpha
     */
    public record HeatmapCell(int row, int col, double value) {}
}
