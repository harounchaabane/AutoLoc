package tn.esprit.autoloc.autolocapi.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.autolocapi.domain.Vehicule;
import tn.esprit.autoloc.autolocapi.service.IVehiculeService;
import tn.esprit.autoloc.autolocapi.web.dto.VehiculeRequestDTO;
import tn.esprit.autoloc.autolocapi.web.dto.VehiculeResponseDTO;
import tn.esprit.autoloc.autolocapi.web.mapper.VehiculeMapper;

@RestController
@RequestMapping("/api/vehicules")
@RequiredArgsConstructor
@Tag(name = "Vehicule", description = "Gestion des vehicules")
public class VehiculeController {

  private final IVehiculeService vehiculeService;
  private final VehiculeMapper vehiculeMapper;

  @PostMapping
  @Operation(summary = "Creer un vehicule", description = "Cree un nouveau vehicule")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Vehicule cree avec succes"),
      @ApiResponse(responseCode = "400", description = "Donnees invalides")
  })
  public ResponseEntity<VehiculeResponseDTO> create(@Valid @RequestBody VehiculeRequestDTO dto) {
    Vehicule vehicule = vehiculeMapper.toEntity(dto);
    Vehicule created = vehiculeService.create(vehicule);
    return ResponseEntity.status(HttpStatus.CREATED).body(vehiculeMapper.toResponseDTO(created));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Obtenir un vehicule", description = "Recupere un vehicule par son identifiant")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Vehicule trouve"),
      @ApiResponse(responseCode = "404", description = "Vehicule introuvable")
  })
  public ResponseEntity<VehiculeResponseDTO> findById(@PathVariable Long id) {
    Vehicule vehicule = vehiculeService.findById(id);
    return ResponseEntity.ok(vehiculeMapper.toResponseDTO(vehicule));
  }

  @GetMapping
  @Operation(summary = "Lister les vehicules", description = "Recupere la liste de tous les vehicules")
  @ApiResponse(responseCode = "200", description = "Liste des vehicules recuperee")
  public ResponseEntity<List<VehiculeResponseDTO>> findAll() {
    List<VehiculeResponseDTO> vehicules = vehiculeService.findAll().stream()
        .map(vehiculeMapper::toResponseDTO)
        .collect(Collectors.toList());
    return ResponseEntity.ok(vehicules);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Modifier un vehicule", description = "Met a jour un vehicule existant")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Vehicule modifie avec succes"),
      @ApiResponse(responseCode = "400", description = "Donnees invalides"),
      @ApiResponse(responseCode = "404", description = "Vehicule introuvable")
  })
  public ResponseEntity<VehiculeResponseDTO> update(
      @PathVariable Long id, @Valid @RequestBody VehiculeRequestDTO dto) {
    Vehicule existant = vehiculeService.findById(id);
    vehiculeMapper.updateEntityFromDTO(dto, existant);
    Vehicule updated = vehiculeService.update(id, existant);
    return ResponseEntity.ok(vehiculeMapper.toResponseDTO(updated));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Supprimer un vehicule", description = "Supprime un vehicule par son identifiant")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Vehicule supprime"),
      @ApiResponse(responseCode = "404", description = "Vehicule introuvable")
  })
  public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    vehiculeService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
