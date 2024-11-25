package com.laterna.xaxaxa.repository;

import com.laterna.xaxaxa.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);


    List<Token> findAllValidTokensByUserId(Long userId);
}