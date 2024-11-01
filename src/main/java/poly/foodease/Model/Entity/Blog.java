package poly.foodease.Model.Entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "blog")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_id")
    private Integer blogId;

    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "nvarchar(MAX)")
    private String content;

    @Column(name = "imageURL")
    private String imageURL;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "blog_category_id")
    private BlogCategory blogCategory;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "blog_author_id")
    private BlogAuthor blogAuthor;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "blog_hashtags",
            joinColumns = @JoinColumn(name="blog_id"),
            inverseJoinColumns = @JoinColumn(name="hashtag_id")
    )
    private List<Hashtag> hashtags;
}
