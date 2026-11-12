package tn.esprit.autoloc.autolocapi.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.autoloc.autolocapi.domain.CategorieVehicule;
import tn.esprit.autoloc.autolocapi.domain.StatutVehicule;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class VehiculeRequestDTO {

  @NotBlank(message = "L'immatriculation est obligatoire")
  @Size(max = 20, message = "L'immatriculation ne peut pas depasser 20 caracteres")
  private String immatriculation;

  @NotBlank(message = "La marque est obligatoire")
  @Size(max = 50, message = "La marque ne peut pas depasser 50 caracteres")
  private String marque;

  @NotBlank(message = "Le modele est obligatoire")
  @Size(max = 50, message = "Le modele ne peut pas depasser 50 caracteres")
  private String modele;

  @NotNull(message = "La categorie est obligatoire")
  private CategorieVehicule categorie;

  @NotNull(message = "Le tarif journalier est obligatoire")
  @DecimalMin(value = "0.0", inclusive = false, message = "Le tarif journalier doit etre superieur a 0")
  private BigDecimal tarifJournalier;

  @NotNull(message = "Le statut est obligatoire")
  private StatutVehicule statut;

  private Long agenceId;
}
