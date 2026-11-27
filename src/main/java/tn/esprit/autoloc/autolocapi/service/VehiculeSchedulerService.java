package tn.esprit.autoloc.autolocapi.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.autolocapi.domain.Reservation;
import tn.esprit.autoloc.autolocapi.domain.StatutReservation;
import tn.esprit.autoloc.autolocapi.domain.StatutVehicule;
import tn.esprit.autoloc.autolocapi.domain.Vehicule;
import tn.esprit.autoloc.autolocapi.repository.IReservationRepository;
import tn.esprit.autoloc.autolocapi.repository.IVehiculeRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehiculeSchedulerService {

  private final IReservationRepository reservationRepository;
  private final IVehiculeRepository vehiculeRepository;

  /**
   * Planifié chaque jour à 1h du matin.
   * Remet le statut d'un véhicule à DISPONIBLE à la fin d'une location
   * et clôture le statut de la location (StatutReservation.TERMINEE).
   * Expression cron : seconde(0) minute(0) heure(1) jourDuMois(*) mois(*) jourDeSemaine(*)
   */
  @Scheduled(cron = "0 0 1 * * *")
  @Transactional
  public void libererVehiculesEtCloturerLocations() {
    LocalDate today = LocalDate.now();

    log.info("[VehiculeSchedulerService] Exécution de la tâche planifiée de 1h du matin pour la date du {}", today);

    // Récupérer les réservations CONFIRMEE dont la dateFin est arrivée à terme (dateFin <= today)
    List<Reservation> reservationsTerminees = reservationRepository.findReservationsTerminees(StatutReservation.CONFIRMEE, today);

    if (reservationsTerminees.isEmpty()) {
      log.info("[VehiculeSchedulerService] Aucune réservation terminée à clôturer.");
      return;
    }

    log.info("[VehiculeSchedulerService] {} réservation(s) à clôturer trouvée(s).", reservationsTerminees.size());

    for (Reservation reservation : reservationsTerminees) {
      // 1. Clôturer le statut de la réservation
      reservation.setStatut(StatutReservation.TERMINEE);
      reservationRepository.save(reservation);

      // 2. Remettre le statut du véhicule à DISPONIBLE
      Vehicule vehicule = reservation.getVehicule();
      if (vehicule != null) {
        // Si le véhicule n'est pas en maintenance, le remettre DISPONIBLE
        if (vehicule.getStatut() != StatutVehicule.MAINTENANCE) {
          vehicule.setStatut(StatutVehicule.DISPONIBLE);
          vehiculeRepository.save(vehicule);
          log.info("[VehiculeSchedulerService] Véhicule {} ({}) remis à DISPONIBLE.",
              vehicule.getIdVehicule(), vehicule.getImmatriculation());
        }
      }

      log.info("[VehiculeSchedulerService] Réservation ID: {} clôturée avec statut TERMINEE.",
          reservation.getIdReservation());
    }
  }
}
