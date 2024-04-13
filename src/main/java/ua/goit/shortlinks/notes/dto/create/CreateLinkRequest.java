package ua.goit.shortlinks.notes.dto.create;

import lombok.Data;

@Data
public class CreateLinkRequest  {
    private String shortLink;
    private String originalLink;
}
