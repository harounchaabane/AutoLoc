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
import tn.esprit.autoloc.autolocapi.domain.Agence;
import tn.esprit.autoloc.autolocapi.service.IAgenceService;
import tn.esprit.autoloc.autolocapi.web.dto.AgenceRequestDTO;
import tn.esprit.autoloc.autolocapi.web.dto.AgenceResponseDTO;
import tn.esprit.autoloc.autolocapi.web.mapper.AgenceMapper;

@RestController
@RequestMapping("/api/agences")
@RequiredArgsConstructor
@Tag(name = "Agence", description = "Gestion des agences de location")
public class AgenceController {

  private final IAgenceService agenceService;
  private final AgenceMapper agenceMapper;

  @PostMapping
  @Operation(summary = "Creer une agence", description = "Cree une nouvelle agence de location")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Agence creee avec succes"),
      @ApiResponse(responseCode = "400", description = "Donnees invalides")
  })
  public ResponseEntity<AgenceResponseDTO> create(@Valid @RequestBody AgenceRequestDTO dto) {
    Agence agence = agenceMapper.toEntity(dto);
    Agence created = agenceService.create(agence);
    return ResponseEntity.status(HttpStatus.CREATED).body(agenceMapper.toResponseDTO(created));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Obtenir une agence", description = "Recupere une agence par son identifiant")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Agence trouvee"),
      @ApiResponse(responseCode = "404", description = "Agence introuvable")
  })
  public ResponseEntity<AgenceResponseDTO> findById(@PathVariable Long id) {
    Agence agence = agenceService.findById(id);
    return ResponseEntity.ok(agenceMapper.toResponseDTO(agence));
  }

  @GetMapping
  @Operation(summary = "Lister les agences", description = "Recupere la liste de toutes les agences")
  @ApiResponse(responseCode = "200", description = "Liste des agences recuperee")
  public ResponseEntity<List<AgenceResponseDTO>> findAll() {
    List<AgenceResponseDTO> agences = agenceService.findAll().stream()
        .map(agenceMapper::toResponseDTO)
        .collect(Collectors.toList());
    return ResponseEntity.ok(agences);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Modifier une agence", description = "Met a jour une agence existante")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Agence modifiee avec succes"),
      @ApiResponse(responseCode = "400", description = "Donnees invalides"),
      @ApiResponse(responseCode = "404", description = "Agence introuvable")
  })
  public ResponseEntity<AgenceResponseDTO> update(
      @PathVariable Long id, @Valid @RequestBody AgenceRequestDTO dto) {
    Agence agence = agenceMapper.toEntity(dto);
    Agence updated = agenceService.update(id, agence);
    return ResponseEntity.ok(agenceMapper.toResponseDTO(updated));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Supprimer une agence", description = "Supprime une agence par son identifiant")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Agence supprimee"),
      @ApiResponse(responseCode = "404", description = "Agence introuvable")
  })
  public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    agenceService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
