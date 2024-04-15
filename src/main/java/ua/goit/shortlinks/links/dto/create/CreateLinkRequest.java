package ua.goit.shortlinks.links.dto.create;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class CreateLinkRequest  {
    private String shortLink;
    @Column(name = "original_link")
    private String originalLink;
}
