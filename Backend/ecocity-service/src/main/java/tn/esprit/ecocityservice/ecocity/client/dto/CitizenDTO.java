package tn.esprit.ecocityservice.ecocity.client.dto;

/** The only fields MS4 needs from MS1's citizen profile. Other fields are ignored. */
public record CitizenDTO(
        Long id,
        String neighborhood
) {
}
