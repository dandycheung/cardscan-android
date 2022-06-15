package com.getbouncer.scan.framework.api.dto

import com.getbouncer.scan.framework.RepeatingTaskStats
import com.getbouncer.scan.framework.Stats
import com.getbouncer.scan.framework.TaskStats
import com.getbouncer.scan.framework.ml.ModelLoadDetails
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Deprecated(
    message = "Replaced by stripe card scan. See https://github.com/stripe/stripe-android/tree/master/stripecardscan",
    replaceWith = ReplaceWith("StripeCardScan"),
)
data class StatsPayload(
    @SerialName("instance_id") val instanceId: String,
    @SerialName("scan_id") val scanId: String?,
    @SerialName("payload_version") val payloadVersion: Int = 2,
    @SerialName("device") val device: ClientDevice,
    @SerialName("app") val app: AppInfo,
    @SerialName("scan_stats") val scanStats: ScanStatistics,
    @SerialName("model_versions") val modelVersions: List<ModelVersion>,
)

@Serializable
@Deprecated(
    message = "Replaced by stripe card scan. See https://github.com/stripe/stripe-android/tree/master/stripecardscan",
    replaceWith = ReplaceWith("StripeCardScan"),
)
data class ScanStatistics(
    @SerialName("tasks") val tasks: Map<String, List<TaskStatistics>>,
    @SerialName("repeating_tasks") val repeatingTasks: Map<String, List<RepeatingTaskStatistics>>
) {
    companion object {
        @JvmStatic
        fun fromStats() = ScanStatistics(
            tasks = Stats.getTasks().mapValues { entry ->
                entry.value.map { TaskStatistics.fromTaskStats(it) }
            },
            repeatingTasks = Stats.getRepeatingTasks().mapValues { repeatingTasks ->
                repeatingTasks.value.map { resultMap ->
                    RepeatingTaskStatistics.fromRepeatingTaskStats(
                        result = resultMap.key,
                        repeatingTaskStats = resultMap.value,
                    )
                }
            }
        )
    }
}

@Serializable
@Deprecated(
    message = "Replaced by stripe card scan. See https://github.com/stripe/stripe-android/tree/master/stripecardscan",
    replaceWith = ReplaceWith("StripeCardScan"),
)
data class ModelVersion(
    @SerialName("name") val name: String,
    @SerialName("version") val version: String,
    @SerialName("framework_version") val frameworkVersion: Int,
    @SerialName("loaded_successfully") val loadedSuccessfully: Boolean
) {
    companion object {
        fun fromModelLoadDetails(details: ModelLoadDetails) = ModelVersion(
            name = details.modelClass,
            version = details.modelVersion,
            frameworkVersion = details.modelFrameworkVersion,
            loadedSuccessfully = details.success
        )
    }
}

@Serializable
@Deprecated(
    message = "Replaced by stripe card scan. See https://github.com/stripe/stripe-android/tree/master/stripecardscan",
    replaceWith = ReplaceWith("StripeCardScan"),
)
data class TaskStatistics(
    @SerialName("started_at_ms") val startedAtMs: Long,
    @SerialName("duration_ms") val durationMs: Long,
    @SerialName("result") val result: String?
) {
    companion object {
        @JvmStatic
        fun fromTaskStats(taskStats: TaskStats) = TaskStatistics(
            startedAtMs = taskStats.started.toMillisecondsSinceEpoch(),
            durationMs = taskStats.duration.inMilliseconds.toLong(),
            result = taskStats.result
        )
    }
}

@Serializable
@Deprecated(
    message = "Replaced by stripe card scan. See https://github.com/stripe/stripe-android/tree/master/stripecardscan",
    replaceWith = ReplaceWith("StripeCardScan"),
)
data class RepeatingTaskStatistics(
    @SerialName("result") val result: String,
    @SerialName("executions") val executions: Int,
    @SerialName("start_time_ms") val startTimeMs: Long,
    @SerialName("total_duration_ms") val totalDurationMs: Long,
    @SerialName("total_cpu_duration_ms") val totalCpuDurationMs: Long,
    @SerialName("average_duration_ms") val averageDurationMs: Long,
    @SerialName("minimum_duration_ms") val minimumDurationMs: Long,
    @SerialName("maximum_duration_ms") val maximumDurationMs: Long,
) {
    companion object {
        @JvmStatic
        fun fromRepeatingTaskStats(result: String, repeatingTaskStats: RepeatingTaskStats) = RepeatingTaskStatistics(
            result = result,
            executions = repeatingTaskStats.executions,
            startTimeMs = repeatingTaskStats.startedAt.toMillisecondsSinceEpoch(),
            totalDurationMs = repeatingTaskStats.totalDuration.inMilliseconds.toLong(),
            totalCpuDurationMs = repeatingTaskStats.totalCpuDuration.inMilliseconds.toLong(),
            averageDurationMs = repeatingTaskStats.averageDuration().inMilliseconds.toLong(),
            minimumDurationMs = repeatingTaskStats.minimumDuration.inMilliseconds.toLong(),
            maximumDurationMs = repeatingTaskStats.maximumDuration.inMilliseconds.toLong(),
        )
    }
}
