package project.service.subject;

import java.util.List;
import project.dto.SubjectDTO;

public interface SubjectService {

    List<SubjectDTO> findByTitleLike(String title, String username, String password);

    List<SubjectDTO> findAll(String username, String password);

    SubjectDTO findById(Long id, String username, String password);

    SubjectDTO save(SubjectDTO subjectDTO,String username, String password);

    SubjectDTO update(SubjectDTO subjectDTO, Long id, String username, String password);

    void approve(Long id, String username, String password);

    void reject(Long id, String username, String password);
}
