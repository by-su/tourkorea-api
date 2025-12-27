package info.tourkorea.userservice.domain.email;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmailToken {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String link;

    @Builder
    public EmailToken(String email, String link) {
        this.email = email;
        this.link = link;
    }
}
