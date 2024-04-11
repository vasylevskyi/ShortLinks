package ua.goit.shortlinks.notes;

import ua.goit.shortlinks.notes.dto.create.CreateNoteRequest;
import ua.goit.shortlinks.notes.dto.create.CreateNoteResponse;
import ua.goit.shortlinks.notes.dto.delete.DeleteNoteResponse;
import ua.goit.shortlinks.notes.dto.get.GetUserNotesResponse;
import ua.goit.shortlinks.notes.dto.update.UpdateNoteRequest;
import ua.goit.shortlinks.notes.dto.update.UpdateNoteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;

    @PostMapping
    public CreateNoteResponse create(Principal principal, @RequestBody CreateNoteRequest request) {
        return noteService.create(principal.getName(), request);
    }

    @GetMapping ("/getUserNotes")
    public GetUserNotesResponse getUserNotes(Principal principal) {
        return noteService.getUserNotes(principal.getName());
    }

    @PatchMapping
    public UpdateNoteResponse update(Principal principal, @RequestBody UpdateNoteRequest request) {
        return noteService.update(principal.getName(), request);
    }

    @DeleteMapping
    public DeleteNoteResponse delete(Principal principal, @RequestParam(name = "id") long id) {
        return noteService.delete(principal.getName(), id);
    }
}
