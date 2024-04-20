package ua.goit.shortlinks.links.dto.update;

import lombok.Data;

@Data
public class UpdateLinkRequest  {
    private String shortLink;
    private String originalLink;
}
