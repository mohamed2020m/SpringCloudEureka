package com.leeuw.controller;

import com.leeuw.entities.Client;
import com.leeuw.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClientController{
    @Autowired
    private ClientRepository clientRepository ;

    @GetMapping("/clients")
    public List<Client> findAll(){
        return clientRepository.findAll();
    }

    @GetMapping("/clients/{id}")
    public Client findById(@PathVariable("id") Long id) throws Exception {
        return clientRepository.findById(id).orElseThrow(() -> new Exception("Client inexistent"));
    }

}
