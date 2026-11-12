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
import tn.esprit.autoloc.autolocapi.domain.Client;
import tn.esprit.autoloc.autolocapi.service.IClientService;
import tn.esprit.autoloc.autolocapi.web.dto.ClientRequestDTO;
import tn.esprit.autoloc.autolocapi.web.dto.ClientResponseDTO;
import tn.esprit.autoloc.autolocapi.web.mapper.ClientMapper;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Tag(name = "Client", description = "Gestion des clients")
public class ClientController {

  private final IClientService clientService;
  private final ClientMapper clientMapper;

  @PostMapping
  @Operation(summary = "Creer un client", description = "Cree un nouveau client")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Client cree avec succes"),
      @ApiResponse(responseCode = "400", description = "Donnees invalides")
  })
  public ResponseEntity<ClientResponseDTO> create(@Valid @RequestBody ClientRequestDTO dto) {
    Client client = clientMapper.toEntity(dto);
    Client created = clientService.create(client);
    return ResponseEntity.status(HttpStatus.CREATED).body(clientMapper.toResponseDTO(created));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Obtenir un client", description = "Recupere un client par son identifiant")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Client trouve"),
      @ApiResponse(responseCode = "404", description = "Client introuvable")
  })
  public ResponseEntity<ClientResponseDTO> findById(@PathVariable Long id) {
    Client client = clientService.findById(id);
    return ResponseEntity.ok(clientMapper.toResponseDTO(client));
  }

  @GetMapping
  @Operation(summary = "Lister les clients", description = "Recupere la liste de tous les clients")
  @ApiResponse(responseCode = "200", description = "Liste des clients recuperee")
  public ResponseEntity<List<ClientResponseDTO>> findAll() {
    List<ClientResponseDTO> clients = clientService.findAll().stream()
        .map(clientMapper::toResponseDTO)
        .collect(Collectors.toList());
    return ResponseEntity.ok(clients);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Modifier un client", description = "Met a jour un client existant")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Client modifie avec succes"),
      @ApiResponse(responseCode = "400", description = "Donnees invalides"),
      @ApiResponse(responseCode = "404", description = "Client introuvable")
  })
  public ResponseEntity<ClientResponseDTO> update(
      @PathVariable Long id, @Valid @RequestBody ClientRequestDTO dto) {
    Client existant = clientService.findById(id);
    clientMapper.updateEntityFromDTO(dto, existant);
    Client updated = clientService.update(id, existant);
    return ResponseEntity.ok(clientMapper.toResponseDTO(updated));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Supprimer un client", description = "Supprime un client par son identifiant")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Client supprime"),
      @ApiResponse(responseCode = "404", description = "Client introuvable")
  })
  public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    clientService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
