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
import project.dto.NewsDTO;

@RequestMapping("/news")
@Tag(name = "News")
public interface NewsAPI {

    @PostMapping
    ResponseEntity<NewsDTO> create(@Valid @RequestBody NewsDTO newsDTO, @RequestHeader(value = "userName" ,required = false) String userName,
            @RequestHeader(value = "password",required = false) String password);

    @PutMapping("/{id}")
    ResponseEntity<NewsDTO> update(@PathVariable("id") Long id, @RequestBody NewsDTO newsDTO,  @RequestHeader(value = "userName" ,required = false) String userName,
            @RequestHeader(value = "password",required = false) String password);

    @PutMapping("/{id}/submission")
    ResponseEntity<NewsDTO> submit(@PathVariable("id") Long id,  @RequestHeader(value = "userName" ,required = false) String userName, @RequestHeader(value = "password",required = false) String password);

    @PutMapping("/{id}/approval")
    ResponseEntity<NewsDTO> approve(@PathVariable("id") Long id,  @RequestHeader(value = "userName" ,required = false) String userName, @RequestHeader(value = "password",required = false) String password);

    @PutMapping("/{id}/publication")
    ResponseEntity<NewsDTO> publish(@PathVariable("id") Long id,  @RequestHeader(value = "userName" ,required = false) String userName, @RequestHeader(value = "password",required = false) String password);

    @PutMapping("/{id}/rejection")
    ResponseEntity<NewsDTO> reject(@PathVariable("id") Long id, @RequestParam("rejectionReason") String rejectionReason,
             @RequestHeader(value = "userName" ,required = false) String userName, @RequestHeader(value = "password",required = false) String password);

    @GetMapping("/search")
    ResponseEntity<List<NewsDTO>> search(@RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content", required = false) String content,  @RequestHeader(value = "userName" ,required = false) String userName,
            @RequestHeader(value = "password",required = false) String password);

    @GetMapping("/{id}")
    ResponseEntity<NewsDTO> findById(@PathVariable("id") Long id,  @RequestHeader(value = "userName" ,required = false) String userName, @RequestHeader(value = "password",required = false) String password);

    @GetMapping
    ResponseEntity<List<NewsDTO>> findAll(@RequestParam(value = "status", required = false) String status, @RequestParam(value = "startDate", required = false) String startDate, @RequestParam(value = "endDate", required = false) String endDate,  @RequestHeader(value = "userName" ,required = false) String userName, @RequestHeader(value = "password",required = false) String password);

    @GetMapping("/subjects/{subjectId}")
    ResponseEntity<List<NewsDTO>> findBySubjectId(@PathVariable("subjectId")String subjectId, @RequestHeader(value = "userName" ,required = false) String userName, @RequestHeader(value = "password",required = false) String password);

}

