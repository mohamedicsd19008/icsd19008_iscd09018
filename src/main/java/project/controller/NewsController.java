package project.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import project.api.NewsAPI;
import project.dto.NewsDTO;
import project.persistence.entity.UserEntity;
import project.service.authorization.AuthService;
import project.service.news.NewsService;

@RestController
public class NewsController implements NewsAPI {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }


    @Override
    public ResponseEntity<NewsDTO> create(NewsDTO newsDTO, String userName, String password) {

        NewsDTO createdNews = newsService.create(newsDTO,userName, password);
        return new ResponseEntity<>(createdNews, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<NewsDTO> update(Long id,NewsDTO newsDTO, String userName, String password) {

        NewsDTO createdNews = newsService.update(id,newsDTO,userName,password);
        return new ResponseEntity<>(createdNews, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<NewsDTO> submit(Long id, String userName, String password) {

        NewsDTO createdNews = newsService.submit(id, userName,password);
        return new ResponseEntity<>(createdNews, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<NewsDTO> approve(Long id, String userName, String password) {

        NewsDTO createdNews = newsService.approve(id,userName,password);
        return new ResponseEntity<>(createdNews, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<NewsDTO> reject(Long id,String rejectionReason, String userName, String password) {

        NewsDTO createdNews = newsService.reject(id, rejectionReason,userName,password);
        return new ResponseEntity<>(createdNews, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<NewsDTO> publish(Long id, String userName, String password) {

        NewsDTO createdNews = newsService.publish(id,userName,password);
        return new ResponseEntity<>(createdNews, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<NewsDTO>> search(String title, String content, String userName, String password) {

        if(title != null){
            title = "%"+title+"%";
        }

        if(content != null){
            content = "%"+content+"%";
        }
        List<NewsDTO> createdNews = newsService.search(title,content,userName, password);
        return new ResponseEntity<>(createdNews, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<NewsDTO> findById(Long id, String userName, String password) {
        return new ResponseEntity<>(newsService.findById(id,userName, password), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<NewsDTO>> findAll(String status, String startDate,String endDate, String userName, String password) {
        if(startDate!= null && endDate!=null){
            return new ResponseEntity<>(newsService.findAll(status, LocalDate.parse(startDate), LocalDate.parse(endDate), userName, password), HttpStatus.OK);
        }

        return new ResponseEntity<>(newsService.findAll(status, null, null, userName, password), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<List<NewsDTO>> findBySubjectId(String subjectId, String username, String password) {
        return new ResponseEntity<>(newsService.findBySubjectId(subjectId, username, password), HttpStatus.OK);
    }
}
