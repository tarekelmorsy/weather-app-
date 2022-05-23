import com.google.gson.annotations.SerializedName


data class Weather (

	@SerializedName("id") val id : Double,
	@SerializedName("main") val main : String,
	@SerializedName("description") val description : String,
	@SerializedName("icon") val icon : String
)