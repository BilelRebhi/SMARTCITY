package tn.esprit.ecocityservice.ecocity.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Polygon;

/** A neighborhood of the city. */
@Entity
@Table(name = "zones")
@Getter
@Setter
@NoArgsConstructor
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 100)
    private String district;

    /** Area of the zone, stored as a PostGIS polygon (WGS 84, SRID 4326). */
    @Column(columnDefinition = "geometry(Polygon,4326)")
    private Polygon boundary;
}
