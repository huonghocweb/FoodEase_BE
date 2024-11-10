package poly.foodease.Model.Entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "blog_comment")
public class BlogComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer commentId;

    @Column(name = "comment_content", columnDefinition = "nvarchar(MAX)")
    private String commentContent;

    @Column(name = "imageURL")
    private String imageURL;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "blog_id")
    private Blog blog;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
