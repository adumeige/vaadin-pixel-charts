package org.antoined.vaadin.pixelcharts;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;

/** Fired on click over any pixel chart. Same payload as {@link PixelChartEvent}. */
@DomEvent("pixel-click")
public class PixelChartClickEvent extends ComponentEvent<Component> {

    private final double x;
    private final double y;
    private final int index;
    private final Double value;
    private final String label;

    public PixelChartClickEvent(Component source, boolean fromClient,
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

    public int getIndex() {return index;}

    public String getLabel() {return label;}

    public Double getValue() {return value;}

    public double getX() {return x;}

    public double getY() {return y;}

    public boolean isOnData() {return index >= 0;}
}
