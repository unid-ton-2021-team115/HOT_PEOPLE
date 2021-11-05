package ins.hands.unid

import android.util.Log
import ins.hands.unid.data.PlaceData
import ins.hands.unid.data.UserData
import retrofit2.http.*
import java.lang.Exception

interface RemoteDateSourcePlace{
    suspend fun getPlace(id : String) : PlaceResponse
    suspend fun getHotPlace() : HotPlaceResponse
    suspend fun searchPlace(str : String) : HotPlaceResponse
    suspend fun getUserData(token : String) : UserResponse
}
class RemoteDataSourcePlaceImpl(private val service : GetPlaceService) : RemoteDateSourcePlace{
    override suspend fun getPlace(id : String) : PlaceResponse = service.getPlace(id)
    override suspend fun getHotPlace() : HotPlaceResponse {
        lateinit var response: HotPlaceResponse
        Log.d("RemoteDataSource","Get Hotplace")
         try{
             response =service.getHotPlace()
         }catch(e:Exception)
         {
             e.printStackTrace()
             response = HotPlaceResponse("Failer", mutableListOf())
         }
        return response
    }
    override suspend fun searchPlace(str: String): HotPlaceResponse =service.searchPlace(str)
    override suspend fun getUserData(token: String): UserResponse {

        return service.getUserData(token)
    }

}

interface GetPlaceService {
    @GET("/api/hot_place")
    suspend fun getHotPlace() : HotPlaceResponse
    @GET("/api/place/{id}")
    suspend fun getPlace(@Path("id") id : String) : PlaceResponse
    @GET("/api/place/search")
    suspend fun searchPlace(@Query("name") name : String) : HotPlaceResponse
    @GET("/auth/kakao/token")
    suspend fun getUserData(@Query("access_token") access_token : String) : UserResponse
}

data class UserResponse(val status : String, val data : UserData)
data class PlaceResponse(val status : String, val data : PlaceData)
data class HotPlaceResponse(val status : String, val data:MutableList<PlaceData>)

