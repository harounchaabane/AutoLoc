package tn.esprit.autoloc.autolocapi.web.controller;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.autolocapi.domain.Reservation;
import tn.esprit.autoloc.autolocapi.service.IReservationService;
import tn.esprit.autoloc.autolocapi.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.autolocapi.web.dto.ReservationWithContratRequestDTO;
import tn.esprit.autoloc.autolocapi.web.mapper.ReservationMapper;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

  private final IReservationService reservationService;
  private final ReservationMapper reservationMapper;

  @PostMapping
  public ResponseEntity<ReservationResponseDTO> create(@RequestBody Reservation reservation) {
    Reservation created = reservationService.create(reservation);
    return ResponseEntity.status(HttpStatus.CREATED).body(reservationMapper.toResponseDTO(created));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ReservationResponseDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(reservationMapper.toResponseDTO(reservationService.findById(id)));
  }

  @GetMapping
  public ResponseEntity<List<ReservationResponseDTO>> findAll() {
    List<ReservationResponseDTO> dtos = reservationService.findAll().stream()
        .map(reservationMapper::toResponseDTO)
        .collect(Collectors.toList());
    return ResponseEntity.ok(dtos);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ReservationResponseDTO> update(@PathVariable Long id, @RequestBody Reservation reservation) {
    return ResponseEntity.ok(reservationMapper.toResponseDTO(reservationService.update(id, reservation)));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    reservationService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  // ========== Services d'affectation ==========

  @PutMapping("/{idReservation}/affecter-client")
  public ResponseEntity<ReservationResponseDTO> affecterReservationAClient(
      @PathVariable Long idReservation,
      @RequestParam String nom,
      @RequestParam String prenom) {
    return ResponseEntity.ok(reservationService.affecterReservationAClient(idReservation, nom, prenom));
  }

  @PostMapping("/avec-contrat")
  public ResponseEntity<ReservationResponseDTO> creerReservationEtContratAssocie(
      @RequestBody ReservationWithContratRequestDTO requestDTO) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(reservationService.creerReservationEtContratAssocie(requestDTO));
  }
}
