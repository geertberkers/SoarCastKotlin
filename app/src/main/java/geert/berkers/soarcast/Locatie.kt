package geert.berkers.soarcast

/**
 * Created by Zorgkluis (Geert Berkers)
 */
data class Locatie(
    var naam: String,
    var id: Int,
    var lat: Double,
    var lon: Double,
    var mindeg: Int,
    var maxdeg: Int
)