package ua.goit.shortlinks.notes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM links l WHERE l.user_id = :userId") ////CHECK!!!
    List<Link> getUserLinks(@Param("userId") String userId);
}