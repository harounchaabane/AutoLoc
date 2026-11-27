package tn.esprit.autoloc.autolocapi.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.autolocapi.domain.Equipement;
import tn.esprit.autoloc.autolocapi.domain.Maintenance;
import tn.esprit.autoloc.autolocapi.domain.StatutVehicule;
import tn.esprit.autoloc.autolocapi.domain.Vehicule;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autoloc.autolocapi.repository.IEquipementRepository;
import tn.esprit.autoloc.autolocapi.repository.IMaintenanceRepository;
import tn.esprit.autoloc.autolocapi.repository.IVehiculeRepository;
import tn.esprit.autoloc.autolocapi.web.dto.VehiculeDTO;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements IVehiculeService {

  private final IVehiculeRepository vehiculeRepository;
  private final IEquipementRepository equipementRepository;
  private final IMaintenanceRepository maintenanceRepository;

  @Override
  public Vehicule create(Vehicule vehicule) {
    return vehiculeRepository.save(vehicule);
  }

  @Override
  public Vehicule findById(Long id) {
    return vehiculeRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Vehicule", id));
  }

  @Override
  public List<Vehicule> findAll() {
    return vehiculeRepository.findAll();
  }

  @Override
  public Vehicule update(Long id, Vehicule vehicule) {
    Vehicule existant = findById(id);
    existant.setImmatriculation(vehicule.getImmatriculation());
    existant.setMarque(vehicule.getMarque());
    existant.setModele(vehicule.getModele());
    existant.setCategorie(vehicule.getCategorie());
    existant.setTarifJournalier(vehicule.getTarifJournalier());
    existant.setStatut(vehicule.getStatut());
    existant.setAgence(vehicule.getAgence());
    existant.setEquipements(vehicule.getEquipements());
    return vehiculeRepository.save(existant);
  }

  @Override
  public void deleteById(Long id) {
    if (!vehiculeRepository.existsById(id)) {
      throw new ResourceNotFoundException("Vehicule", id);
    }
    vehiculeRepository.deleteById(id);
  }

  @Override
  @Transactional
  public VehiculeDTO affecterVehiculeAEquipement(Long idVehicule, Long idEquipement) {
    // 1. Récupérer le véhicule
    Vehicule vehicule = vehiculeRepository.findById(idVehicule)
        .orElseThrow(() -> new ResourceNotFoundException("Vehicule", idVehicule));

    // 2. Récupérer l'équipement
    Equipement equipement = equipementRepository.findById(idEquipement)
        .orElseThrow(() -> new ResourceNotFoundException("Equipement", idEquipement));

    // 3. Ajouter l'équipement au véhicule (éviter les doublons)
    if (vehicule.getEquipements() == null) {
      vehicule.setEquipements(new ArrayList<>());
    }
    boolean dejaPresent = vehicule.getEquipements().stream()
        .anyMatch(e -> e.getIdEquipement().equals(idEquipement));
    if (!dejaPresent) {
      vehicule.getEquipements().add(equipement);
    }

    Vehicule saved = vehiculeRepository.save(vehicule);
    return toVehiculeDTO(saved);
  }

  @Override
  @Transactional
  public VehiculeDTO affecterMaintenanceAvehicule(String description, String immatriculation) {
    // 1. Récupérer le véhicule par immatriculation
    Vehicule vehicule = vehiculeRepository.findByImmatriculation(immatriculation)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Vehicule non trouvé avec immatriculation=" + immatriculation));

    // 2. Créer une nouvelle maintenance
    Maintenance maintenance = new Maintenance();
    maintenance.setDescription(description);
    maintenance.setDateDebut(LocalDate.now());
    maintenance.setVehicule(vehicule);
    maintenanceRepository.save(maintenance);

    // 3. Mettre à jour le statut du véhicule
    vehicule.setStatut(StatutVehicule.MAINTENANCE);
    Vehicule saved = vehiculeRepository.save(vehicule);

    // 4. Recharger les maintenances pour le DTO
    if (saved.getMaintenances() == null) {
      saved.setMaintenances(new ArrayList<>());
    }
    saved.getMaintenances().add(maintenance);

    return toVehiculeDTO(saved);
  }

  private VehiculeDTO toVehiculeDTO(Vehicule vehicule) {
    VehiculeDTO dto = new VehiculeDTO();
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
    if (vehicule.getEquipements() != null) {
      dto.setEquipements(vehicule.getEquipements().stream()
          .map(Equipement::getLibelle)
          .collect(Collectors.toList()));
    }
    if (vehicule.getMaintenances() != null) {
      dto.setMaintenances(vehicule.getMaintenances().stream()
          .map(m -> {
            VehiculeDTO.MaintenanceDTO mDTO = new VehiculeDTO.MaintenanceDTO();
            mDTO.setIdMaintenance(m.getIdMaintenance());
            mDTO.setDescription(m.getDescription());
            mDTO.setDateDebut(m.getDateDebut());
            mDTO.setDateFin(m.getDateFin());
            return mDTO;
          })
          .collect(Collectors.toList()));
    }
    return dto;
  }
}
