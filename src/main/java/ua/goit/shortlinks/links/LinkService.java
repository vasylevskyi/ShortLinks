package ua.goit.shortlinks.links;

import org.springframework.cache.annotation.Cacheable;
import ua.goit.shortlinks.links.dto.create.CreateLinkRequest;
import ua.goit.shortlinks.links.dto.create.CreateLinkResponse;

import ua.goit.shortlinks.links.dto.delete.DeleteLinkResponse;
import ua.goit.shortlinks.links.dto.get.GetUserLinksResponse;
import ua.goit.shortlinks.links.dto.update.UpdateLinkRequest;
import ua.goit.shortlinks.links.dto.update.UpdateLinkResponse;
import ua.goit.shortlinks.users.UserService;

import ua.goit.shortlinks.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final UserService userService;
    private final LinkRepository repository;
    private static final String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private String generateRandomString() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            char randomChar = ALLOWED_CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    private String generateUniqueShortLink() {
        String shortLink;
        do {
            shortLink = generateRandomString();
        } while (repository.existsByShortLink(shortLink));
        return shortLink;
    }

    public CreateLinkResponse create(String username, CreateLinkRequest request) {
        String[] links = request.getOriginalLink().split(",");
        if (links.length != 1) {
            return CreateLinkResponse.failed(CreateLinkResponse.Error.multipleLinksProvided);
        }

        if (!LinkValidator.isLinkValid(request.getOriginalLink())) {
            return CreateLinkResponse.failed(CreateLinkResponse.Error.invalidLink);
        }
        if (!isValidLinkFormat(request.getOriginalLink())) {
            return CreateLinkResponse.failed(CreateLinkResponse.Error.invalidLinkFormat);
        }

        Optional<CreateLinkResponse.Error> validationError = validateCreateFields(request);

        if (validationError.isPresent()) {
            return CreateLinkResponse.failed(validationError.get());
        }

        User user = userService.findByUsername(username);

        Optional<Link> existingLink = repository.findByOriginalLinkAndUser(request.getOriginalLink(), user);
        if (existingLink.isPresent()) {
            if (existingLink.get().isDeleted()) {
                String shortLink = generateUniqueShortLink();
                Link createdLink = repository.save(Link.builder()
                        .user(user)
                        .originalLink(request.getOriginalLink())
                        .shortLink(shortLink)
                        .build());
                return CreateLinkResponse.success(createdLink.getShortLink(), request.getOriginalLink());
            } else {
                return CreateLinkResponse.failed(CreateLinkResponse.Error.originalLinkAlreadyExists);
            }
        } else {
            String shortLink = generateUniqueShortLink();

            Link createdLink = repository.save(Link.builder()
                    .user(user)
                    .originalLink(request.getOriginalLink())
                    .shortLink(shortLink)
                    .build());

            return CreateLinkResponse.success(createdLink.getShortLink(), request.getOriginalLink());
        }
    }
    public GetUserLinksResponse getUserLinks(String username) {
        List<Link> userLinks = repository.getUserLinksByUserId(username);
        return GetUserLinksResponse.success(userLinks);
    }

    public UpdateLinkResponse update(String username, UpdateLinkRequest request) {

        Optional<Link> optionalLink = repository.findByShortLink(request.getShortLink());

        if (optionalLink.isEmpty()) {
            return UpdateLinkResponse.failed(UpdateLinkResponse.Error.linkNotFound);
        }

        Link link = optionalLink.get();

        boolean isNotUserLink = isNotUserLink(username, link);

        if (isNotUserLink) {
            return UpdateLinkResponse.failed(UpdateLinkResponse.Error.NoLinkAvailable);
        }

        if (link.isDeleted()) {
            return UpdateLinkResponse.failed(UpdateLinkResponse.Error.linkNotExist);
        }

        if (!LinkValidator.isLinkValid(request.getOriginalLink())) {
            return UpdateLinkResponse.failed(UpdateLinkResponse.Error.invalidNewLink);
        }

        Optional<UpdateLinkResponse.Error> validationError = validateUpdateFields(request);

        if (validationError.isPresent()) {
            return UpdateLinkResponse.failed(validationError.get());
        }

        Optional<Link> existingUserLink = repository.findByOriginalLinkAndUser(request.getOriginalLink(), link.getUser());
        if (existingUserLink.isPresent() && !existingUserLink.get().isDeleted()) {
            return UpdateLinkResponse.failed(UpdateLinkResponse.Error.linkAlreadyExist);
        }

        link.setShortLink(request.getShortLink());
        link.setOriginalLink(request.getOriginalLink());

        repository.save(link);

        return UpdateLinkResponse.success(link);
    }

    public DeleteLinkResponse delete(String username, String shortLink) {
        Optional<Link> optionalLink = repository.findByShortLink(shortLink);

        if (optionalLink.isEmpty()) {
            return DeleteLinkResponse.failed(DeleteLinkResponse.Error.linkDoesNotExist);
        }

        Link link = optionalLink.get();
        boolean isNotUserLink = isNotUserLink(username, link);

        if (isNotUserLink) {
            return DeleteLinkResponse.failed(DeleteLinkResponse.Error.linkDoesNotExist);
        }

        if (link.isDeleted()) {
            return DeleteLinkResponse.failed(DeleteLinkResponse.Error.linkAlreadyDeleted);
        }

        link.setDeleted(true);
        repository.save(link);

        return DeleteLinkResponse.success();
    }    private Optional<CreateLinkResponse.Error> validateCreateFields(CreateLinkRequest request) {


/*        if (Objects.isNull(request.getShortLink()) || request.getShortLink().isEmpty()) { ASK GRAVDO
            return Optional.of(CreateLinkResponse.Error.invalidLink);
        }*/

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

    @Cacheable(value = "redirects", key = "#shortLink")
    public Link getByShortLink(String shortLink) {
        return repository.getByShortLink(shortLink);
    }

    public void save(Link link) {
        repository.save(link);
    }
    private boolean isValidLinkFormat(String link) {
        String regex = "^(https?|ftp):\\/\\/[\\w\\d-]+(\\.[\\w\\d-]+)+(\\/\\S*)?$";
        return link.matches(regex);
    }
}