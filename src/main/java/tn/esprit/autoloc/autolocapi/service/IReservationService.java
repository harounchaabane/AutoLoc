package tn.esprit.autoloc.autolocapi.service;

import java.util.List;
import tn.esprit.autoloc.autolocapi.domain.Reservation;
import tn.esprit.autoloc.autolocapi.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.autolocapi.web.dto.ReservationWithContratRequestDTO;

public interface IReservationService {
  Reservation create(Reservation reservation);
  Reservation findById(Long id);
  List<Reservation> findAll();
  Reservation update(Long id, Reservation reservation);
  void deleteById(Long id);

  // Services d'affectation
  ReservationResponseDTO affecterReservationAClient(Long idReservation, String nom, String prenom);
  ReservationResponseDTO creerReservationEtContratAssocie(ReservationWithContratRequestDTO requestDTO);
}
