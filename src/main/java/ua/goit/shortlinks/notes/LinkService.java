package ua.goit.shortlinks.notes;

import ua.goit.shortlinks.notes.dto.create.CreateLinkRequest;
import ua.goit.shortlinks.notes.dto.create.CreateLinkResponse;

import ua.goit.shortlinks.notes.dto.delete.DeleteLinkResponse;
import ua.goit.shortlinks.notes.dto.get.GetUserLinksResponse;
import ua.goit.shortlinks.notes.dto.update.UpdateLinkRequest;
import ua.goit.shortlinks.notes.dto.update.UpdateLinkResponse;
import ua.goit.shortlinks.users.UserService;

import ua.goit.shortlinks.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LinkService {
    private static final int MAX_TITLE_LENGTH = 100;
    private static final int MAX_CONTENT_LENGTH = 1000;

    private final UserService userService;
    private final LinkRepository repository;

    public CreateLinkResponse create(String username, CreateLinkRequest request) {
        Optional<CreateLinkResponse.Error> validationError = validateCreateFields(request);

        if (validationError.isPresent()) {
            return CreateLinkResponse.failed(validationError.get());
        }

        User user = userService.findByUsername(username);

        Link createdLink = repository.save(Link.builder()
                .user(user)
                .shortLink(request.getShortLink())
                .originalLink(request.getOriginalLink())
                .build());

        return CreateLinkResponse.success(createdLink.getId());
    }

    public GetUserLinksResponse getUserLinks(String username) {
        List<Link> userLinks = repository.getUserLinks(username);

        return GetUserLinksResponse.success(userLinks);
    }

    public UpdateLinkResponse update(String username, UpdateLinkRequest request) {
        Optional<Link> optionalLink = repository.findById(request.getId());

        if (optionalLink.isEmpty()) {
            return UpdateLinkResponse.failed(UpdateLinkResponse.Error.invalidLinkId);
        }

        Link link = optionalLink.get();

        boolean isNotUserLink = isNotUserLink(username, link);

        if (isNotUserLink) {
            return UpdateLinkResponse.failed(UpdateLinkResponse.Error.insufficientPrivileges);
        }

        Optional<UpdateLinkResponse.Error> validationError = validateUpdateFields(request);

        if (validationError.isPresent()) {
            return UpdateLinkResponse.failed(validationError.get());
        }

        link.setShortLink(request.getShortLink());
        link.setOriginalLink(request.getOriginalLink());

        repository.save(link);

        return UpdateLinkResponse.success(link);
    }

    public DeleteLinkResponse delete(String username, long id) {
        Optional<Link> optionalLink = repository.findById(id);

        if (optionalLink.isEmpty()) {
            return DeleteLinkResponse.failed(DeleteLinkResponse.Error.invalidLinkId);
        }

        Link link = optionalLink.get();
        boolean isNotUserLink = isNotUserLink(username, link);

        if (isNotUserLink ) {
            return DeleteLinkResponse.failed(DeleteLinkResponse.Error.insufficientPrivileges);
        }

        repository.delete(link);

        return DeleteLinkResponse.success();
    }

    private Optional<CreateLinkResponse.Error> validateCreateFields(CreateLinkRequest request) {
        if (Objects.isNull(request.getShortLink()) || request.getShortLink().isEmpty()) {
            return Optional.of(CreateLinkResponse.Error.invalidShortLink);
        }

        if (Objects.isNull(request.getOriginalLink()) || request.getOriginalLink().isEmpty()) {
            return Optional.of(CreateLinkResponse.Error.invalidOriginalLink);
        }
        return Optional.empty();
    }

    private Optional<UpdateLinkResponse.Error> validateUpdateFields(UpdateLinkRequest request) {
        if (Objects.isNull(request.getShortLink()) || request.getShortLink().isEmpty()) {
            return Optional.of(UpdateLinkResponse.Error.invalidShortLinkLength);
        }

        if (Objects.isNull(request.getOriginalLink()) || request.getOriginalLink().isEmpty()) {
            return Optional.of(UpdateLinkResponse.Error.invalidOriginalLinkLength);
        }

        return Optional.empty();
    }

    private boolean isNotUserLink(String username, Link link) {
        return !link.getUser().getUserId().equals(username);
    }
}
