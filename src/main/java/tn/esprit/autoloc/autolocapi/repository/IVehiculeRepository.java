package tn.esprit.autoloc.autolocapi.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.autolocapi.domain.Vehicule;

public interface IVehiculeRepository extends JpaRepository<Vehicule, Long> {
  Optional<Vehicule> findByImmatriculation(String immatriculation);
}
