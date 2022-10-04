package dev.saha.otpauthserverweb.repository;


import dev.saha.otpauthserverweb.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token>  findTokenByEmployee_UserName(String userName);
    boolean existsByEmployee_UserName(String userName);
}
