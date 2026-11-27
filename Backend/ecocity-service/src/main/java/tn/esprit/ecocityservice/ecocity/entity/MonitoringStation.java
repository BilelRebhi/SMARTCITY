package tn.esprit.ecocityservice.ecocity.entity;

import tn.esprit.ecocityservice.ecocity.enums.StationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A virtual station placed in a zone that records readings. */
@Entity
@Table(name = "monitoring_stations")
@Getter
@Setter
@NoArgsConstructor
public class MonitoringStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StationStatus status = StationStatus.ACTIVE;

    /** How often the station produces a reading (300 s = every 5 minutes). */
    @Column(nullable = false)
    private Integer samplingIntervalSec = 300;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;
}
