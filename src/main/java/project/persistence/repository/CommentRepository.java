package project.persistence.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.persistence.entity.CommentEntity;

@Repository
public interface CommentRepository extends CrudRepository<CommentEntity, Long> {

    List<CommentEntity> findByNewsId(Long newsId);

}
