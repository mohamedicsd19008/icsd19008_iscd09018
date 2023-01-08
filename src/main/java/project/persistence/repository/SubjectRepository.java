package project.persistence.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.persistence.entity.SubjectEntity;

@Repository
public interface SubjectRepository extends CrudRepository<SubjectEntity, Long> {

    List<SubjectEntity> findByTitleLike(String title);

    List<SubjectEntity> findAllByOrderByStatusDescTitleDesc();

    Optional<SubjectEntity> findById(Long id);

    Optional<SubjectEntity> findByIdAndStatus(Long id, String status);

    boolean existsById(Long id);

    @Query("SELECT subject FROM SubjectEntity subject WHERE (subject.id = :id and subject.status = :status) or (subject.id = :id and subject.username = :username)")
    Optional<SubjectEntity> findByIdAndStatusOrUsername(@Param("id") Long id, @Param("status")String status, @Param("username") String username);


}
