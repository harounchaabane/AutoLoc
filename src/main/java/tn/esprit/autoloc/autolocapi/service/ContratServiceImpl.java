package tn.esprit.autoloc.autolocapi.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.autolocapi.domain.Contrat;
import tn.esprit.autoloc.autolocapi.domain.Reservation;
import tn.esprit.autoloc.autolocapi.domain.StatutReservation;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autoloc.autolocapi.repository.IContratRepository;
import tn.esprit.autoloc.autolocapi.repository.IReservationRepository;
import tn.esprit.autoloc.autolocapi.web.dto.ContratDTO;
import tn.esprit.autoloc.autolocapi.web.mapper.ContratMapper;

@Service
@RequiredArgsConstructor
public class ContratServiceImpl implements IContratService {

  private final IContratRepository contratRepository;
  private final IReservationRepository reservationRepository;
  private final ContratMapper contratMapper;

  @Override
  public Contrat create(Contrat contrat) {
    if (contrat.getIdContrat() != null) {
      throw new IllegalArgumentException("Un nouveau contrat ne doit pas avoir d'identifiant");
    }
    if (contrat.getMontantTotal() != null && contrat.getMontantTotal().compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Le montant total ne peut pas être négatif");
    }
    return contratRepository.save(contrat);
  }

  @Override
  public Contrat findById(Long id) {
    return contratRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Contrat", id));
  }

  @Override
  public List<Contrat> findAll() {
    return contratRepository.findAll();
  }

  @Override
  public Contrat update(Long id, Contrat contrat) {
    Contrat existant = findById(id);
    if (contrat.getMontantTotal() != null && contrat.getMontantTotal().compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Le montant total ne peut pas être négatif");
    }
    existant.setDateSignature(contrat.getDateSignature());
    existant.setMontantTotal(contrat.getMontantTotal());
    existant.setValide(contrat.isValide());
    existant.setReservation(contrat.getReservation());
    return contratRepository.save(existant);
  }

  @Override
  public void deleteById(Long id) {
    if (!contratRepository.existsById(id)) {
      throw new ResourceNotFoundException("Contrat", id);
    }
    contratRepository.deleteById(id);
  }

  @Override
  @Transactional
  public ContratDTO affecterContratAReservation(LocalDate dateSignature, LocalDate dateDebut, LocalDate dateFin) {
    // 1. Chercher une réservation correspondant aux dates
    List<Reservation> reservations = reservationRepository.findAll();
    Reservation reservation = reservations.stream()
        .filter(r -> r.getDateDebut().equals(dateDebut) && r.getDateFin().equals(dateFin))
        .filter(r -> r.getContrat() == null) // seulement les réservations sans contrat
        .findFirst()
        .orElseThrow(() -> new ResourceNotFoundException(
            "Aucune réservation sans contrat trouvée avec dateDebut=" + dateDebut + " et dateFin=" + dateFin));

    // 2. Calculer le montant total
    long nbJours = ChronoUnit.DAYS.between(dateDebut, dateFin);
    if (nbJours <= 0) nbJours = 1;
    BigDecimal montantTotal = BigDecimal.ZERO;
    if (reservation.getVehicule() != null && reservation.getVehicule().getTarifJournalier() != null) {
      montantTotal = reservation.getVehicule().getTarifJournalier().multiply(BigDecimal.valueOf(nbJours));
    }

    // 3. Créer et sauvegarder le contrat
    Contrat contrat = new Contrat();
    contrat.setDateSignature(dateSignature);
    contrat.setMontantTotal(montantTotal);
    contrat.setValide(true);
    contrat.setReservation(reservation);
    Contrat saved = contratRepository.save(contrat);

    // 4. Confirmer la réservation
    reservation.setStatut(StatutReservation.CONFIRMEE);
    reservationRepository.save(reservation);

    return contratMapper.toDTO(saved);
  }
}
