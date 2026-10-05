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
    <groupId>io.github.adumeige.vaadin-pixel-charts</groupId>
    <artifactId>vaadin-pixel-charts</artifactId>
    <version>1.0.0</version>
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
    <groupId>io.github.adumeige.vaadin-pixel-charts</groupId>
    <artifactId>vaadin-pixel-charts-karibu</artifactId>
    <version>1.0.0</version>
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


## Maven Central and release migration

The examples target release `1.0.0`. Once published, Maven Central serves the
dependencies without extra repository declarations or credentials.

- Maven group: `io.github.adumeige.vaadin-pixel-charts`.
- Java/Kotlin package root: `io.github.adumeige.vaadin.pixelcharts`.
- Library modules: `vaadin-pixel-charts`, `vaadin-pixel-charts-karibu`.
- The demo module `vaadin-pixel-charts-demo` is built in CI and excluded from publication.

Consumers must update their dependency group IDs and package imports.
The project keeps its independent parent; it does not need `agentic-parent`
to be published first. Development POMs use `1.0.0-SNAPSHOT`.

Releases are mirrored to [GitHub Packages](https://github.com/adumeige/vaadin-pixel-charts/packages)
and attached to [GitHub Releases](https://github.com/adumeige/vaadin-pixel-charts/releases).
GitHub Packages requires authenticated Maven downloads; Central is recommended.

### Publishing

Configure the same four repository Actions secrets used by the other projects:

| Secret | Value |
| --- | --- |
| `CENTRAL_USERNAME` | Sonatype Central Portal token username |
| `CENTRAL_PASSWORD` | Sonatype Central Portal token password |
| `GPG_PRIVATE_KEY` | Full ASCII-armored exported private signing key |
| `GPG_PASSPHRASE` | Signing key passphrase |

The `io.github.adumeige` namespace must be verified in Central, and the public
signing key must be on a supported keyserver such as `keyserver.ubuntu.com`.
GitHub publishing uses the built-in `GITHUB_TOKEN`; no extra token is needed.

1. Merge the release changes into `main` and check that CI passes.
2. Open **Actions → Build and publish Vaadin Pixel Charts → Run workflow**.
3. Select `main` and a new release version, initially `1.0.0`.
4. The workflow creates a versioned POM commit, builds and signs once, and
   automatically publishes to Central. It waits up to an hour for publication;
   no final portal **Publish** click is required.
5. It creates an annotated `v<version>` tag and draft GitHub Release, mirrors and
   verifies the exact signed artifacts in GitHub Packages, attaches downloads,
   and makes the release public with generated notes.

The tag points to the release-version commit; `main` keeps its snapshot version.
Pushes and pull requests verify the reactor and library release archives; they do
not publish. Tags do not trigger publication. Published versions are immutable.
Release artifacts include sources and Java Javadoc/Dokka API documentation.

### Recover an interrupted release

Central and GitHub publish sequentially. If Central succeeds but the GitHub job
fails, use **Re-run failed jobs** on the same Actions run. Its signed bundle and
release source are retained for 90 days. The mirror skips byte-identical files
already uploaded and rejects conflicting ones. The release remains a draft until
its packages and assets succeed, although the tag may already be visible.

Do not rerun all jobs or start a fresh run for a version already published to
Central. If Central itself fails or times out, inspect the deployment in
[Central Portal](https://central.sonatype.com/publishing/deployments) before recovery:
publication may have continued after the runner stopped. The saved bundle permits
manual recovery without rebuilding.

To verify release artifacts without signing or uploading:

```bash
mvn -Pcentral-release verify -pl '!vaadin-pixel-charts-demo' -Dgpg.skip=true
```

A local `-Pcentral-release deploy` stages for manual approval by default;
the workflow explicitly enables automatic publication.
