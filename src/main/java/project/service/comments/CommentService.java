package project.service.comments;

import java.util.List;
import project.dto.CommentDTO;

public interface CommentService {

    CommentDTO post(CommentDTO commentDTO, String username, String password);

    CommentDTO update(String content, Long id, String username, String password);

    CommentDTO approve(Long id, String username, String password);

    void reject(Long id, String username,String password);

    List<CommentDTO> findByNewsId(Long newsId, String username, String password);
}
