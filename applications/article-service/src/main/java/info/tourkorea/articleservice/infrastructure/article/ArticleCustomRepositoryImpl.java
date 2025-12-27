package info.tourkorea.articleservice.infrastructure.article;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import info.tourkorea.articleservice.domain.article.hashtag.Hashtag;
import info.tourkorea.articleservice.domain.comment.QComment;
import info.tourkorea.articleservice.dto.article.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import request.UserContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.querydsl.core.types.ExpressionUtils.count;
import static com.querydsl.core.types.ExpressionUtils.list;
import static info.tourkorea.articleservice.domain.article.QArticle.article;
import static info.tourkorea.articleservice.domain.article.hashtag.QHashtag.hashtag;
import static info.tourkorea.articleservice.domain.article.like.QDislike.dislike;
import static info.tourkorea.articleservice.domain.article.like.QLiked.liked;
import static info.tourkorea.articleservice.domain.bookmark.QBookmark.bookmark;
import static info.tourkorea.articleservice.domain.comment.QComment.*;
import static info.tourkorea.articleservice.dto.article.ArticleRequest.ArticleSearchRequest;

@Repository
@RequiredArgsConstructor
public class ArticleCustomRepositoryImpl implements ArticleCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public ArticleDTO.ArticleDetailQueryDTO getArticleDetail(Long articleId, Long loginUserId) {
        BooleanExpression likedCondition = loginUserId != null ?
                JPAExpressions
                        .select(liked.id)
                        .from(liked)
                        .where(
                                liked.userId.eq(loginUserId)
                                        .and(liked.deleted.isFalse())
                                        .and(liked.article.id.eq(article.id))
                        ).exists() : Expressions.FALSE;

        BooleanExpression dislikedCondition = loginUserId != null ?
                JPAExpressions
                        .select(dislike.id)
                        .from(dislike)
                        .where(dislike.userId.eq(loginUserId)
                                .and(dislike.deleted.isFalse())
                                .and(dislike.article.id.eq(article.id))
                        ).exists() : Expressions.FALSE;

        BooleanExpression bookmarkedCondition = loginUserId != null ?
                JPAExpressions
                        .select(bookmark.id)
                        .from(bookmark)
                        .where(bookmark.userId.eq(loginUserId)
                                .and(bookmark.deleted.isFalse())
                                .and(bookmark.article.id.eq(article.id))
                        ).exists() : Expressions.FALSE;

