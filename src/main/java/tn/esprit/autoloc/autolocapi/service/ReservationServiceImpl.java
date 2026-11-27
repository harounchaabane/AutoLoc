package tn.esprit.autoloc.autolocapi.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.autolocapi.domain.*;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autoloc.autolocapi.repository.IClientRepository;
import tn.esprit.autoloc.autolocapi.repository.IContratRepository;
import tn.esprit.autoloc.autolocapi.repository.IReservationRepository;
import tn.esprit.autoloc.autolocapi.repository.IVehiculeRepository;
import tn.esprit.autoloc.autolocapi.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.autolocapi.web.dto.ReservationWithContratRequestDTO;
import tn.esprit.autoloc.autolocapi.web.mapper.ReservationMapper;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {

  private final IReservationRepository reservationRepository;
  private final IClientRepository clientRepository;
  private final IVehiculeRepository vehiculeRepository;
  private final IContratRepository contratRepository;
  private final ReservationMapper reservationMapper;

  @Override
  public Reservation create(Reservation reservation) {
    if (reservation.getIdReservation() != null) {
      throw new IllegalArgumentException("Une nouvelle réservation ne doit pas avoir d'identifiant");
    }
    validateDates(reservation);
    return reservationRepository.save(reservation);
  }

  @Override
  public Reservation findById(Long id) {
    return reservationRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
  }

  @Override
  public List<Reservation> findAll() {
    return reservationRepository.findAll();
  }

  @Override
  public Reservation update(Long id, Reservation reservation) {
    validateDates(reservation);
    Reservation existant = findById(id);
    existant.setDateDebut(reservation.getDateDebut());
    existant.setDateFin(reservation.getDateFin());
    existant.setStatut(reservation.getStatut());
    existant.setVehicule(reservation.getVehicule());
    existant.setClient(reservation.getClient());
    return reservationRepository.save(existant);
  }

  @Override
  public void deleteById(Long id) {
    if (!reservationRepository.existsById(id)) {
      throw new ResourceNotFoundException("Reservation", id);
    }
    reservationRepository.deleteById(id);
  }

  @Override
  @Transactional
  public ReservationResponseDTO affecterReservationAClient(Long idReservation, String nom, String prenom) {
    // 1. Récupérer la réservation
    Reservation reservation = reservationRepository.findById(idReservation)
        .orElseThrow(() -> new ResourceNotFoundException("Reservation", idReservation));

    // 2. Chercher le client par nom et prénom
    Client client = clientRepository.findByNomAndPrenom(nom, prenom)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Client non trouvé avec nom=" + nom + " et prenom=" + prenom));

    // 3. Affecter le client à la réservation
    reservation.setClient(client);
    Reservation saved = reservationRepository.save(reservation);

    // 4. Retourner le DTO
    return reservationMapper.toResponseDTO(saved);
  }

  @Override
  @Transactional
  public ReservationResponseDTO creerReservationEtContratAssocie(ReservationWithContratRequestDTO requestDTO) {
    // 1. Récupérer le véhicule
    Vehicule vehicule = vehiculeRepository.findById(requestDTO.getVehiculeId())
        .orElseThrow(() -> new ResourceNotFoundException("Vehicule", requestDTO.getVehiculeId()));

    // 2. Récupérer le client
    Client client = clientRepository.findById(requestDTO.getClientId())
        .orElseThrow(() -> new ResourceNotFoundException("Client", requestDTO.getClientId()));

    // 3. Créer la réservation
    Reservation reservation = new Reservation();
    reservation.setDateDebut(requestDTO.getDateDebut());
    reservation.setDateFin(requestDTO.getDateFin());
    reservation.setStatut(requestDTO.getStatut() != null ? requestDTO.getStatut() : StatutReservation.EN_ATTENTE);
    reservation.setVehicule(vehicule);
    reservation.setClient(client);
    validateDates(reservation);
    Reservation savedReservation = reservationRepository.save(reservation);

    // 4. Calculer le montant total (nombre de jours × tarif journalier)
    long nbJours = ChronoUnit.DAYS.between(requestDTO.getDateDebut(), requestDTO.getDateFin());
    if (nbJours <= 0) nbJours = 1;
    BigDecimal montantTotal = vehicule.getTarifJournalier().multiply(BigDecimal.valueOf(nbJours));

    // 5. Créer le contrat associé
    Contrat contrat = new Contrat();
    contrat.setDateSignature(requestDTO.getDateSignature());
    contrat.setMontantTotal(montantTotal);
    contrat.setValide(true);
    contrat.setReservation(savedReservation);
    contratRepository.save(contrat);

    // 6. Mettre à jour la référence bidirectionnelle
    savedReservation.setContrat(contrat);

    return reservationMapper.toResponseDTO(savedReservation);
  }

  private void validateDates(Reservation reservation) {
    if (reservation.getDateDebut() == null || reservation.getDateFin() == null) {
      throw new IllegalArgumentException("Les dates de début et de fin de réservation sont obligatoires");
    }
    if (reservation.getDateFin().isBefore(reservation.getDateDebut())) {
      throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
    }
  }
}
