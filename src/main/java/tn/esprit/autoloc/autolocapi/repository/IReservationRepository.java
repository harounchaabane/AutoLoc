package tn.esprit.autoloc.autolocapi.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.autoloc.autolocapi.domain.Reservation;
import tn.esprit.autoloc.autolocapi.domain.StatutReservation;

public interface IReservationRepository extends JpaRepository<Reservation, Long> {

  @Query("SELECT r FROM Reservation r WHERE r.statut = :statut AND r.dateFin <= :dateReference")
  List<Reservation> findReservationsTerminees(@Param("statut") StatutReservation statut, @Param("dateReference") LocalDate dateReference);
}
