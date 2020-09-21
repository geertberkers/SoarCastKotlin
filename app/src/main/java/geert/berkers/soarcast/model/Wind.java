package geert.berkers.soarcast.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Zorgkluis (Geert Berkers)
 */
public class Wind {

    public Integer dag;
    public Integer locatieID;
    public Double snelheidGFS;
    public Double snelheidHarmonie;
    public Double snelheidMeting;
    public Long unixTimestamp;
    public Double vlaagGFS;
    public Double vlaagHarmonie;
    public Double vlaagMeting;

    Wind(final Long unixTimestamp, final Integer dag, final Integer locatieID, final Double snelheidMeting, final Double vlaagMeting, final Double snelheidHarmonie, final Double vlaagHarmonie, final Double snelheidGFS, final Double vlaagGFS) {
        this.unixTimestamp = unixTimestamp;
        this.dag = dag;
        this.locatieID = locatieID;
        this.snelheidMeting = snelheidMeting;
        this.vlaagMeting = vlaagMeting;
        this.snelheidHarmonie = snelheidHarmonie;
        this.vlaagHarmonie = vlaagHarmonie;
        this.snelheidGFS = snelheidGFS;
        this.vlaagGFS = vlaagGFS;
    }

    public Integer[] geefZonOpOnder() {
        final String format = new SimpleDateFormat("MM").format(new Date(this.unixTimestamp * 1000L));
        while (true) {
            try {
                Integer n = Integer.parseInt(format);
                while (true) {
                    Integer n2 = null;
                    Integer n3 = null;
                    switch (n) {
                        case 12: {
                            n2 = 8;
                            n3 = 16;
                            break;
                        }
                        case 11: {
                            n2 = 7;
                            n3 = 17;
                            break;
                        }
                        case 10: {
                            n2 = 7;
                            n3 = 19;
                            break;
                        }
                        case 9: {
                            n2 = 7;
                            n3 = 20;
                            break;
                        }
                        case 8: {
                            n2 = 6;
                            n3 = 21;
                            break;
                        }
                        case 7: {
                            n2 = 5;
                            n3 = 22;
                            break;
                        }
                        case 6: {
                            n2 = 5;
                            n3 = 22;
                            break;
                        }
                        case 5: {
                            n2 = 6;
                            n3 = 21;
                            break;
                        }
                        case 4: {
                            n2 = 7;
                            n3 = 20;
                            break;
                        }
                        case 3: {
                            n2 = 7;
                            n3 = 18;
                            break;
                        }
                        case 2: {
                            n2 = 8;
                            n3 = 17;
                            break;
                        }
                        case 1: {
                            n2 = 8;
                            n3 = 16;
                            break;
                        }
                        default: {
                            n2 = 8;
                            n3 = 16;
                            break;
                        }
                    }
                    return new Integer[] { n2, n3 };
//                        n = 0;
//                        continue;
                }
            }
            catch (NumberFormatException ex) {}
            continue;
        }
    }
}