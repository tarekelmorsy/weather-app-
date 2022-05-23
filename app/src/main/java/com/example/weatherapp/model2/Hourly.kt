import com.google.gson.annotations.SerializedName



data class Hourly (

	@SerializedName("dt") val dt : Double,
	@SerializedName("temp") val temp : Double,
	@SerializedName("feels_like") val feels_like : Double,
	@SerializedName("pressure") val pressure : Double,
	@SerializedName("humidity") val humidity : Double,
	@SerializedName("dew_point") val dew_point : Double,
	@SerializedName("uvi") val uvi : Double,
	@SerializedName("clouds") val clouds : Double,
	@SerializedName("visibility") val visibility : Double,
	@SerializedName("wind_speed") val wind_speed : Double,
	@SerializedName("wind_deg") val wind_deg : Double,
	@SerializedName("wind_gust") val wind_gust : Double,
	@SerializedName("weather") val weather : List<Weather>,
	@SerializedName("pop") val pop : Double
)