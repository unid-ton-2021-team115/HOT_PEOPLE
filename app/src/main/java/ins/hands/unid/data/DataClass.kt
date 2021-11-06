package ins.hands.unid.data

data class PlaceData(
    val id: String,
    val name : String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val phone_number : String,
    val is_hot: Int,
    val recent_post_cnt: Int
)

data  class MatchingData(
    val id: Int,
    val host_id : Int,
    val place_id : String,
    val create_date_time : String,
    val matching_date_time : String,
    val description : String,
    val status : String,
    val guest_id : Int
)
data class MatchingStatusData(
    val id : Int,
    val host_id : Int,
    val place_id : String,
    val create_datetime : String,
    val matchiing_datetime : String,
    val description: String,
    val status: String,
    val joinRequests : JoinRequest
)
data class JoinRequest(
    val id : Int,
    val matching_id : Int,
    val guest_id : Int,
    val status : String,
    val guest : UserData
)
data class UserData(
    val id : Int,
    val nickname : String,
    val profile_url : String,
    val gender : String,
    val age : Int,
    val ageRange : String,
    val likes : Int,
    val accessToken : String = ""
)

data class PlaceTypesData(
    val id : Int,
    val placeId : String,
    val type : String
)