package ua.goit.shortlinks.links;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    boolean existsByShortLink(String shortLink);
    Optional<Link> findByShortLink(String shortLink);//ДОБАВЛЕНО
    @Query(nativeQuery = true, value = "SELECT * FROM links l WHERE l.user_id = :userId")
    List<Link> getUserLinksByUserId(@Param("userId") String userId);//ДОБАВЛЕНО
}