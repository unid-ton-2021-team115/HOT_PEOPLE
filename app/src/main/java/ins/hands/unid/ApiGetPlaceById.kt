package ins.hands.unid

import android.util.Log
import ins.hands.unid.data.PlaceData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.lang.Exception

interface RemoteDateSourcePlace{
    suspend fun getPlace(id : String) : PlaceResponse
    suspend fun getHotPlace() : HotPlaceResponse
    suspend fun searchPlace(str : String) : HotPlaceResponse
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

}

interface GetPlaceService {
    @GET("/api/hot_place")
    suspend fun getHotPlace() : HotPlaceResponse
    @GET("/api/place/{id}")
    suspend fun getPlace(@Path("id") id : String) : PlaceResponse
    @GET("/api/place/search?name={str}")
    suspend fun searchPlace(@Path("str") str : String) : HotPlaceResponse
}
data class PlaceResponse(val status : String, val data : PlaceData)
data class HotPlaceResponse(val status : String, val data:MutableList<PlaceData>)

