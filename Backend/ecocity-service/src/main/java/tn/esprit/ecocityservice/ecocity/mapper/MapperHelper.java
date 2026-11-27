package tn.esprit.ecocityservice.ecocity.mapper;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import tn.esprit.ecocityservice.ecocity.enums.Indicator;
import tn.esprit.ecocityservice.ecocity.exception.InvalidRequestException;

/** Conversions used by the MapStruct mappers that need custom logic (WKT geometry, units). */
public final class MapperHelper {

    private static final int WGS84 = 4326;

    private MapperHelper() {
    }

    /** Reads a WKT polygon such as "POLYGON((lng lat, lng lat, ...))". Returns null when the text is empty. */
    public static Polygon toPolygon(String wkt) {
        if (wkt == null || wkt.isBlank()) {
            return null;
        }
        Geometry geometry;
        try {
            geometry = new WKTReader().read(wkt);
        } catch (ParseException e) {
            throw new InvalidRequestException("boundary is not valid WKT: " + e.getMessage());
        }
        if (!(geometry instanceof Polygon polygon) || !polygon.isValid()) {
            throw new InvalidRequestException("boundary must be a valid, closed POLYGON");
        }
        polygon.setSRID(WGS84);
        return polygon;
    }

    public static String toWkt(Polygon polygon) {
        return polygon == null ? null : polygon.toText();
    }

    /** Uses the indicator's unit when none is given, and rejects a unit that does not match the indicator. */
    public static String resolveUnit(Indicator indicator, String unit) {
        if (unit == null || unit.isBlank()) {
            return indicator.getUnit();
        }
        if (!unit.trim().equalsIgnoreCase(indicator.getUnit())) {
            throw new InvalidRequestException(
                    "unit '" + unit + "' is not valid for " + indicator + " (expected '" + indicator.getUnit() + "')");
        }
        return indicator.getUnit();
    }
}
