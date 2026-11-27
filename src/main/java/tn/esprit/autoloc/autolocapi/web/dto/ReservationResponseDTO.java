package tn.esprit.autoloc.autolocapi.web.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.autoloc.autolocapi.domain.StatutReservation;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReservationResponseDTO {
  private Long idReservation;
  private LocalDate dateDebut;
  private LocalDate dateFin;
  private StatutReservation statut;
  private String clientNom;
  private String clientPrenom;
  private Long clientId;
  private String vehiculeImmatriculation;
  private String vehiculeMarque;
  private String vehiculeModele;
  private Long vehiculeId;
  private Long contratId;
}
