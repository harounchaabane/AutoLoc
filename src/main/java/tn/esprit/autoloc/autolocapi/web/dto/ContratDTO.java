package tn.esprit.autoloc.autolocapi.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ContratDTO {
  private Long idContrat;
  private LocalDate dateSignature;
  private BigDecimal montantTotal;
  private boolean valide;
  private Long reservationId;
  private LocalDate reservationDateDebut;
  private LocalDate reservationDateFin;
  private String clientNom;
  private String clientPrenom;
}
