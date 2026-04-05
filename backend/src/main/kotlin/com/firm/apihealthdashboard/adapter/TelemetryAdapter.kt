package com.firm.apihealthdashboard.adapter

import java.util.UUID

data class MetricDataPoint(
    val apiId: UUID,
    val metricType: String,
    val value: Double,
    val unit: String,
    val source: String
)

interface TelemetryAdapter {
    fun connect(config: Map<String, Any>): Boolean
    fun validateConnection(): Boolean
    fun fetchMetrics(apiId: UUID): List<MetricDataPoint>
    fun sourceType(): String
}
