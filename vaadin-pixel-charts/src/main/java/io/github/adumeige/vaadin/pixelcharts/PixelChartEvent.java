package io.github.adumeige.vaadin.pixelcharts;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;

/**
 * Fired on mouse move over any pixel chart. Contains the canvas position
 * and the resolved data point (if any) under the cursor.
 * <p>
 * Use {@link #isOnData()} to check whether the cursor is over a data point.
 * When off-data, index is -1 and value/label are null.
 */
@DomEvent("pixel-hover")
public class PixelChartEvent extends ComponentEvent<Component> {

    private final double x;
    private final double y;
    private final int index;
    private final Double value;
    private final String label;

    public PixelChartEvent(Component source, boolean fromClient,
                           @EventData("event.detail.x") double x,
                           @EventData("event.detail.y") double y,
                           @EventData("event.detail.index") int index,
                           @EventData("event.detail.value") Double value,
                           @EventData("event.detail.label") String label) {
        super(source, fromClient);
        this.x = x;
        this.y = y;
        this.index = index;
        this.value = value;
        this.label = label;
    }

    /** Data point index, or -1 if off-data. */
    public int getIndex() {return index;}

    /** Data point label, or null if off-data. */
    public String getLabel() {return label;}

    /** Data point value, or null if off-data. */
    public Double getValue() {return value;}

    /** Canvas x position in CSS pixels. */
    public double getX() {return x;}

    /** Canvas y position in CSS pixels. */
    public double getY() {return y;}

    /** True when the cursor is over a data point. */
    public boolean isOnData() {return index >= 0;}
}
