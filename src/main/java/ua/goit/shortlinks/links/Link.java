package ua.goit.shortlinks.links;

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
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @JsonIgnore
    private User user;

    @Column(name = "short_link", nullable = false,unique = true)
    private String shortLink;

    @Column(name = "original_link", nullable = false)
    private String originalLink;

    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @Column(name = "valid_to", nullable = false)
    private LocalDateTime validTo = LocalDateTime.now().plusDays(20);

    @Builder.Default
    @Column(name = "counter", nullable = false)
    private int counter = 0;
    ////////////////////////////////////
    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @PreUpdate
    protected void onUpdate() {
        if (LocalDateTime.now().isAfter(validTo)) {
            this.isDeleted = true;
        }
    }
}
