package tn.esprit.autoloc.autolocapi.service;

import java.util.List;
import tn.esprit.autoloc.autolocapi.domain.Vehicule;
import tn.esprit.autoloc.autolocapi.web.dto.VehiculeDTO;

public interface IVehiculeService {
  Vehicule create(Vehicule vehicule);
  Vehicule findById(Long id);
  List<Vehicule> findAll();
  Vehicule update(Long id, Vehicule vehicule);
  void deleteById(Long id);

  // Services d'affectation
  VehiculeDTO affecterVehiculeAEquipement(Long idVehicule, Long idEquipement);
  VehiculeDTO affecterMaintenanceAvehicule(String description, String immatriculation);
}
