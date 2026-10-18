package tn.esprit.autoloc.autolocapi.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.autolocapi.domain.Reservation;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autoloc.autolocapi.repository.IReservationRepository;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {

  private final IReservationRepository reservationRepository;

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

  private void validateDates(Reservation reservation) {
    if (reservation.getDateDebut() == null || reservation.getDateFin() == null) {
      throw new IllegalArgumentException("Les dates de début et de fin de réservation sont obligatoires");
    }
    if (reservation.getDateFin().isBefore(reservation.getDateDebut())) {
      throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
    }
  }
}
