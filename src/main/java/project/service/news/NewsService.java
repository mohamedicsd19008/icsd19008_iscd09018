package project.service.news;

import java.time.LocalDate;
import java.util.List;
import project.dto.NewsDTO;
import project.service.enums.NewsStatus;

public interface NewsService {

    NewsDTO create(NewsDTO newsDTO, String username, String password);

    NewsDTO update(Long id,NewsDTO newsDTO, String username, String password);

    boolean existsById(Long id);

    NewsDTO submit(Long id, String username, String password);

    NewsDTO approve(Long id, String username, String password);

    NewsDTO publish(Long id, String username, String password);

    NewsDTO reject(Long id, String rejectionReason, String username, String password);

    List<NewsDTO> search(String title, String content, String username,String password);

    NewsDTO findById(Long id, String username, String password);

    List<NewsDTO> findAll(String status, LocalDate startDate, LocalDate endDate, String username, String password);

    List<NewsDTO> findBySubjectId(String subjectId, String usernname, String password);
}
