package project.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import project.api.SubjectAPI;
import project.dto.SubjectDTO;
import project.service.authorization.AuthService;
import project.service.subject.SubjectService;

@RestController
public class SubjectController implements SubjectAPI {

    private final SubjectService subjectService;

    @Autowired
    public SubjectController(SubjectService subjectService ) {
        this.subjectService = subjectService;
    }

    @Override
    public ResponseEntity<SubjectDTO> create(SubjectDTO subjectDTO, String userName, String password) {
        SubjectDTO createdSubject = subjectService.save(subjectDTO, userName, password);
        return new ResponseEntity<>(createdSubject, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<SubjectDTO> findById(Long id, String userName, String password) {
        return new ResponseEntity<>(subjectService.findById(id,userName,password), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<List<SubjectDTO>> findByTitleLike(String title, String userName, String password) {
        return new ResponseEntity<>(subjectService.findByTitleLike(title,userName,password), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<SubjectDTO>> findByAll(String userName, String password) {
        return new ResponseEntity<>(subjectService.findAll(userName,password), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SubjectDTO> update(SubjectDTO subjectDTO, Long id ,String userName, String password) {
        SubjectDTO createdSubject = subjectService.update(subjectDTO,id,userName,password);
        return new ResponseEntity<>(createdSubject, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> approve(Long id, String userName, String password) {
        subjectService.approve(id,userName,password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> reject(Long id, String userName, String password) {
        subjectService.reject(id,userName,password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
