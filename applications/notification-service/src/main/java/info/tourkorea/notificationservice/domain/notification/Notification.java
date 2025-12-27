package info.tourkorea.notificationservice.domain.notification;

import info.tourkorea.notificationservice.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@ToString
@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_USER_ID_SENDER_ID_CONTENT_ID_TYPE",
                        columnNames = {"user_id", "sender_id", "content_id", "type"}
                )
        }
)
public class Notification extends BaseEntity {
    // TODO : 댓글, 답글, 좋아요 시 알림 생성 로직 추가 필요
    // 누가 어디서 (글, 댓글) 무엇을 (답글, 좋아요) 받았는지 알려주는 알림
    // ~님이 회원님의 게시물을 좋아합니다.
    // ~님이 회원님의 댓글을 좋아합니다.
    // ~님이 회원님의 게시물에 댓글을 남겼습니다. - 내용 (댓글 내용)
    // ~님이 회원님의 댓글에 답글을 남겼습니다. - 내용 (답글 내용)

    // ex)
    // sendername 님이 회원님의 게시물을 좋아합니다.
    // sendername 님이 회원님의 댓글에 답글을 남겼습니다. : comment

    // TODO : 프론트에서? 알림 클릭 시 type으로 먼저 구분 후, contentId로 찾기?
    // (LIKE = 게시물로 이동, COMMENT or REPLY = 댓글로 이동)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 알림 받는 사람 ID
    @Column(nullable = false)
    private Long userId;
    // 알림 보내는 사람 ID
    @Column(nullable = false)
    private Long senderId;

    // 컨텐츠 ID로 (게시물 ID, 댓글 ID) 컬럼 하나로 처리.
    // 알림 대상 글 or 댓글의 ID
    @Column(nullable = false)
    private Long contentId;

    // 알림 받는 사람 닉네임
    @Column(nullable = false)
    private String username;

    // 알림 보내는 사람 닉네임
    @Column(nullable = false)
    private String sendername;

    // 알림의 타입 (게시물 좋아요, 댓글 좋아요, 게시물 댓글, 댓글 답글)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column
    private String comment;

    // 읽었는지 여부
    @Column
    private boolean readYn;

    public enum NotificationType {
        LIKE_ON_POST("게시물 좋아요"),
        LIKE_ON_COMMENT("댓글 좋아요"),
        COMMENT_ON_POST("게시물 댓글"),
        REPLY_ON_COMMENT("댓글 답글");

        private final String info;

        NotificationType(String info) {
            this.info = info;
        }
    }

    public void setReadYnTrue() {
        this.readYn = true;
    }
}
