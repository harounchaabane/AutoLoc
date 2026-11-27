package tn.esprit.autoloc.autolocapi.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.autolocapi.domain.Contrat;
import tn.esprit.autoloc.autolocapi.repository.IContratRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContratSchedulerService {

  private final IContratRepository contratRepository;

  /**
   * Planifié tous les jours de la semaine (du lundi au vendredi) à 7h00 du matin.
   * Expression cron : seconde(0) minute(0) heure(7) jourDuMois(*) mois(*) jourDeSemaine(MON-FRI)
   */
  @Scheduled(cron = "0 0 7 * * MON-FRI")
  public void identifierContratsArrivantAEcheanceSous48h() {
    LocalDate today = LocalDate.now();
    LocalDate dans48h = today.plusDays(2);

    log.info("[ContratSchedulerService] Recherche des contrats arrivant à échéance entre le {} et le {}", today, dans48h);

    List<Contrat> contratsEcheance = contratRepository.findContratsArrivantAEcheanceDans48h(today, dans48h);

    if (contratsEcheance.isEmpty()) {
      log.info("[ContratSchedulerService] Aucun contrat n'arrive à échéance sous 48 heures.");
    } else {
      log.warn("[ContratSchedulerService] {} contrat(s) arrivent à échéance sous 48 heures :", contratsEcheance.size());
      for (Contrat c : contratsEcheance) {
        log.warn(" - Contrat ID: {}, Date Signature: {}, Date Fin Réservation: {}, Client: {} {}, Montant: {} DT",
            c.getIdContrat(),
            c.getDateSignature(),
            c.getReservation() != null ? c.getReservation().getDateFin() : "N/A",
            (c.getReservation() != null && c.getReservation().getClient() != null) ? c.getReservation().getClient().getNom() : "N/A",
            (c.getReservation() != null && c.getReservation().getClient() != null) ? c.getReservation().getClient().getPrenom() : "",
            c.getMontantTotal());
      }
    }
  }
}
