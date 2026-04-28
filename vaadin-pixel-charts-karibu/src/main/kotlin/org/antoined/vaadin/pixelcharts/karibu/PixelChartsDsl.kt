package org.antoined.vaadin.pixelcharts.karibu

import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.init
import com.vaadin.flow.component.HasComponents
import org.antoined.vaadin.pixelcharts.PixelBarChart
import org.antoined.vaadin.pixelcharts.PixelHeatmap
import org.antoined.vaadin.pixelcharts.PixelScatter
import org.antoined.vaadin.pixelcharts.PixelSparkline

/**
 * Creates a [PixelBarChart] — equalizer-style bar chart with stacked pixel blocks.
 *
 * ```kotlin
 * pixelBarChart {
 *     setHeight("200px")
 *     setWidthFull()
 *     setItems(listOf(BarItem("A", 0.8), BarItem("B", 0.5)))
 * }
 * ```
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).pixelBarChart(
    block: (@VaadinDsl PixelBarChart).() -> Unit = {}
): PixelBarChart = init(PixelBarChart(), block)

/**
 * Creates a [PixelSparkline] — line chart drawn as connected pixel blocks.
 *
 * ```kotlin
 * pixelSparkline {
 *     setHeight("150px")
 *     setWidthFull()
 *     setFillArea(true)
 *     setItems(listOf(0.2, 0.5, 0.8, 0.3))
 * }
 * ```
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).pixelSparkline(
    block: (@VaadinDsl PixelSparkline).() -> Unit = {}
): PixelSparkline = init(PixelSparkline(), block)

/**
 * Creates a [PixelHeatmap] — 2D grid of pixels with color intensity mapping.
 *
 * ```kotlin
 * pixelHeatmap {
 *     setHeight("200px")
 *     setWidthFull()
 *     setGridSize(7, 24)
 *     setItems(cells)
 * }
 * ```
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).pixelHeatmap(
    block: (@VaadinDsl PixelHeatmap).() -> Unit = {}
): PixelHeatmap = init(PixelHeatmap(), block)

/**
 * Creates a [PixelScatter] — dot plot with variable size and color per point.
 *
 * ```kotlin
 * pixelScatter {
 *     setHeight("200px")
 *     setWidthFull()
 *     setItems(points)
 * }
 * ```
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).pixelScatter(
    block: (@VaadinDsl PixelScatter).() -> Unit = {}
): PixelScatter = init(PixelScatter(), block)
