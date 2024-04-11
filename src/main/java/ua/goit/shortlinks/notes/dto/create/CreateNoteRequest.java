package ua.goit.shortlinks.notes.dto.create;

import lombok.Data;

@Data
public class CreateNoteRequest {
    private String title;
    private String content;
}
