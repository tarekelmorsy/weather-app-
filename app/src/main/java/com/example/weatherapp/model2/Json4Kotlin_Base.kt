import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


const val ID=0
@Entity(tableName = "weather")
data class AllWeather (

	@PrimaryKey
	@SerializedName("lat") val lat : Double,
	@SerializedName("lon") val lon : Double,
	@SerializedName("timezone") val timezone : String,
	@SerializedName("timezone_offset") val timezone_offset : Double,
	@SerializedName("current") val current : Current,

	@Embedded(prefix = "minutely_")
	@SerializedName("minutely") val minutely : List<Minutely>,
	@Embedded(prefix = "hourly_")
	@SerializedName("hourly") val hourly : List<Hourly>,
	@Embedded(prefix = "daily_")
	@SerializedName("daily") val daily : List<Daily>,
	@SerializedName("alerts") val alerts : List<Alart>
){

	@PrimaryKey(autoGenerate = false)
	var id:Int =ID
}