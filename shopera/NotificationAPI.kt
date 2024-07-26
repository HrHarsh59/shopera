package project.demo.shopera

import okhttp3.ResponseBody
import project.demo.shopera.Constants.Companion.CONTENT_TYPE
import project.demo.shopera.Constants.Companion.SERVER_KEY
import project.demo.shopera.model.PushNotification
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationAPI {

    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}