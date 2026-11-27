package com.campusverse.ecocity.entity;

import com.campusverse.ecocity.enums.AlertSeverity;
import com.campusverse.ecocity.enums.AlertStatus;
import com.campusverse.ecocity.enums.Indicator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/** Raised when a measurement goes over a threshold. Lifecycle: OPEN -> ACKNOWLEDGED -> RESOLVED. */
@Entity
@Table(name = "pollution_alerts")
@Getter
@Setter
@NoArgsConstructor
public class PollutionAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Indicator indicator;

    @Column(name = "measured_value", nullable = false)
    private Double value;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AlertSeverity severity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AlertStatus status = AlertStatus.OPEN;

    @Column(nullable = false)
    private LocalDateTime triggeredAt;

    /** A measurement triggers at most one alert. */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "measurement_id", nullable = false, unique = true)
    private Measurement measurement;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "threshold_id", nullable = false)
    private Threshold threshold;

    /** OPEN -> ACKNOWLEDGED. Returns false if the alert is not OPEN. */
    public boolean acknowledge() {
        if (status != AlertStatus.OPEN) {
            return false;
        }
        status = AlertStatus.ACKNOWLEDGED;
        return true;
    }

    /** OPEN or ACKNOWLEDGED -> RESOLVED. Returns false if it is already RESOLVED. */
    public boolean resolve() {
        if (status == AlertStatus.RESOLVED) {
            return false;
        }
        status = AlertStatus.RESOLVED;
        return true;
    }
}
