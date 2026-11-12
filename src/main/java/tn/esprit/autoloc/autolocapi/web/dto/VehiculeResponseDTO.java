package tn.esprit.autoloc.autolocapi.web.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.autoloc.autolocapi.domain.CategorieVehicule;
import tn.esprit.autoloc.autolocapi.domain.StatutVehicule;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class VehiculeResponseDTO {
  private Long idVehicule;
  private String immatriculation;
  private String marque;
  private String modele;
  private CategorieVehicule categorie;
  private BigDecimal tarifJournalier;
  private StatutVehicule statut;
  private String agenceNom;
  private Long agenceId;
}
