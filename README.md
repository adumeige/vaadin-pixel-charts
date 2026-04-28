# vaadin-pixel-charts

Pixel-art chart components for Vaadin — bar, sparkline, heatmap, scatter. Canvas-rendered with configurable pixel size,
gap, corner radius, glow intensity, and hover crosshair. Theme-aware via `--lumo-*` CSS variables.

![Demo](resources/demo.png)

Although the component should work out of the box with standard Lumo themes, it's meant to be used with themes providing
a bit more "pop" to the screen.
Exemples with themes from https://github.com/adumeige/vaadin-themes :

**Fjord**

![Fjord theme](resources/demo-themes-fjord.png)

**Terminal Synth**

![Terminal Synth theme](resources/demo-themes-terminal-synth.png)

## Modules

| Module                       | Description                                    |
|------------------------------|------------------------------------------------|
| `vaadin-pixel-charts`        | Component library (Java + LitElement/TS)       |
| `vaadin-pixel-charts-karibu` | Karibu DSL extension functions (Kotlin)        |
| `vaadin-pixel-charts-demo`   | Spring Boot demo app with live data simulation |

## Usage

```xml

<dependency>
    <groupId>org.antoined</groupId>
    <artifactId>vaadin-pixel-charts</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

```java
var chart = new PixelBarChart();
chart.

setItems(List.of(new BarItem("A", 0.8), new

BarItem("B",0.5)));
        chart.

setPixelSize(8);
chart.

setGap(2);
chart.

setGlowIntensity(0.6);
chart.

setPixelRadius(0);       // 0 = sharp squares, pixelSize/2 = circles
chart.

setHighlightEnabled(true);
chart.

addHoverListener(e ->{ /* e.getIndex(), e.getValue(), e.getLabel() */ });
```

### Karibu DSL

```xml

<dependency>
    <groupId>org.antoined</groupId>
    <artifactId>vaadin-pixel-charts-karibu</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

```kotlin
pixelBarChart {
    setHeight("200px")
    setGlowIntensity(0.8)
    setItems(listOf(BarItem("A", 0.9), BarItem("B", 0.5)))
}
```

## Chart types

- **PixelBarChart** — equalizer-style stacked pixel columns. Data: `List<BarItem(label, value)>`
- **PixelSparkline** — line with Bresenham interpolation + optional area fill. Data: `List<Double>`
- **PixelHeatmap** — 2D grid, color intensity by value. Data: `List<HeatmapCell(row, col, value)>`
- **PixelScatter** — positioned dots, variable size/color. Data: `List<ScatterPoint(x, y, size, colorIndex)>`

## Properties

| Property           | Default | Description                                        |
|--------------------|---------|----------------------------------------------------|
| `pixelSize`        | 8       | Side length of each pixel block (px)               |
| `gap`              | 2       | Space between blocks (px)                          |
| `glowIntensity`    | 0.5     | Glow strength (0–1), uses canvas `shadowBlur`      |
| `pixelRadius`      | 0       | Corner radius per pixel (0 = square, max = circle) |
| `highlightEnabled` | true    | Hover crosshair + radial glow effect               |
| `highlightRadius`  | 3       | Cells affected by radial hover glow                |
| `colors`           | theme   | Custom colors; falls back to `--lumo-*` variables  |
| `fillArea`         | true    | Sparkline only — fill below the line               |

## Colors

Colors resolve in order: `setColors(...)` > `--pixel-chart-color-1..5` CSS vars >
`--lumo-primary/success/error/warning-color`. Works with any Vaadin theme.

## Build

```sh
mvn install
cd vaadin-pixel-charts-demo
mvn spring-boot:run
```
