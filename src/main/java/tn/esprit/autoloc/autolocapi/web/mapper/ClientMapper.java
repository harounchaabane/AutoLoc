package tn.esprit.autoloc.autolocapi.web.mapper;

import java.time.LocalDate;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.autolocapi.domain.Client;
import tn.esprit.autoloc.autolocapi.web.dto.ClientRequestDTO;
import tn.esprit.autoloc.autolocapi.web.dto.ClientResponseDTO;

@Component
public class ClientMapper {

  public Client toEntity(ClientRequestDTO dto) {
    Client client = new Client();
    client.setNom(dto.getNom());
    client.setPrenom(dto.getPrenom());
    client.setEmail(dto.getEmail());
    client.setTelephone(dto.getTelephone());
    client.setNumPermis(dto.getNumPermis());
    client.setDateInscription(LocalDate.now());
    return client;
  }

  public ClientResponseDTO toResponseDTO(Client client) {
    ClientResponseDTO dto = new ClientResponseDTO();
    dto.setIdClient(client.getIdClient());
    dto.setNom(client.getNom());
    dto.setPrenom(client.getPrenom());
    dto.setEmail(client.getEmail());
    dto.setTelephone(client.getTelephone());
    dto.setNumPermis(client.getNumPermis());
    dto.setDateInscription(client.getDateInscription());
    dto.setNombreReservations(client.getReservations() != null ? client.getReservations().size() : 0);
    return dto;
  }

  public void updateEntityFromDTO(ClientRequestDTO dto, Client client) {
    client.setNom(dto.getNom());
    client.setPrenom(dto.getPrenom());
    client.setEmail(dto.getEmail());
    client.setTelephone(dto.getTelephone());
    client.setNumPermis(dto.getNumPermis());
  }
}
