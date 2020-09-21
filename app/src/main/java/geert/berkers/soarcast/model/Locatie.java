package geert.berkers.soarcast.model;

/**
 * Created by Zorgkluis (Geert Berkers)
 */
public class Locatie {

    public Integer id;
    public Double lat;
    public Double lon;
    public Integer maxdeg;
    public Integer mindeg;
    public String naam;

    public Locatie(final String naam, final Integer id, final Double lat, final Double lon, final Integer mindeg, final Integer maxdeg) {
        this.naam = naam;
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.mindeg = mindeg;
        this.maxdeg = maxdeg;
    }
}