package pl.skomunikacja.synclyapp.model

data class CreateReportRequest(
    val userId: Long,
    val entityId: Long,
    val reportReasonType: ReportReason,
    val title: String,
    val reason: String,
    val reportType: ReportType
)
