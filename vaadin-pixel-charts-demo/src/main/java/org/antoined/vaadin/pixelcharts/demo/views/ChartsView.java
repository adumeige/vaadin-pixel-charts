package org.antoined.vaadin.pixelcharts.demo.views;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.antoined.vaadin.pixelcharts.PixelBarChart;
import org.antoined.vaadin.pixelcharts.PixelHeatmap;
import org.antoined.vaadin.pixelcharts.PixelScatter;
import org.antoined.vaadin.pixelcharts.PixelSparkline;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Route("")
@PageTitle("Pixel Charts Demo")
public class ChartsView extends VerticalLayout {

    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r -> {
        var t = new Thread(r, "chart-sim");
        t.setDaemon(true);
        return t;
    });
    private final Random rng = new Random(42);
    private final List<ChartRef> allCharts = new ArrayList<>();
    private final double[] barValues = {0.92, 0.78, 0.45, 0.88, 0.31, 0.67, 0.95, 0.12, 0.53, 0.81};
    private final String[] barLabels = {"ARCTIC-7", "BOREAL-3", "CIPHER-X", "DELTA-9", "ECHO-11", "FROST-2", "GHOST-4", "HELIX-6", "IRIS-15", "JADE-1"};
    private final ArrayList<Double> sparklineValues = new ArrayList<>();
    private PixelBarChart barChart;
    private PixelSparkline sparklineChart;
    private ScheduledFuture<?> simulationTask;

    public ChartsView() {
        setPadding(true);
        setSpacing(true);

        add(new H2("Pixel Charts Demo"));
        add(buildControls());
        add(new Hr());

        double v = 0.3;
        for (int i = 0; i < 48; i++) {
            v += (rng.nextDouble() - 0.45) * 0.12;
            v = Math.max(0.05, Math.min(1.0, v));
            sparklineValues.add(v);
        }

        var grid = new FlexLayout();
        grid.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        grid.getStyle().set("gap", "var(--lumo-space-m)");

        grid.add(createBarChartCard());
        grid.add(createSparklineCard());
        grid.add(createHeatmapCard());
        grid.add(createScatterCard());

        add(grid);
    }

    private HorizontalLayout buildControls() {
        var row = new HorizontalLayout();
        row.setAlignItems(Alignment.BASELINE);
        row.getStyle().set("flex-wrap", "wrap").set("gap", "var(--lumo-space-s)");

        var pixelSize = new IntegerField("Pixel Size");
        pixelSize.setValue(8);
        pixelSize.setMin(2);
        pixelSize.setMax(24);
        pixelSize.setStepButtonsVisible(true);
        pixelSize.setWidth("120px");
        pixelSize.addValueChangeListener(e -> {if (e.getValue() != null) allCharts.forEach(c -> c.setPixelSize.apply(e.getValue()));});

        var gap = new IntegerField("Gap");
        gap.setValue(2);
        gap.setMin(0);
        gap.setMax(8);
        gap.setStepButtonsVisible(true);
        gap.setWidth("100px");
        gap.addValueChangeListener(e -> {if (e.getValue() != null) allCharts.forEach(c -> c.setGap.apply(e.getValue()));});

        var glow = new NumberField("Glow");
        glow.setValue(0.5);
        glow.setMin(0);
        glow.setMax(1);
        glow.setStep(0.1);
        glow.setStepButtonsVisible(true);
        glow.setWidth("120px");
        glow.addValueChangeListener(e -> {if (e.getValue() != null) allCharts.forEach(c -> c.setGlow.apply(e.getValue()));});

        var radius = new IntegerField("Pixel Radius");
        radius.setValue(0);
        radius.setMin(0);
        radius.setMax(12);
        radius.setStepButtonsVisible(true);
        radius.setWidth("130px");
        radius.addValueChangeListener(e -> {if (e.getValue() != null) allCharts.forEach(c -> c.setRadius.apply(e.getValue()));});

        var colors = new TextField("Colors");
        colors.setPlaceholder("#ff4dd2, #ffae3d, #6dff8a");
        colors.setHelperText("Comma-separated hex colors");
        colors.setWidth("280px");
        colors.setClearButtonVisible(true);
        colors.addValueChangeListener(e -> {
            var val = e.getValue();
            if (val == null || val.isBlank()) {
                allCharts.forEach(c -> c.setColors.apply());
                return;
            }
            allCharts.forEach(c -> c.setColors.apply(val.split("\\s*,\\s*")));
        });

        row.add(pixelSize, gap, glow, radius, colors);
        return row;
    }

    private Div createBarChartCard() {
        barChart = new PixelBarChart();
        barChart.setHeight("200px");
        barChart.setWidthFull();
        var items = new ArrayList<PixelBarChart.BarItem>();
        for (int i = 0; i < barValues.length; i++) items.add(new PixelBarChart.BarItem(barLabels[i], barValues[i]));
        barChart.setItems(items);
        register(barChart, barChart::setPixelSize, barChart::setGap, barChart::setGlowIntensity, barChart::setPixelRadius, barChart::setColors);
        var result = wrapCard("Signal Strength (live)", "Bar chart — equalizer style, updating every 800ms", barChart);
        barChart.addHoverListener(e -> result.hoverInfo.setText(e.isOnData()
                                                                ? "x=%.0f y=%.0f | #%d %s = %.2f".formatted(e.getX(), e.getY(), e.getIndex(), e.getLabel(), e.getValue())
                                                                : "Hover over chart..."));
        return result.card;
    }

    private Div createHeatmapCard() {
        var chart = new PixelHeatmap();
        chart.setHeight("200px");
        chart.setWidthFull();
        chart.setGridSize(7, 24);
        chart.setPixelSize(6);
        chart.setGap(2);
        var cells = new ArrayList<PixelHeatmap.HeatmapCell>();
        for (int day = 0; day < 7; day++)
            for (int hour = 0; hour < 24; hour++) {
                double base = (day < 5 && hour >= 8 && hour <= 18) ? 0.6 : 0.15;
                cells.add(new PixelHeatmap.HeatmapCell(day, hour, Math.min(1.0, base + rng.nextDouble() * 0.4)));
            }
        chart.setItems(cells);
        register(chart, chart::setPixelSize, chart::setGap, chart::setGlowIntensity, chart::setPixelRadius, chart::setColors);
        var result = wrapCard("Activity Matrix", "Heatmap — 7 days × 24 hours", chart);
        chart.addHoverListener(e -> result.hoverInfo.setText(e.isOnData()
                                                             ? "x=%.0f y=%.0f | %s value=%.2f".formatted(e.getX(), e.getY(), e.getLabel(), e.getValue())
                                                             : "Hover over chart..."));
        return result.card;
    }

    private Div createScatterCard() {
        var chart = new PixelScatter();
        chart.setHeight("200px");
        chart.setWidthFull();
        var points = new ArrayList<PixelScatter.ScatterPoint>();
        for (int i = 0; i < 30; i++)
            points.add(new PixelScatter.ScatterPoint(rng.nextDouble(), rng.nextDouble(), 1 + rng.nextInt(3), rng.nextInt(4)));
        chart.setItems(points);
        register(chart, chart::setPixelSize, chart::setGap, chart::setGlowIntensity, chart::setPixelRadius, chart::setColors);
        var result = wrapCard("Signal Map", "Scatter — position, size, color", chart);
        chart.addHoverListener(e -> result.hoverInfo.setText(e.isOnData()
                                                             ? "x=%.0f y=%.0f | #%d %s size=%.0f".formatted(e.getX(), e.getY(), e.getIndex(), e.getLabel(), e.getValue())
                                                             : "Hover over chart..."));
        return result.card;
    }

    private Div createSparklineCard() {
        sparklineChart = new PixelSparkline();
        sparklineChart.setHeight("200px");
        sparklineChart.setWidthFull();
        sparklineChart.setFillArea(true);
        sparklineChart.setItems(new ArrayList<>(sparklineValues));
        register(sparklineChart, sparklineChart::setPixelSize, sparklineChart::setGap, sparklineChart::setGlowIntensity, sparklineChart::setPixelRadius, sparklineChart::setColors);
        var result = wrapCard("Throughput (live)", "Sparkline — scrolling time series, 800ms tick", sparklineChart);
        sparklineChart.addHoverListener(e -> result.hoverInfo.setText(e.isOnData()
                                                                      ? "x=%.0f y=%.0f | index=%d value=%.3f".formatted(e.getX(), e.getY(), e.getIndex(), e.getValue())
                                                                      : "Hover over chart..."));
        return result.card;
    }

    @Override
    protected void onAttach(AttachEvent event) {
        super.onAttach(event);
        var ui = event.getUI();
        simulationTask = executor.scheduleAtFixedRate(() -> tick(ui), 100, 100, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void onDetach(DetachEvent event) {
        super.onDetach(event);
        if (simulationTask != null) {
            simulationTask.cancel(false);
            simulationTask = null;
        }
    }

    private void register(Component chart, SetPixelSize ps, SetGap g, SetGlow gl, SetRadius r, SetColors c) {
        allCharts.add(new ChartRef(chart, ps, g, gl, r, c));
    }

    private void tick(UI ui) {
        for (int i = 0; i < barValues.length; i++) {
            barValues[i] += (rng.nextDouble() - 0.5) * 0.15;
            barValues[i] = Math.max(0.05, Math.min(1.0, barValues[i]));
        }
        double last = sparklineValues.getLast();
        last += (rng.nextDouble() - 0.48) * 0.1;
        last = Math.max(0.05, Math.min(1.0, last));
        sparklineValues.removeFirst();
        sparklineValues.add(last);

        ui.access(() -> {
            var barItems = new ArrayList<PixelBarChart.BarItem>();
            for (int i = 0; i < barValues.length; i++) {
                barItems.add(new PixelBarChart.BarItem(barLabels[i], barValues[i]));
            }
            barChart.setItems(barItems);
            sparklineChart.setItems(new ArrayList<>(sparklineValues));
        });
    }

    private CardResult wrapCard(String title, String subtitle, Component chart) {
        var card = new Div();
        card.getStyle()
            .set("background", "var(--lumo-base-color)")
            .set("border", "1px solid var(--lumo-contrast-20pct)")
            .set("border-radius", "var(--lumo-border-radius-l)")
            .set("padding", "var(--lumo-space-m)")
            .set("min-width", "340px")
            .set("flex", "1 1 45%")
            .set("box-shadow", "var(--lumo-box-shadow-xs)");
        var titleSpan = new H3(title);
        titleSpan.getStyle().set("margin", "0 0 var(--lumo-space-xs) 0");
        var subtitleSpan = new Span(subtitle);
        subtitleSpan.getStyle().set("font-size", "var(--lumo-font-size-xs)").set("color", "var(--lumo-tertiary-text-color)").set("display", "block").set("margin-bottom", "var(--lumo-space-s)");
        var hoverInfo = new Span("Hover over chart...");
        hoverInfo.getStyle()
                 .set("font-size", "var(--lumo-font-size-xs)")
                 .set("color", "var(--lumo-tertiary-text-color)")
                 .set("display", "block")
                 .set("margin-top", "var(--lumo-space-xs)")
                 .set("min-height", "1.4em")
                 .set("font-variant-numeric", "tabular-nums");
        card.add(titleSpan, subtitleSpan, chart, hoverInfo);
        return new CardResult(card, hoverInfo);
    }

    @FunctionalInterface interface SetColors {
        void apply(String... c);
    }

    @FunctionalInterface interface SetGap {
        void apply(int v);
    }

    @FunctionalInterface interface SetGlow {
        void apply(double v);
    }

    @FunctionalInterface interface SetPixelSize {
        void apply(int v);
    }

    @FunctionalInterface interface SetRadius {
        void apply(int v);
    }

    private record CardResult(Div card, Span hoverInfo) {}

    private record ChartRef(Component chart, SetPixelSize setPixelSize, SetGap setGap, SetGlow setGlow, SetRadius setRadius, SetColors setColors) {}
}
