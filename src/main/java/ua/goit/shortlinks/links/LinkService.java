package ua.goit.shortlinks.links;

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
    private static final String PREFIX = "http://localhost:8080/";

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
            shortLink = PREFIX + generateRandomString();
        } while (repository.existsByShortLink(shortLink));
        return shortLink;
    }

    public CreateLinkResponse create(String username, CreateLinkRequest request) {
        Optional<CreateLinkResponse.Error> validationError = validateCreateFields(request);

        if (validationError.isPresent()) {
            return CreateLinkResponse.failed(validationError.get());
        }

        User user = userService.findByUsername(username);

        String shortLink = generateRandomString();

        Link createdLink = repository.save(Link.builder()
                .user(user)
                .shortLink(shortLink)
                .originalLink(request.getOriginalLink())
                .build());

        return CreateLinkResponse.success(createdLink.getId());
    }
    public GetUserLinksResponse getUserLinks(String userId) {//АПДЕЙТ
        List<Link> userLinks = repository.getUserLinksByUserId(userId);
        return GetUserLinksResponse.success(userLinks);
    }
    //    public GetUserLinksResponse getUserLinks(String username) {
//        List<Link> userLinks = repository.getUserLinks(username);
//
//        return GetUserLinksResponse.success(userLinks);
//    }
    public UpdateLinkResponse update(String username, String shortLink, UpdateLinkRequest request) {//АПДЕЙТ
        Optional<Link> optionalLink = repository.findByShortLink(shortLink);

        if (optionalLink.isEmpty()) {
            return UpdateLinkResponse.failed(UpdateLinkResponse.Error.linkNotFound);
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
    //    public UpdateLinkResponse update(String username, UpdateLinkRequest request) {
//        Optional<Link> optionalLink = repository.findById(request.getId());
//
//        if (optionalLink.isEmpty()) {
//            return UpdateLinkResponse.failed(UpdateLinkResponse.Error.invalidLinkId);
//        }
//
//        Link link = optionalLink.get();
//
//        boolean isNotUserLink = isNotUserLink(username, link);
//
//        if (isNotUserLink) {
//            return UpdateLinkResponse.failed(UpdateLinkResponse.Error.insufficientPrivileges);
//        }
//
//        Optional<UpdateLinkResponse.Error> validationError = validateUpdateFields(request);
//
//        if (validationError.isPresent()) {
//            return UpdateLinkResponse.failed(validationError.get());
//        }
//
//        link.setShortLink(request.getShortLink());
//        link.setOriginalLink(request.getOriginalLink());
//
//        repository.save(link);
//
//        return UpdateLinkResponse.success(link);
//    }
    public DeleteLinkResponse delete(String username, String shortLink) {//АПДЕЙТ
        Optional<Link> optionalLink = repository.findByShortLink(shortLink);

        if (optionalLink.isEmpty()) {
            return DeleteLinkResponse.failed(DeleteLinkResponse.Error.linkNotFound);
        }

        Link link = optionalLink.get();
        boolean isNotUserLink = isNotUserLink(username, link);

        if (isNotUserLink) {
            return DeleteLinkResponse.failed(DeleteLinkResponse.Error.insufficientPrivileges);
        }

        // Устанавливаем значение поля isDeleted в true
        link.setDeleted(true);
        repository.save(link);

        return DeleteLinkResponse.success();
    }
//    public DeleteLinkResponse delete(String username, long id) {
//        Optional<Link> optionalLink = repository.findById(id);
//
//        if (optionalLink.isEmpty()) {
//            return DeleteLinkResponse.failed(DeleteLinkResponse.Error.invalidLinkId);
//        }
//
//        Link link = optionalLink.get();
//        boolean isNotUserLink = isNotUserLink(username, link);
//
//        if (isNotUserLink ) {
//            return DeleteLinkResponse.failed(DeleteLinkResponse.Error.insufficientPrivileges);
//        }
//
//        repository.delete(link);
//
//        return DeleteLinkResponse.success();
//    }

    private Optional<CreateLinkResponse.Error> validateCreateFields(CreateLinkRequest request) {
//        if (Objects.isNull(request.getShortLink()) || request.getShortLink().isEmpty()) {
//            return Optional.of(CreateLinkResponse.Error.invalidShortLink);
//        }

        System.out.println("request.getOriginalLink() = " + request.getOriginalLink());

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
//