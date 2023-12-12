package com.leeuw.voiture;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoitureResult {
    private Long id;
    private String marque;
    private String matricule;
    private String model;
    private Client client;
}
