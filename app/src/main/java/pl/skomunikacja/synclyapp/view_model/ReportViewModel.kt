package pl.skomunikacja.synclyapp.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.skomunikacja.synclyapp.config.RetrofitClient
import pl.skomunikacja.synclyapp.helpers.ApiReportsHelper
import pl.skomunikacja.synclyapp.model.CreateReportRequest

class ReportViewModel : ViewModel() {

    private val apiReportsHelper = ApiReportsHelper(RetrofitClient.apiReportsService)

    fun createReport(
        createReportRequest: CreateReportRequest,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = apiReportsHelper.createReport(createReportRequest)

                if (result.isSuccessful) {
                    onSuccess()
                } else {
                    onError(Exception("Error: ${result.errorBody()}"))
                }
            } catch (exception: Exception) {
                onError(exception)
            }
        }
    }
}