        return queryFactory
                .select(
                        new QArticleDTO_ArticleDetailQueryDTO(
                                article.id,
                                article.title,
                                article.content,
                                article.likes.size(),
                                article.dislikes.size(),
                                likedCondition,
                                dislikedCondition,
                                bookmarkedCondition,
                                article.createdBy,
                                article.updatedBy,
                                article.createdAt,
                                article.updatedAt
                        )
                )
                .from(article)
                .where(
                        article.deleted.isFalse()
                                .and(article.id.eq(articleId))
                )
                .fetchOne();
    }

    @Override
    public Page<ArticleDTO.ArticleQueryDTO> getArticles(ArticleSearchRequest request, Pageable pageable) {

        UserContext user = request.getUserContext();

        Long userId = -1L;
        if (user != null) userId = user.getId();

        var totalQuery = queryFactory
                .select(article.id.count())
                .from(article);

        var query = queryFactory
                .select(
                        new QArticleDTO_ArticleQueryDTO(
                                article.id,
                                article.title,
                                article.content.substring(0, 500),
                                article.likes.size(),
                                article.dislikes.size(),
                                article.comments.size(),
                                JPAExpressions
                                        .select(liked.id)
                                        .from(liked)
                                        .where(
                                                liked.userId.eq(userId)
                                                        .and(liked.deleted.isFalse())
                                                        .and(liked.article.id.eq(article.id))
                                        ).exists(),
                                JPAExpressions
                                        .select(dislike.id)
                                        .from(dislike)
                                        .where(dislike.userId.eq(userId)
                                                .and(dislike.deleted.isFalse())
                                                .and(dislike.article.id.eq(article.id))
                                        ).exists(),
                                article.createdBy,
                                article.updatedBy,
                                article.createdAt,
                                article.updatedAt
                        )
                )
                .from(article);

        if (request.getHashtagFlag()) {
            query
                    .leftJoin(hashtag).on(hashtag.article.id.eq(article.id));
            totalQuery
                    .leftJoin(hashtag).on(hashtag.article.id.eq(article.id));
        }

        query
                .where(
                        searchArticlesWhereClause(request),
                        article.deleted.isFalse()
                )
                .orderBy(getArticleOrderSpecifiers(pageable).toArray(new OrderSpecifier[0]))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();


        Long total = totalQuery
                .where(
                        searchArticlesWhereClause(request)
                )
                .fetchFirst();

        return new PageImpl<>(query.fetch(), pageable, total);
    }

    @Override
    public Page<ArticleDTO.MyPageArticleQueryDTO> getMypageArticles(Long userId, String type, Pageable pageable) {

        var totalQuery = queryFactory
                .select(article.id.count())
                .from(article);


        var query = queryFactory
                .select(
                        new QArticleDTO_MyPageArticleQueryDTO(
                                article.id,
                                article.title,
                                article.content,
                                article.likes.size(),
                                article.dislikes.size(),
                                article.comments.size(),
                                JPAExpressions
                                        .select(liked.id)
                                        .from(liked)
                                        .where(
                                                liked.userId.eq(userId)
                                                        .and(liked.deleted.isFalse())
                                                        .and(liked.article.id.eq(article.id))
                                                        .and(liked.createdBy.eq(userId))
                                        ).exists(),
                                JPAExpressions
                                        .select(dislike.id)
                                        .from(dislike)
                                        .where(dislike.userId.eq(userId)
                                                .and(dislike.deleted.isFalse())
                                                .and(dislike.article.id.eq(article.id))
                                                .and(dislike.createdBy.eq(userId))
                                        ).exists(),
                                JPAExpressions
                                        .select(count(article.id))
                                        .from(article)
                                        .where(
                                                article.createdBy.eq(userId)
                                                        .and(article.deleted.isFalse())
                                                        .and(article.id.eq(article.id))
                                        ),
                                JPAExpressions
                                        .select(count(bookmark.id))
                                        .from(bookmark)
                                        .innerJoin(article).on(article.id.eq(bookmark.article.id))
                                        .where(
                                                bookmark.userId.eq(userId)
                                                        .and(bookmark.deleted.isFalse())
                                                        .and(article.deleted.isFalse())
                                        ),
                                article.createdBy,
                                article.updatedBy,
                                article.createdAt,
                                article.updatedAt
                        )
                )
                .from(article);

        if (type.equals("liked")) {
            query
                    .leftJoin(liked).on(
                            liked.article.id.eq(article.id)
                                    .and(liked.deleted.isFalse()
                                    .and(liked.userId.eq(userId))
                            ));
            totalQuery
                    .leftJoin(liked).on(
                            liked.article.id.eq(article.id)
                                    .and(liked.deleted.isFalse()
                                            .and(liked.userId.eq(userId))
                    ));
        }

        if (type.equals("saved")) {
            query
                    .innerJoin(bookmark).on(
                            bookmark.article.id.eq(article.id)
                                    .and(bookmark.deleted.isFalse())
                                    .and(bookmark.createdBy.eq(userId))
                    );
            totalQuery
                    .innerJoin(bookmark).on(
                            bookmark.article.id.eq(article.id)
                                    .and(bookmark.deleted.isFalse())
                                    .and(bookmark.createdBy.eq(userId))
                    );
        }

        if (type.equals("comments")) {
            query
                    .innerJoin(comment).on(
                            comment.article.id.eq(article.id)
                                    .and(comment.createdBy.eq(userId))
                                    .and(comment.deleted.isFalse())
                    );

            totalQuery
                    .innerJoin(comment).on(
                            comment.article.id.eq(article.id)
                                    .and(comment.createdBy.eq(userId))
                                    .and(comment.deleted.isFalse())
                    );
        }


        List<ArticleDTO.MyPageArticleQueryDTO> response = query
                .where(
                        myPageWhereClause(type, userId)
                )
                .orderBy(article.createdAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();


        Long total = totalQuery
                .where(
                        myPageWhereClause(type, userId)
                )
                .fetchFirst();

        return new PageImpl<>(response, pageable, total);
    }

    private BooleanBuilder nullSafeBuilder(Supplier<BooleanExpression> fun) {
        try {
            return new BooleanBuilder(fun.get());
        } catch (NullPointerException e) {
            return new BooleanBuilder();
        }
    }

    private List<OrderSpecifier<?>> getArticleOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        Sort sort = pageable.getSort();

        sort.forEach(
                order -> {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    PathBuilder<Object> pathBuilder = new PathBuilder<>(article.getType(), article.getMetadata());
                    orderSpecifiers.add(new OrderSpecifier(direction, pathBuilder.get(order.getProperty())));
                });

        return orderSpecifiers;
    }

    private BooleanBuilder searchArticlesWhereClause(ArticleSearchRequest request) {
        if (request.getHashtagFlag()) {
            return containsHashtag(request.getKeyword());
        } else {
            return containsTitle(request.getKeyword());
        }
    }

    private BooleanBuilder myPageWhereClause(String type, Long userId) {
        if (type.equals("post")) {
            return nullSafeBuilder(() -> article.createdBy.eq(userId));
        }

        if (type.equals("liked")) {
            return nullSafeBuilder(() -> liked.userId.eq(userId));
        }

        if (type.equals("bookmark")) {
            return nullSafeBuilder(() ->bookmark.createdBy.eq(userId));
        }

        return null;
    }

    private BooleanBuilder containsTitle(String keyword) {
        return nullSafeBuilder(() -> article.title.contains(keyword));
    }

    private BooleanBuilder containsHashtag(String keyword) {
        return nullSafeBuilder(() -> hashtag.tag.contains(keyword));
    }

    @Override
    public List<Hashtag> getHashtags(Long articleId) {
        return queryFactory
                .selectFrom(hashtag)
                .where(
                        hashtag.article.id.eq(articleId)
                                .and(hashtag.deleted.isFalse())
                )
                .fetch();
    }

    @Override
    public ArticleResponse.UserArticleCountResponse getUserArticleCounts(Long userId) {
        Long articleCount = queryFactory
                .select(
                        count(article.id)
                )
                .from(article)
                .where(
                        article.deleted.isFalse()
                                .and(article.createdBy.eq(userId))
                )
                .fetchOne();

        Long bookmarkCount = queryFactory
                .select(
                        count(bookmark.id)
                )
                .from(bookmark)
                .where(
                        bookmark.deleted.isFalse()
                                .and(bookmark.userId.eq(userId))
                )
                .fetchOne();


        return new ArticleResponse.UserArticleCountResponse(articleCount, bookmarkCount);
    }
}