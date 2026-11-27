package tn.esprit.autoloc.autolocapi.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.autoloc.autolocapi.domain.StatutReservation;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReservationWithContratRequestDTO {

  // Reservation fields
  @NotNull(message = "La date de début est obligatoire")
  private LocalDate dateDebut;

  @NotNull(message = "La date de fin est obligatoire")
  private LocalDate dateFin;

  private StatutReservation statut;

  @NotNull(message = "L'identifiant du véhicule est obligatoire")
  private Long vehiculeId;

  @NotNull(message = "L'identifiant du client est obligatoire")
  private Long clientId;

  // Contrat fields
  @NotNull(message = "La date de signature est obligatoire")
  private LocalDate dateSignature;
}
