package project.persistence.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.persistence.entity.NewsEntity;
import project.persistence.entity.SubjectEntity;

@Repository
public interface NewsRepository extends CrudRepository<NewsEntity, Long> {

    boolean existsByTitle(String title);

    @Query("SELECT news FROM NewsEntity news WHERE (:title is null or news.title like :title) and (:content is null"
            + " or news.content like :content) and (:status =0 or news.status = :status)")
    List<NewsEntity> findByTitleAndContentLike(@Param("title") String title,@Param("content") String content, @Param("status") int status);

    List<NewsEntity> findAllByOrderByStatusDescPublicationDateTimeDescCreatedDateTimeDesc();

    @Query("SELECT news FROM NewsEntity news WHERE :subjectId member news.subjects")
    List<NewsEntity>findBySubjectId(@Param("subjectId") String subjectId);

    @Query("SELECT news FROM NewsEntity news WHERE :subjectId member news.subjects and news.status = :status")
    List<NewsEntity>findBySubjectIdAndStatus(@Param("subjectId") String subjectId, @Param("status") int status);

    Optional<NewsEntity> findByIdAndStatus(Long id, int status);

    @Query("SELECT news FROM NewsEntity news WHERE (news.id = :id and news.status = :status) or (news.id = :id and news.username = :username)")
    Optional<NewsEntity> findByIdAndStatusOrUsername(Long id, int status, String username);

}
