package com.campusverse.ecocity.entity;

import com.campusverse.ecocity.enums.Indicator;
import com.campusverse.ecocity.enums.MeasurementQuality;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/** One reading of a pollutant or of the noise level. Exposed by the API as a "reading". */
@Entity
@Table(name = "measurements")
@Getter
@Setter
@NoArgsConstructor
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Indicator indicator;

    @Column(name = "measured_value", nullable = false)
    private Double value;

    @Column(nullable = false, length = 20)
    private String unit;

    @Column(nullable = false)
    private LocalDateTime recordedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MeasurementQuality quality = MeasurementQuality.VALID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "station_id", nullable = false)
    private MonitoringStation station;
}
