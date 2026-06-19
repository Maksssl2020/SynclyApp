package pl.skomunikacja.synclyapp.helpers

import pl.skomunikacja.synclyapp.model.CreateReportRequest
import pl.skomunikacja.synclyapp.service.ApiReportService
import retrofit2.Response

class ApiReportsHelper(
    private val apiReportService: ApiReportService
) {

    suspend fun createReport(request: CreateReportRequest): Response<Unit> {
        val response = apiReportService.createReport(request)
        return response
    }
}