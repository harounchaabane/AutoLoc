package tn.esprit.autoloc.autolocapi.service;

import java.time.LocalDate;
import java.util.List;
import tn.esprit.autoloc.autolocapi.domain.Contrat;
import tn.esprit.autoloc.autolocapi.web.dto.ContratDTO;

public interface IContratService {
  Contrat create(Contrat contrat);
  Contrat findById(Long id);
  List<Contrat> findAll();
  Contrat update(Long id, Contrat contrat);
  void deleteById(Long id);

  // Service d'affectation
  ContratDTO affecterContratAReservation(LocalDate dateSignature, LocalDate dateDebut, LocalDate dateFin);
}
