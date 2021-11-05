package ins.hands.unid.data

data class PlaceData(
    val id: String,
    val name : String,
    val latitude: Double,
    val longitude: Double,
    val phone_number : String,
    val is_hot: Int,
    val recent_post_cnt: Int
)
data  class MatchingData(
    val id: Int,
    val hostId : Int,
    val placeId : String,
    val createDatetime : Long,
    val matchingDateTIme : Long,
    val description : String,
    val status : String,
    val guestId : Int
)
data class UserData(
    val id : Int,
    val nickname : String,
    val profileUrl : String,
    val gender : String,
    val age : Int,
    val ageRange : Int,
    val likes : Int
)
data class MatchingJoinData(
    val id : Int,
    val matchingId : Int,
    val guestId : Int
)
data class PlaceTypesData(
    val id : Int,
    val placeId : String,
    val type : String
)