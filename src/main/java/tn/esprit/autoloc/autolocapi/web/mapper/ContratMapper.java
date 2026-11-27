package tn.esprit.autoloc.autolocapi.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.autolocapi.domain.Contrat;
import tn.esprit.autoloc.autolocapi.web.dto.ContratDTO;

@Component
public class ContratMapper {

  public ContratDTO toDTO(Contrat contrat) {
    ContratDTO dto = new ContratDTO();
    dto.setIdContrat(contrat.getIdContrat());
    dto.setDateSignature(contrat.getDateSignature());
    dto.setMontantTotal(contrat.getMontantTotal());
    dto.setValide(contrat.isValide());
    if (contrat.getReservation() != null) {
      dto.setReservationId(contrat.getReservation().getIdReservation());
      dto.setReservationDateDebut(contrat.getReservation().getDateDebut());
      dto.setReservationDateFin(contrat.getReservation().getDateFin());
      if (contrat.getReservation().getClient() != null) {
        dto.setClientNom(contrat.getReservation().getClient().getNom());
        dto.setClientPrenom(contrat.getReservation().getClient().getPrenom());
      }
    }
    return dto;
  }
}
