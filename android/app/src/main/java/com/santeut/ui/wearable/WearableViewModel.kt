package com.santeut.ui.wearable

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMap
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.PutDataMapRequest
import com.santeut.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class WearableViewModel (
    private val dataClient: DataClient,
    private val messageClient: MessageClient,
    private val capabilityClient: CapabilityClient
) :
    ViewModel(),
    DataClient.OnDataChangedListener,
    MessageClient.OnMessageReceivedListener,
    CapabilityClient.OnCapabilityChangedListener {

    var healthData = mutableStateOf(HealthData())

    @SuppressLint("VisibleForTests")
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.map { dataEvent ->
            if (dataEvent.type == DataEvent.TYPE_CHANGED) {
                val dataItem = dataEvent.dataItem
                val path = dataItem.uri.path
                if (path.equals("/health")) {
                    val dataMap = DataMapItem.fromDataItem(dataItem).dataMap
                    val newHealthData = healthData.value.copy(
                        heartRate = if (dataMap.containsKey("heartRate")) dataMap.getDouble("heartRate") else healthData.value.heartRate,
                        distance = if (dataMap.containsKey("distance")) dataMap.getDouble("distance") else healthData.value.distance,
                        stepsTotal = if (dataMap.containsKey("stepsTotal")) dataMap.getLong("stepsTotal") else healthData.value.stepsTotal,
                        calories = if (dataMap.containsKey("calories")) dataMap.getDouble("calories") else healthData.value.calories,
                        heartRateAverage = if (dataMap.containsKey("heartRateAverage")) dataMap.getDouble("heartRateAverage") else healthData.value.heartRateAverage,
                        absoluteElevation = if (dataMap.containsKey("absoluteElevation")) dataMap.getDouble("absoluteElevation") else healthData.value.absoluteElevation,
                        elevationGainTotal = if (dataMap.containsKey("elevationGainTotal")) dataMap.getDouble("elevationGainTotal") else healthData.value.elevationGainTotal
                    )

                    healthData.value = newHealthData

                    // 로그에 데이터 출력
                    Log.d("onDataChanged", "Received health data: Heart Rate = ${newHealthData.heartRate}, Distance = $newHealthData.distance, Steps = $newHealthData.stepsTotal, Calories = $newHealthData.calories, Heart Rate Average = $newHealthData.heartRateAverage, Elevation = $newHealthData.absoluteElevation, Elevation Gain = $newHealthData.elevationGainTotal")

                    // 어딘 가의 값 변경
                }
            }
        }
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        Log.d("onMessageReceived ", messageEvent.toString())
    }

    override fun onCapabilityChanged(capabilityInfo: CapabilityInfo) {
        Log.d("onCapabilityChanged ", capabilityInfo.toString())
    }

    suspend fun toSend(text: String){

    }
}

class WearableViewModelFactory(
    private val dataClient: DataClient,
    private val messageClient: MessageClient,
    private val capabilityClient: CapabilityClient
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WearableViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WearableViewModel(dataClient, messageClient, capabilityClient) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

data class HealthData(
    var heartRate: Double = 0.0,
    var distance: Double = 0.0,
    var stepsTotal: Long = 0,
    var calories: Double = 0.0,
    var heartRateAverage: Double = 0.0,
    var absoluteElevation: Double = 0.0,
    var elevationGainTotal: Double = 0.0
)