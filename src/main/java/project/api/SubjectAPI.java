package project.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dto.SubjectDTO;

@RequestMapping("/subjects")
@Tag(name = "Subjects")
public interface SubjectAPI {

    @PostMapping
    ResponseEntity<SubjectDTO> create( @RequestBody SubjectDTO subjectDTO,  @RequestHeader(value = "userName" ,required = false) String userName,
            @RequestHeader(value = "password",required = false) String password);

    @GetMapping("/{id}")
    ResponseEntity<SubjectDTO> findById(@PathVariable("id") Long id, @RequestHeader(value = "userName", required = false) String userName,
            @RequestHeader(value = "password",required = false) String password);

    @GetMapping("search/{title}")
    ResponseEntity<List<SubjectDTO>> findByTitleLike(@PathVariable("title") String title, @RequestHeader(value = "userName",required = false) String userName,
            @RequestHeader(value = "password", required = false) String password);

    @GetMapping
    ResponseEntity<List<SubjectDTO>> findByAll( @RequestHeader(value = "userName" ,required = false) String userName,
            @RequestHeader(value = "password",required = false) String password);

    @PutMapping("/{id}")
    ResponseEntity<SubjectDTO> update( @RequestBody SubjectDTO subjectDTO, @PathVariable("id") Long id, @RequestHeader(value = "userName" ,required = false) String userName,
            @RequestHeader(value = "password",required = false) String password);


    @PutMapping("/{id}/approval")
    ResponseEntity<Void> approve(@PathVariable("id") Long id, @RequestHeader(value = "userName" ,required = false) String userName,
            @RequestHeader(value = "password",required = false) String password);

    @PutMapping("/{id}/rejection")
    ResponseEntity<Void> reject(@PathVariable("id") Long id, @RequestHeader(value = "userName" ,required = false) String userName,
            @RequestHeader(value = "password",required = false) String password);
}

