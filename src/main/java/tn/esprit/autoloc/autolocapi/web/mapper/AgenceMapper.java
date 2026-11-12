package tn.esprit.autoloc.autolocapi.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.autolocapi.domain.Agence;
import tn.esprit.autoloc.autolocapi.web.dto.AgenceRequestDTO;
import tn.esprit.autoloc.autolocapi.web.dto.AgenceResponseDTO;

@Component
public class AgenceMapper {

  public Agence toEntity(AgenceRequestDTO dto) {
    Agence agence = new Agence();
    agence.setNom(dto.getNom());
    agence.setVille(dto.getVille());
    agence.setAdresse(dto.getAdresse());
    agence.setTelephone(dto.getTelephone());
    return agence;
  }

  public AgenceResponseDTO toResponseDTO(Agence agence) {
    AgenceResponseDTO dto = new AgenceResponseDTO();
    dto.setIdAgence(agence.getIdAgence());
    dto.setNom(agence.getNom());
    dto.setVille(agence.getVille());
    dto.setAdresse(agence.getAdresse());
    dto.setTelephone(agence.getTelephone());
    dto.setNombreEmployes(agence.getEmployes() != null ? agence.getEmployes().size() : 0);
    dto.setNombreVehicules(agence.getVehicules() != null ? agence.getVehicules().size() : 0);
    return dto;
  }

  public void updateEntityFromDTO(AgenceRequestDTO dto, Agence agence) {
    agence.setNom(dto.getNom());
    agence.setVille(dto.getVille());
    agence.setAdresse(dto.getAdresse());
    agence.setTelephone(dto.getTelephone());
  }
}
