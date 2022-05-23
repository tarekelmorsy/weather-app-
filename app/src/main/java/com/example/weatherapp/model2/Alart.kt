import com.google.gson.annotations.SerializedName


data class Alart (

	@SerializedName("sender_name") val sender_name : String,
	@SerializedName("event") val event : String,
	@SerializedName("start") val start : Double,
	@SerializedName("end") val end : Double,
	@SerializedName("description") val description : String,
	@SerializedName("tags") val tags : List<String>
)