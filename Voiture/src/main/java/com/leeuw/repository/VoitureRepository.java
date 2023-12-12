package com.leeuw.repository;

import com.leeuw.entities.Voiture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoitureRepository extends JpaRepository<Voiture, Long> {
    @Query("From Voiture where id_client = ?1")
    List<Voiture> findByIdClient(Long clientId);
}
