package source.restaurant_web_project.model.entity;


import source.restaurant_web_project.model.entity.enums.ReviewType;
import source.restaurant_web_project.model.entity.superClass.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "reviews")
public class Review extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private ReviewType type;
    @Column(columnDefinition = "text")
    private String review;
    @Column(columnDefinition = "text")
    private String comment;

    @ManyToOne(targetEntity = User.class)
    private User user;

    public Review(){}

    public ReviewType getType() {
        return type;
    }

    public void setType(ReviewType type) {
        this.type = type;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String message) {
        this.review = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
