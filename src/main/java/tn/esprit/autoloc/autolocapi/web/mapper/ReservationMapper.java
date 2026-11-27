package tn.esprit.autoloc.autolocapi.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.autolocapi.domain.Reservation;
import tn.esprit.autoloc.autolocapi.web.dto.ReservationResponseDTO;

@Component
public class ReservationMapper {

  public ReservationResponseDTO toResponseDTO(Reservation reservation) {
    ReservationResponseDTO dto = new ReservationResponseDTO();
    dto.setIdReservation(reservation.getIdReservation());
    dto.setDateDebut(reservation.getDateDebut());
    dto.setDateFin(reservation.getDateFin());
    dto.setStatut(reservation.getStatut());
    if (reservation.getClient() != null) {
      dto.setClientNom(reservation.getClient().getNom());
      dto.setClientPrenom(reservation.getClient().getPrenom());
      dto.setClientId(reservation.getClient().getIdClient());
    }
    if (reservation.getVehicule() != null) {
      dto.setVehiculeImmatriculation(reservation.getVehicule().getImmatriculation());
      dto.setVehiculeMarque(reservation.getVehicule().getMarque());
      dto.setVehiculeModele(reservation.getVehicule().getModele());
      dto.setVehiculeId(reservation.getVehicule().getIdVehicule());
    }
    if (reservation.getContrat() != null) {
      dto.setContratId(reservation.getContrat().getIdContrat());
    }
    return dto;
  }
}
