package tn.esprit.autoloc.autolocapi.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.autoloc.autolocapi.domain.Contrat;

public interface IContratRepository extends JpaRepository<Contrat, Long> {

  @Query("SELECT c FROM Contrat c WHERE c.valide = true AND c.reservation.dateFin BETWEEN :today AND :dateLimite")
  List<Contrat> findContratsArrivantAEcheanceDans48h(@Param("today") LocalDate today, @Param("dateLimite") LocalDate dateLimite);
}
