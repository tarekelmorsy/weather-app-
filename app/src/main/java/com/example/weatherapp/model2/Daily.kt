import com.google.gson.annotations.SerializedName



data class Daily (

	@SerializedName("dt") val dt : Double,
	@SerializedName("sunrise") val sunrise : Double,
	@SerializedName("sunset") val sunset : Double,
	@SerializedName("moonrise") val moonrise : Double,
	@SerializedName("moonset") val moonset : Double,
	@SerializedName("moon_phase") val moon_phase : Double,
	@SerializedName("temp") val temp : Temp,
	@SerializedName("feels_like") val feels_like : Feels_like,
	@SerializedName("pressure") val pressure : Double,
	@SerializedName("humidity") val humidity : Double,
	@SerializedName("dew_point") val dew_point : Double,
	@SerializedName("wind_speed") val wind_speed : Double,
	@SerializedName("wind_deg") val wind_deg : Double,
	@SerializedName("wind_gust") val wind_gust : Double,
	@SerializedName("weather") val weather : List<Weather>,
	@SerializedName("clouds") val clouds : Double,
	@SerializedName("pop") val pop : Double,
	@SerializedName("uvi") val uvi : Double
)