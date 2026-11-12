package tn.esprit.autoloc.autolocapi.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AgenceRequestDTO {

  @NotBlank(message = "Le nom de l'agence est obligatoire")
  @Size(max = 100, message = "Le nom ne peut pas depasser 100 caracteres")
  private String nom;

  @NotBlank(message = "La ville est obligatoire")
  @Size(max = 50, message = "La ville ne peut pas depasser 50 caracteres")
  private String ville;

  @NotBlank(message = "L'adresse est obligatoire")
  @Size(max = 150, message = "L'adresse ne peut pas depasser 150 caracteres")
  private String adresse;

  @NotBlank(message = "Le telephone est obligatoire")
  @Size(max = 20, message = "Le telephone ne peut pas depasser 20 caracteres")
  private String telephone;
}
