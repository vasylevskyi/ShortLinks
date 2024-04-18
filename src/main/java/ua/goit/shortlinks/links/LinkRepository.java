package ua.goit.shortlinks.links;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM links l WHERE l.user_id = :userId")
    List<Link> getUserLinks(@Param("userId") String userId); Комент 18 04 причина, не используеться нигде
//    @Query(nativeQuery = true, value = "SELECT * FROM links l WHERE l.short_link = :shortLink")//добавлено 18 04

    boolean existsByShortLink(String shortLink);
    Optional<Link> findByShortLink(String shortLink);//ДОБАВЛЕНО
    List<Link> getUserLinksByUserId(String userId);//ДОБАВЛЕНО

    ////////////////

}*/
@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
//    @Query(nativeQuery = true, value = "SELECT * FROM links l WHERE l.user_id = :userId")
//    List<Link> getUserLinks(@Param("userId") String userId);
    boolean existsByShortLink(String shortLink);
    Optional<Link> findByShortLink(String shortLink);//ДОБАВЛЕНО
    @Query(nativeQuery = true, value = "SELECT * FROM links l WHERE l.user_id = :userId")
    List<Link> getUserLinksByUserId(@Param("userId") String userId);//ДОБАВЛЕНО
}