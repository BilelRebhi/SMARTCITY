package tn.esprit.ecocityservice.ecocity.entity;

import tn.esprit.ecocityservice.ecocity.enums.AlertSeverity;
import tn.esprit.ecocityservice.ecocity.enums.Indicator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Maximum allowed value for an indicator. Only one threshold per indicator can be active. */
@Entity
@Table(name = "thresholds")
@Getter
@Setter
@NoArgsConstructor
public class Threshold {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Indicator indicator;

    @Column(nullable = false)
    private Double maxValue;

    @Column(nullable = false, length = 20)
    private String unit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AlertSeverity severity;

    @Column(nullable = false)
    private Boolean active = true;
}
