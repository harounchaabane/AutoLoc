package tn.esprit.autoloc.autolocapi.web.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.autoloc.autolocapi.domain.CategorieVehicule;
import tn.esprit.autoloc.autolocapi.domain.StatutVehicule;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class VehiculeDTO {
  private Long idVehicule;
  private String immatriculation;
  private String marque;
  private String modele;
  private CategorieVehicule categorie;
  private BigDecimal tarifJournalier;
  private StatutVehicule statut;
  private String agenceNom;
  private Long agenceId;
  private List<String> equipements;
  private List<MaintenanceDTO> maintenances;

  @Getter @Setter @NoArgsConstructor @AllArgsConstructor
  public static class MaintenanceDTO {
    private Long idMaintenance;
    private String description;
    private java.time.LocalDate dateDebut;
    private java.time.LocalDate dateFin;
  }
}
