package project.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.dto.CommentDTO;

@RequestMapping("/comments")
@Tag(name = "Comments")
public interface CommentsAPI {

    @PostMapping
    ResponseEntity<CommentDTO> post(@Valid @RequestBody CommentDTO commentDTO,  @RequestHeader(value = "userName" ,required = false) String userName,
            @RequestHeader(value = "password",required = false) String password);

    @PutMapping("/{id}")
    ResponseEntity<CommentDTO> update(@RequestParam String content, @PathVariable("id") Long id,  @RequestHeader(value = "userName" ,required = false) String userName,
            @RequestHeader(value = "password",required = false) String password);


    @PutMapping("/{id}/approval")
    ResponseEntity<CommentDTO> approve(@PathVariable("id") Long id,  @RequestHeader(value = "userName" ,required = false) String userName, @RequestHeader(value = "password",required = false) String password);

    @PutMapping("/{id}/rejection")
    ResponseEntity<Void> reject(@PathVariable("id") Long id,  @RequestHeader(value = "userName" ,required = false) String userName, @RequestHeader(value = "password",required = false) String password);

    @GetMapping("/{newsId}")
    ResponseEntity<List<CommentDTO>> findByNewsId(@PathVariable("newsId") Long newsId, @RequestHeader(value = "userName", required = false) String userName,
            @RequestHeader(value = "password", required = false) String password);
}

