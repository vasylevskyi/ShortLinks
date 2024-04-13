package ua.goit.shortlinks.notes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ua.goit.shortlinks.users.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "links")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Link {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @Column(name = "short_link", nullable = false)
    private String shortLink;

    @Column(name = "original_link", nullable = false)
    private String originalLink;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "valid_to", nullable = false)
    @Builder.Default
    private LocalDateTime validTo = LocalDateTime.now().plusDays(3);

    @Column(name = "counter", nullable = false)
    @Builder.Default
    private int counter = 0;
}
