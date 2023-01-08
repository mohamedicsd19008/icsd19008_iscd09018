package project.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import project.api.CommentsAPI;
import project.dto.CommentDTO;
import project.service.comments.CommentService;

@RestController
public class CommentsController implements CommentsAPI {

    private final CommentService commentService;

    @Autowired
    public CommentsController(CommentService commentService) {
        this.commentService = commentService;
    }



    @Override
    public ResponseEntity<CommentDTO> post(CommentDTO commentDTO, String userName, String password) {
        return new ResponseEntity<>(commentService.post(commentDTO,userName,password), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CommentDTO> update(String content, Long id, String userName, String password) {

        return new ResponseEntity<>(commentService.update(content, id,userName,password), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CommentDTO> approve(Long id, String userName, String password) {

        return new ResponseEntity<>(commentService.approve(id,userName,password), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> reject(Long id, String userName, String password) {
        commentService.reject(id,userName,password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<CommentDTO>> findByNewsId(Long newsId, String userName, String password){
        return new ResponseEntity<>(commentService.findByNewsId(newsId, userName, password),HttpStatus.OK);
    }
}
