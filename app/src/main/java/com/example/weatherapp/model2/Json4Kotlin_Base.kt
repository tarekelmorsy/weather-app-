import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/*
Copyright (c) 2022 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */

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