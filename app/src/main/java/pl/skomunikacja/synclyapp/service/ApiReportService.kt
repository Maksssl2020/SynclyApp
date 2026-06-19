package pl.skomunikacja.synclyapp.service

import pl.skomunikacja.synclyapp.model.CreateReportRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiReportService {

    @POST("api/v1/reports/android-app/report")
    suspend fun createReport(
        @Body request: CreateReportRequest
    ): Response<Unit>
}