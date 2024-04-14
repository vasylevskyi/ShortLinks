package ua.goit.shortlinks.links.dto.update;

import lombok.Data;

@Data
public class UpdateLinkRequest  {
    private long id;
    private String shortLink;
    private String originalLink;
}
