package project.service.comments;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.dto.CommentDTO;
import project.errorhandling.exception.CommentNotFoundException;
import project.errorhandling.exception.IncorrectRoleException;
import project.errorhandling.exception.InvalidStatusException;
import project.errorhandling.exception.NewsNotFoundException;
import project.mapper.CommentMapper;
import project.persistence.entity.CommentEntity;
import project.persistence.entity.NewsEntity;
import project.persistence.entity.UserEntity;
import project.persistence.repository.CommentRepository;
import project.persistence.repository.NewsRepository;
import project.service.authorization.AuthService;
import project.service.enums.CommentStatus;
import project.service.enums.NewsStatus;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    private NewsRepository newsRepository;

    private AuthService authService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, NewsRepository newsRepository, AuthService authService) {
        this.commentRepository = commentRepository;
        this.newsRepository = newsRepository;
        this.authService = authService;
    }

    @Override
    public CommentDTO post(CommentDTO commentDTO, String username, String password) {

        UserEntity user = authService.login(username, password);
        boolean isVisitor = authService.isVisitor(user.getRole());


        //an einai tautopoihmenos xrhsths tote to onoma einai upoxrewtiko
        if (!isVisitor) {
            commentDTO.setSubmitterName(user.getFirstName() + " " + user.getLastName());
        }

        Optional<NewsEntity> newsEntityOptional = newsRepository.findById(commentDTO.getNewsId());

        //sxolia ginontai mono se dhmosievmenes eidseis
        if (newsEntityOptional.isPresent() && NewsStatus.PUBLISHED.getStatusWeight()== newsEntityOptional.get().getStatus()) {

            CommentEntity commentEntity = CommentMapper.toEntity(commentDTO);
            commentEntity.setStatus(CommentStatus.CREATED.name()); //arxikh katastash

            CommentEntity savedComment = commentRepository.save(commentEntity);

            //afou swsoume to sxolio prepei na enhmerwsoume kai tis eggrafes ths eidhshs gia thn opoia egine to sxolio
            newsEntityOptional.get().getComments().add(savedComment.getId());

            newsRepository.save(newsEntityOptional.get());

            return CommentMapper.toDTO(savedComment);
        } else {
            throw new NewsNotFoundException(commentDTO.getNewsId().toString());
        }
    }

    @Override
    public CommentDTO update(String content, Long id, String username, String password) {

        UserEntity user = authService.login(username, password);

        //mono epimelhths mporei na tropoipoihsei kapoio sxolio
        if(authService.isSupervisor(user.getRole())){
            Optional<CommentEntity> commentEntityOptional = commentRepository.findById(id);

            //tropopoihsh sxoliwn epitrepetai mono otan auta einai sthn arxikh tous katastash
            if (commentEntityOptional.isPresent()) {
                if(!CommentStatus.CREATED.name().equals(commentEntityOptional.get().getStatus())){
                    throw new InvalidStatusException("Comments", CommentStatus.CREATED.name());
                }
                commentEntityOptional.get().setContent(content);
                CommentEntity savedEntity = commentRepository.save(commentEntityOptional.get());
                return CommentMapper.toDTO(savedEntity);
            }
            throw new CommentNotFoundException(id.toString());
        }
       throw new IncorrectRoleException(user.getRole());
    }

    @Override
    public CommentDTO approve(Long id, String username, String password) {

        UserEntity user = authService.login(username, password);

        //mono epimelhths mporei na tropoipoihsei kapoio sxolio
        if(authService.isSupervisor(user.getRole())){

            Optional<CommentEntity> commentEntityOptional = commentRepository.findById(id);
            if (commentEntityOptional.isPresent()) {

                //ena sxolio mporei na metavei sthn katastash egkekrimeno mono otan einai sthn arxikh tou
                if(!CommentStatus.CREATED.name().equals(commentEntityOptional.get().getStatus())){
                    throw new InvalidStatusException("Comments", CommentStatus.CREATED.name());
                }
                commentEntityOptional.get().setStatus(CommentStatus.APPROVED.name());
                CommentEntity savedEntity = commentRepository.save(commentEntityOptional.get());
                return CommentMapper.toDTO(savedEntity);
            }
            throw new CommentNotFoundException(id.toString());
        }
        throw new IncorrectRoleException(user.getRole());
    }

    @Override
    public void reject(Long id, String username, String password) {
        UserEntity user = authService.login(username, password);

        //mono epimelhths mporei na tropoipoihsei kapoio sxolio
        if(authService.isSupervisor(user.getRole())){
            Optional<CommentEntity> commentEntityOptional = commentRepository.findById(id);
            //ena sxolio mporei na diagraftei mono otan einai sthn arxikh tou kastash
            if (commentEntityOptional.isPresent()) {
                if(!CommentStatus.CREATED.name().equals(commentEntityOptional.get().getStatus())){
                    throw new InvalidStatusException("Comments", CommentStatus.CREATED.name());
                }
                commentRepository.deleteById(id);
                return;
            }
            throw new CommentNotFoundException(id.toString());
        }
        throw new IncorrectRoleException(user.getRole());
    }

    @Override
    public List<CommentDTO> findByNewsId(Long newsId, String username, String password) {

        UserEntity user = authService.login(username, password);

        boolean isVisitor = authService.isVisitor(user.getRole());

        List<CommentEntity> commentEntities = commentRepository.findByNewsId(newsId);

        //episkeptes kai dhmosiografoi mporoun an doune mono egkekrimena sxolia
        if (isVisitor || authService.isJournalist(user.getRole())) {
            List<CommentEntity> approvedComments = commentEntities.stream()
                                                                  .filter(commentEntity -> CommentStatus.APPROVED.name().equals(commentEntity.getStatus()))
                                                                  .collect(Collectors.toList());
            return CommentMapper.toDTOList(approvedComments);

        } else {
            //o epimelhths ta vlepei ola
            return CommentMapper.toDTOList(commentEntities);
        }

    }
}
