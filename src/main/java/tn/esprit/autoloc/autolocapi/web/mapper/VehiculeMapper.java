package tn.esprit.autoloc.autolocapi.web.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.autolocapi.domain.Agence;
import tn.esprit.autoloc.autolocapi.domain.Vehicule;
import tn.esprit.autoloc.autolocapi.repository.IAgenceRepository;
import tn.esprit.autoloc.autolocapi.web.dto.VehiculeRequestDTO;
import tn.esprit.autoloc.autolocapi.web.dto.VehiculeResponseDTO;

@Component
@RequiredArgsConstructor
public class VehiculeMapper {

  private final IAgenceRepository agenceRepository;

  public Vehicule toEntity(VehiculeRequestDTO dto) {
    Vehicule vehicule = new Vehicule();
    vehicule.setImmatriculation(dto.getImmatriculation());
    vehicule.setMarque(dto.getMarque());
    vehicule.setModele(dto.getModele());
    vehicule.setCategorie(dto.getCategorie());
    vehicule.setTarifJournalier(dto.getTarifJournalier());
    vehicule.setStatut(dto.getStatut());
    if (dto.getAgenceId() != null) {
      Agence agence = agenceRepository.findById(dto.getAgenceId()).orElse(null);
      vehicule.setAgence(agence);
    }
    return vehicule;
  }

  public VehiculeResponseDTO toResponseDTO(Vehicule vehicule) {
    VehiculeResponseDTO dto = new VehiculeResponseDTO();
    dto.setIdVehicule(vehicule.getIdVehicule());
    dto.setImmatriculation(vehicule.getImmatriculation());
    dto.setMarque(vehicule.getMarque());
    dto.setModele(vehicule.getModele());
    dto.setCategorie(vehicule.getCategorie());
    dto.setTarifJournalier(vehicule.getTarifJournalier());
    dto.setStatut(vehicule.getStatut());
    if (vehicule.getAgence() != null) {
      dto.setAgenceNom(vehicule.getAgence().getNom());
      dto.setAgenceId(vehicule.getAgence().getIdAgence());
    }
    return dto;
  }

  public void updateEntityFromDTO(VehiculeRequestDTO dto, Vehicule vehicule) {
    vehicule.setImmatriculation(dto.getImmatriculation());
    vehicule.setMarque(dto.getMarque());
    vehicule.setModele(dto.getModele());
    vehicule.setCategorie(dto.getCategorie());
    vehicule.setTarifJournalier(dto.getTarifJournalier());
    vehicule.setStatut(dto.getStatut());
    if (dto.getAgenceId() != null) {
      Agence agence = agenceRepository.findById(dto.getAgenceId()).orElse(null);
      vehicule.setAgence(agence);
    } else {
      vehicule.setAgence(null);
    }
  }
}
