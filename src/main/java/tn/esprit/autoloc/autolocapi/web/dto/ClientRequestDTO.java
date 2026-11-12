package tn.esprit.autoloc.autolocapi.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ClientRequestDTO {

  @NotBlank(message = "Le nom est obligatoire")
  @Size(max = 50, message = "Le nom ne peut pas depasser 50 caracteres")
  private String nom;

  @NotBlank(message = "Le prenom est obligatoire")
  @Size(max = 50, message = "Le prenom ne peut pas depasser 50 caracteres")
  private String prenom;

  @NotBlank(message = "L'email est obligatoire")
  @Email(message = "L'email doit etre valide")
  @Size(max = 100, message = "L'email ne peut pas depasser 100 caracteres")
  private String email;

  @NotBlank(message = "Le telephone est obligatoire")
  @Size(max = 20, message = "Le telephone ne peut pas depasser 20 caracteres")
  private String telephone;

  @NotBlank(message = "Le numero de permis est obligatoire")
  @Size(max = 50, message = "Le numero de permis ne peut pas depasser 50 caracteres")
  private String numPermis;
}
