package com.leeuw.controller;


import com.leeuw.voiture.Client;
import com.leeuw.entities.Voiture;
import com.leeuw.repository.VoitureRepository;
import com.leeuw.voiture.ClientService;
import com.leeuw.voiture.VoitureResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class VoitureController {


    @Autowired
    private ClientService clientService;

    @Autowired
    private VoitureRepository voitureRepository;

    @GetMapping(value = "/voitures", produces = {"application/json"})
    public ResponseEntity<Object> findAll() {
        try {
            List<Voiture> voitures = voitureRepository.findAll();
            return ResponseEntity.ok(getVoitures(voitures));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching voitures: " + e.getMessage());
        }
    }

    @GetMapping("/voitures/{Id}")
    public ResponseEntity<Object> findById(@PathVariable Long Id) {
        try {
            Voiture voiture = voitureRepository.findById(Id)
                    .orElseThrow(() -> new Exception("Voiture Introuvable"));

            // Fetch the client details using the clientService
            voiture.setClient(clientService.clientById(voiture.getClient().getId()));

            return ResponseEntity.ok(voiture);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Voiture not found with ID: " + Id);
        }
    }

    @GetMapping("/voitures/client/{Id}")
    public ResponseEntity<List<VoitureResult>> findByClient(@PathVariable Long Id) {
        try {
            Client client = clientService.clientById(Id);
            if (client != null) {
                List<Voiture> voitures = voitureRepository.findByIdClient(Id);
                return ResponseEntity.ok(getVoitures(voitures));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/voitures/{clientId}")
    public ResponseEntity<Object> save(@PathVariable Long clientId, @RequestBody Voiture voiture) {
        try {
            // Fetch the client details using the clientService
            Client client = clientService.clientById(clientId);

            if (client != null) {
                // Set the fetched client in the voiture object
                voiture.setClient(client);

                // Save the Voiture with the associated Client
//                voiture.setId_client(clientId);
                voiture.setClient(client);
                Voiture savedVoiture = voitureRepository.save(voiture);

                return ResponseEntity.ok(savedVoiture);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Client not found with ID: " + clientId);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving voiture: " + e.getMessage());
        }
    }


    @PutMapping("/voitures/{Id}")
    public ResponseEntity<Object> update(@PathVariable Long Id, @RequestBody
    Voiture updatedVoiture) {
        try {
            Voiture existingVoiture = voitureRepository.findById(Id)
                    .orElseThrow(() -> new Exception("Voiture not found with ID: "
                            + Id));

            // Update only the non-null fields from the request body
            if (updatedVoiture.getMatricule() != null &&
                    !updatedVoiture.getMatricule().isEmpty()) {
                existingVoiture.setMatricule(updatedVoiture.getMatricule());
            }

            if (updatedVoiture.getMarque() != null &&
                    !updatedVoiture.getMarque().isEmpty()) {
                existingVoiture.setMarque(updatedVoiture.getMarque());
            }

            if (updatedVoiture.getModel() != null &&
                    !updatedVoiture.getModel().isEmpty()) {
                existingVoiture.setModel(updatedVoiture.getModel());
            }


            // Save the updated Voiture
            Voiture savedVoiture = voitureRepository.save(existingVoiture);

            return ResponseEntity.ok(savedVoiture);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating voiture: " + e.getMessage());
        }
    }

    private List<VoitureResult> getVoitures(List<Voiture> voitures){
        List<VoitureResult> voitureResults = new ArrayList<>();
        for (Voiture voiture : voitures) {
            VoitureResult voitureResult = new VoitureResult();

            Client client = clientService.clientById(voiture.getId_client());

            voitureResult.setId(voiture.getId());
            voitureResult.setModel(voiture.getModel());
            voitureResult.setMarque(voiture.getMarque());
            voitureResult.setMatricule(voiture.getMatricule());
            voitureResult.setClient(client);

            voitureResults.add(voitureResult);
        }

        return voitureResults;
    }
}
