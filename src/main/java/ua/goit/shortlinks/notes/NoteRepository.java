package ua.goit.shortlinks.notes;

import ua.goit.shortlinks.notes.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM notes n WHERE n.user_id = :username")
    List<Note> getUserNotes(@Param("username") String username);
}