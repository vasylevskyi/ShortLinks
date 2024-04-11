package ua.goit.shortlinks.notes.dto.update;

import lombok.Data;

@Data
public class UpdateNoteRequest {
    private long id;
    private String title;
    private String content;
}
