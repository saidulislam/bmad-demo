package com.firm.apihealthdashboard.adapter

import org.springframework.stereotype.Component

@Component
class AdapterFactory(private val adapters: List<TelemetryAdapter>) {

    fun getAdapter(sourceType: String): TelemetryAdapter? {
        return adapters.find { it.sourceType() == sourceType }
    }
}
