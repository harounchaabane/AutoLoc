package tn.esprit.autoloc.autolocapi.web.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.autolocapi.domain.Contrat;
import tn.esprit.autoloc.autolocapi.service.IContratService;
import tn.esprit.autoloc.autolocapi.web.dto.ContratDTO;
import tn.esprit.autoloc.autolocapi.web.mapper.ContratMapper;

@RestController
@RequestMapping("/api/contrats")
@RequiredArgsConstructor
public class ContratController {

  private final IContratService contratService;
  private final ContratMapper contratMapper;

  @PostMapping
  public ResponseEntity<ContratDTO> create(@RequestBody Contrat contrat) {
    Contrat created = contratService.create(contrat);
    return ResponseEntity.status(HttpStatus.CREATED).body(contratMapper.toDTO(created));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ContratDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(contratMapper.toDTO(contratService.findById(id)));
  }

  @GetMapping
  public ResponseEntity<List<ContratDTO>> findAll() {
    List<ContratDTO> dtos = contratService.findAll().stream()
        .map(contratMapper::toDTO)
        .collect(Collectors.toList());
    return ResponseEntity.ok(dtos);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ContratDTO> update(@PathVariable Long id, @RequestBody Contrat contrat) {
    return ResponseEntity.ok(contratMapper.toDTO(contratService.update(id, contrat)));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    contratService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  // ========== Service d'affectation ==========

  @PostMapping("/affecter-reservation")
  public ResponseEntity<ContratDTO> affecterContratAReservation(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateSignature,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(contratService.affecterContratAReservation(dateSignature, dateDebut, dateFin));
  }
}
