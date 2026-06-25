package in.vvm.butly.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.vvm.butly.entity.Butly;

@Repository
public interface ButlyRepository extends JpaRepository<Butly, Long> {

    Optional<Butly> findByShortUrl(String shortUrl);
}
