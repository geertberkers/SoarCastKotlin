package geert.berkers.soarcast

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Zorgkluis (Geert Berkers)
 */
data class Richting(
    var unixTimestamp: Long,
    var dag: Int,
    var locatieID: Int,
    var richtingMeting: Double,
    var richtingHarmonie: Double,
    var richtingGFS: Double
) {
    fun geefZonOpOnder(): Array<Int> {
        val format = SimpleDateFormat("MM").format(Date(unixTimestamp * 1000L))
        while (true) {
            try {
                val n = format.toInt()
                while (true) {
                    var n2: Int? = null
                    var n3: Int? = null
                    when (n) {
                        12 -> {
                            n2 = 8
                            n3 = 16
                        }
                        11 -> {
                            n2 = 7
                            n3 = 17
                        }
                        10 -> {
                            n2 = 7
                            n3 = 19
                        }
                        9 -> {
                            n2 = 7
                            n3 = 20
                        }
                        8 -> {
                            n2 = 6
                            n3 = 21
                        }
                        7 -> {
                            n2 = 5
                            n3 = 22
                        }
                        6 -> {
                            n2 = 5
                            n3 = 22
                        }
                        5 -> {
                            n2 = 6
                            n3 = 21
                        }
                        4 -> {
                            n2 = 7
                            n3 = 20
                        }
                        3 -> {
                            n2 = 7
                            n3 = 18
                        }
                        2 -> {
                            n2 = 8
                            n3 = 17
                        }
                        1 -> {
                            n2 = 8
                            n3 = 16
                        }
                        else -> {
                            n2 = 8
                            n3 = 16
                        }
                    }
                    return arrayOf(n2, n3)
                    //                        n = 0;
//                        continue;
                }
            } catch (ex: NumberFormatException) {
            }
            continue
        }
    }
}