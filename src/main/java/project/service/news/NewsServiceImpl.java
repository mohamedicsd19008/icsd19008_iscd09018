package project.service.news;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.dto.NewsDTO;
import project.errorhandling.exception.IncorrectRoleException;
import project.errorhandling.exception.InvalidStatusException;
import project.errorhandling.exception.NewsAlreadyExistsException;
import project.errorhandling.exception.NewsNotFoundException;
import project.errorhandling.exception.NoRejectionReasonException;
import project.errorhandling.exception.SubjectNotFoundException;
import project.mapper.NewsMapper;
import project.persistence.entity.NewsEntity;
import project.persistence.entity.UserEntity;
import project.persistence.repository.NewsRepository;
import project.persistence.repository.SubjectRepository;
import project.service.authorization.AuthService;
import project.service.enums.NewsStatus;

@Service
public class NewsServiceImpl implements NewsService {

    private NewsRepository newsRepository;

    private SubjectRepository subjectRepository;

    private AuthService authService;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository, SubjectRepository subjectRepository, AuthService authService) {
        this.newsRepository = newsRepository;
        this.subjectRepository = subjectRepository;
        this.authService = authService;
    }

    @Override
    public NewsDTO create(NewsDTO newsDTO, String username, String password) {

        UserEntity user = authService.login(username, password);

        //mono dimosiografos h epimeliths mporei na dimiourgisei eidhsh
        if (!authService.isVisitor(user.getRole())) {
            validate(newsDTO);

            NewsEntity newsEntity = NewsMapper.toEntity(newsDTO);
            newsEntity.setUsername(user.getUsername());
            newsEntity.setStatus(NewsStatus.CREATED.getStatusWeight());

            return NewsMapper.toDTO(newsRepository.save(newsEntity));
        } else {
            throw new IncorrectRoleException(user.getRole());
        }
    }

    @Override
    public NewsDTO update(Long id, NewsDTO newsDTO, String username, String password) {

        UserEntity user = authService.login(username, password);

        //mono dimosiografos h epimeliths mporei na dimiourgisei eidhsh
        if (!authService.isVisitor(user.getRole())) {

            if (!existsById(id)) {
                throw new NewsNotFoundException(id.toString());
            }

            Optional<NewsEntity> savedNews = newsRepository.findById(id);

            //mporoume na tropopoihsoume mono titlo, periexomeno, themata. ara vriskoume thn hdh swzmenh eidhsh
            //kai tis allazoume ta antistoixa pedia me ta kainourgia elegxontas panta ta parakatw

            //mono se katastash dhmiourghmenh mporei na tropopoihthei
            if (NewsStatus.CREATED.getStatusWeight() == savedNews.get().getStatus()) {
                validate(newsDTO);

                //o titlos prepei na nai monadikos akomh kai meta thn tropopoihsh
                if (newsDTO.getTitle() != null) {
                    if (newsRepository.existsByTitle(newsDTO.getTitle())) {
                        throw new NewsAlreadyExistsException(newsDTO.getTitle());
                    }

                    savedNews.get().setTitle(newsDTO.getTitle());
                }

                if (newsDTO.getContent() != null) {
                    savedNews.get().setContent(newsDTO.getContent());
                }
                //an exei anafores se themata tote auta prepei na uparxoun
                if (newsDTO.getSubjects() != null && !newsDTO.getSubjects().isEmpty()) {
                    newsDTO.getSubjects().forEach(subjectId -> {
                        if (!subjectRepository.existsById(Long.parseLong(subjectId))) {
                            throw new SubjectNotFoundException(subjectId);
                        }
                    });

                    savedNews.get().setSubjects(newsDTO.getSubjects());
                }

                return NewsMapper.toDTO(newsRepository.save(savedNews.get()));
            }

            //den epitrepetai h tropopoihsh se dhmosievmenes eidhseis
            throw new InvalidStatusException("News", NewsStatus.PUBLISHED.name());

        } else {
            //mono dimosiografos h epimeliths mporei na dimiourgisei eidhsh
            throw new IncorrectRoleException(user.getRole());
        }

    }

    @Override
    public boolean existsById(Long id) {
        return newsRepository.existsById(id);
    }

    @Override
    public NewsDTO submit(Long id, String username, String password) {
        UserEntity user = authService.login(username, password);

        //mono dimosiografos h epimeliths mporei na upovalei eidhsh
        if (!authService.isVisitor(user.getRole())) {
            Optional<NewsEntity> savedNews = newsRepository.findById(id);

            //mia eidhsh metavainei se katastash upovolhs mono an einai se arxikh katastash
            if(savedNews.isPresent() && NewsStatus.CREATED.getStatusWeight() != savedNews.get().getStatus()){
                throw new InvalidStatusException("News", NewsStatus.CREATED.name());
            }

            if (savedNews.isPresent()) {
                savedNews.get().setStatus(NewsStatus.SUBMITTED.getStatusWeight());
                return NewsMapper.toDTO(newsRepository.save(savedNews.get()));
            }

            throw new NewsNotFoundException(id.toString());
        } else {
            throw new IncorrectRoleException(user.getRole());
        }

    }

    @Override
    public NewsDTO approve(Long id, String username, String password) {

        //mono  epimeliths mporei na upovalei eidhsh
        UserEntity user = authService.login(username, password);

        if(authService.isSupervisor(user.getRole())){
            Optional<NewsEntity> savedNews = newsRepository.findById(id);

            if(savedNews.isPresent() && NewsStatus.SUBMITTED.getStatusWeight() != savedNews.get().getStatus()){
                throw new InvalidStatusException("News", NewsStatus.SUBMITTED.name());
            }

            if (savedNews.isPresent()) {
                savedNews.get().setStatus(NewsStatus.APPROVED.getStatusWeight());
                savedNews.get().setRejectionReason(null);
                return NewsMapper.toDTO(newsRepository.save(savedNews.get()));
            }

            throw new NewsNotFoundException(id.toString());
        }

        throw new IncorrectRoleException(user.getRole());
    }

    @Override
    public NewsDTO reject(Long id, String rejectionReason, String username,String password) {
        //mono  epimeliths mporei na upovalei eidhsh

        UserEntity user = authService.login(username, password);
        if(authService.isSupervisor(user.getRole())){
            Optional<NewsEntity> savedNews = newsRepository.findById(id);

            // o logos apporipshs einai upoxrewtikos
            if (rejectionReason == null) {
                throw new NoRejectionReasonException();
            }

            if(savedNews.isPresent() && NewsStatus.SUBMITTED.getStatusWeight() != savedNews.get().getStatus()){
                throw new InvalidStatusException("News", NewsStatus.SUBMITTED.name());
            }

            //apo th stigmh pou aporiptetai h eidhsh epistrefei sthn arxikh ths katastash
            if (savedNews.isPresent()) {
                savedNews.get().setStatus(NewsStatus.CREATED.getStatusWeight());
                savedNews.get().setRejectionReason(rejectionReason);
                return NewsMapper.toDTO(newsRepository.save(savedNews.get()));
            }

            throw new NewsNotFoundException(id.toString());
        }
        throw new IncorrectRoleException(user.getRole());
    }

    @Override
    public List<NewsDTO> search(String title, String content, String username, String password) {
        UserEntity user = authService.login(username, password);

        if (authService.isVisitor(user.getRole())) {
            // oi episkeptes mporoune na doune mono dhmosievmnenes eidhseis
            List<NewsEntity> news = newsRepository.findByTitleAndContentLike(title, content, NewsStatus.PUBLISHED.getStatusWeight());
            return NewsMapper.toDTOList(news);
        } else {

            List<NewsEntity> news = newsRepository.findByTitleAndContentLike(title, content, 0);

            if (authService.isJournalist(user.getRole())) {
                // oi dhmosiografoi mporoun na doune mono dimosievmenes eidhshs h eidhseis pou tous anhkoun ara tis filtraroume
                news = news.stream().filter(newsEntity -> NewsStatus.PUBLISHED.getStatusWeight() == newsEntity.getStatus()
                        || username.equals(newsEntity.getUsername())).collect(Collectors.toList());
            }

            //alliws an eiani epimeliths tis gurname oles
            return NewsMapper.toDTOList(news);
        }


    }

    @Override
    public NewsDTO findById(Long id, String username, String password) {

        UserEntity user = authService.login(username, password);

        if (authService.isVisitor(user.getRole())) {
            //oi episkeptes mporoune na doune mono dhmosievmenes eidhseis
            Optional<NewsEntity> publishedNews = newsRepository.findByIdAndStatus(id, NewsStatus.PUBLISHED.getStatusWeight());
            if (publishedNews.isPresent()) {
                return NewsMapper.toDTO(publishedNews.get());
            }
        } else {

            if (authService.isJournalist(user.getRole())) {
                //oi dhmosiografoi mporoune na doune mono dhmosievmenes eidhseis h eidhseis pou tous anoikoun
                Optional<NewsEntity> newsOptional = newsRepository.findByIdAndStatusOrUsername(id, NewsStatus.PUBLISHED.getStatusWeight(), username);
                if (newsOptional.isPresent()) {
                    return NewsMapper.toDTO(newsOptional.get());
                }
            } else {
                //oi epimelites mporoune na doune oles tis eidhshsis
                Optional<NewsEntity> newsOptional = newsRepository.findById(id);

                if (newsOptional.isPresent()) {
                    return NewsMapper.toDTO(newsOptional.get());
                }
            }
        }

        throw new NewsNotFoundException(id.toString());

    }

    @Override
    public List<NewsDTO> findBySubjectId(String subjectId, String username, String password) {

        UserEntity user = authService.login(username, password);

        if (authService.isVisitor(user.getRole())) { // oi episkeptes mporoune na doune mono dimosievmenes eidhseis
            List<NewsEntity> publishedNews = newsRepository.findBySubjectIdAndStatus(subjectId, NewsStatus.PUBLISHED.getStatusWeight());
            return NewsMapper.toDTOList(publishedNews);
        }

        List<NewsEntity> news = newsRepository.findBySubjectId(subjectId);

        if (authService.isJournalist(user.getRole())) { //oi dhmosiaografoi mporoune na doune mono dhmosievmenes eidhseis kai autes pou tous anhkoun
            news = news.stream().filter(newsEntity -> NewsStatus.PUBLISHED.getStatusWeight() == newsEntity.getStatus()
                    || username.equals(newsEntity.getUsername())).collect(Collectors.toList());
        }

        //oi epimelites mporoune na doune oles tis eidhseis
        return NewsMapper.toDTOList(news);

    }

    @Override
    public List<NewsDTO> findAll(String status, LocalDate startDate, LocalDate endDate, String username, String password) {
        List<NewsEntity> news =
                newsRepository.findAllByOrderByStatusDescPublicationDateTimeDescCreatedDateTimeDesc();

        UserEntity user = authService.login(username, password);

        if (startDate != null && endDate != null) {
            //an  exei dwsei ws eisodo tis 2 hmeromhnies tote psaxnoume eidhshs me hmeromhnia dhmosieushs h dhmiourgias (an h prwth den uparxei(
            //anamesa se autes

            return findNewsBetweenDatesAndStatus(status, startDate, endDate, username, news, user);
        }


        return findWithStatus(status, username, news, user);
    }



    @Override
    public NewsDTO publish(Long id, String username, String password) {

        UserEntity user = authService.login(username, password);

        //mono epimelites mporoune na dhmosieusoun eidhsh
        if(authService.isSupervisor(user.getRole())){
            Optional<NewsEntity> savedNews = newsRepository.findById(id);

            if (savedNews.isPresent()) {
                if(NewsStatus.APPROVED.getStatusWeight() != savedNews.get().getStatus()){
                    throw new InvalidStatusException("News", NewsStatus.APPROVED.name());
                }
                savedNews.get().setStatus(NewsStatus.PUBLISHED.getStatusWeight());
                savedNews.get().setPublicationDateTime(LocalDateTime.now());
                return NewsMapper.toDTO(newsRepository.save(savedNews.get()));
            }

            throw new NewsNotFoundException(id.toString());
        }
        throw new IncorrectRoleException(user.getRole());
    }



    private void validate(NewsDTO newsDTO) {
        if (newsRepository.existsByTitle(newsDTO.getTitle())) {
            throw new NewsAlreadyExistsException(newsDTO.getTitle());
        }

        newsDTO.getSubjects().forEach(subjectId -> {
            if (!subjectRepository.existsById(Long.parseLong(subjectId))) {
                throw new SubjectNotFoundException(subjectId);
            }
        });
    }

    private List<NewsDTO> findWithStatus(String status, String username, List<NewsEntity> news, UserEntity user) {
        //Vriskoume oles tis eidhshs me sugkekrimeno status h an den exoume dwsei status tis gurname oles
        List<NewsEntity> noDateWithStatus = news.stream()
                                                .filter(newsEntity -> status == null
                                                        || NewsStatus.valueOf(status).getStatusWeight() == newsEntity.getStatus())
                                                .collect(Collectors.toList());
        if (authService.isVisitor(user.getRole())) {
            //oi episkeptes mporoune na doune mono tis dhmosievemenes ara filtraroume mono autes
            List<NewsEntity> published = news.stream()
                                             .filter(newsEntity -> NewsStatus.PUBLISHED.getStatusWeight() == newsEntity.getStatus())
                                             .collect(Collectors.toList());

            return NewsMapper.toDTOList(published);
        } else if(authService.isJournalist(user.getRole())) {
            //oi dhmosiografoi mporoune na doune dhmosievmenes h autes pou tous anoikoun
            List<NewsEntity> publishedOrOwnedNews = noDateWithStatus.stream()
                                                                    .filter(newsEntity ->
                                                                            newsEntity.getStatus() == NewsStatus.PUBLISHED.getStatusWeight()
                                                                                    || username.equals(newsEntity.getUsername()))
                                                                    .collect(Collectors.toList());
            return NewsMapper.toDTOList(publishedOrOwnedNews);
        }else{
            //oi epimelites oles
            return NewsMapper.toDTOList(noDateWithStatus);
        }
    }

    private List<NewsDTO> findNewsBetweenDatesAndStatus(String status, LocalDate startDate, LocalDate endDate, String username, List<NewsEntity> news, UserEntity user) {
        List<NewsEntity> betweenDates = news.stream().filter(newsEntity -> {
            LocalDateTime publicationDateTime = newsEntity.getPublicationDateTime();
            LocalDateTime createdDateTime = newsEntity.getCreatedDateTime();

            //an exoume hmeromhnia dhmosieushs elgexoume prwta vash auths an einai anamesa stis dothises hmeromhnies alliws
            //meta elegxoume thn hmeromhnia dhmiourgias
            //  startDate <= publicationDateTime or createdDateTime  <= endDate
            if (publicationDateTime != null) {
                return (publicationDateTime.toLocalDate().isEqual(startDate) || publicationDateTime.toLocalDate().isAfter(startDate))
                        && (publicationDateTime.toLocalDate().isEqual(endDate) || publicationDateTime.toLocalDate().isBefore(endDate));
            } else {
                return (createdDateTime.toLocalDate().isEqual(startDate) || createdDateTime.toLocalDate().isAfter(startDate))
                        && (createdDateTime.toLocalDate().isEqual(endDate) || createdDateTime.toLocalDate().isBefore(endDate));
            }

        }).collect(Collectors.toList());

        //epeita filtraroume vash status (an auto exei dwthei)
        List<NewsEntity> betweenDatesWithStatus = betweenDates.stream()
                                                              .filter(newsEntity -> status == null
                                                                      || NewsStatus.valueOf(status).getStatusWeight() == newsEntity.getStatus())
                                                              .collect(
                                                                      Collectors.toList());

        //telos elegxoume to rolo kai filtraroume antistoixa

        if (authService.isVisitor(user.getRole())) {
            //oi episkpetes vlepoune mono tis dhmosievmenes eidhsheis
            List<NewsEntity> publishedNews = betweenDatesWithStatus.stream()
                                                                   .filter(newsEntity -> newsEntity.getStatus() == NewsStatus.PUBLISHED.getStatusWeight())
                                                                   .collect(Collectors.toList());
            return NewsMapper.toDTOList(publishedNews);
        } else if (authService.isJournalist(user.getRole())) {
            //oi dhmosiografoi vlepoune mono tis dhmosievmenes eidhsheis kai autes pou tous anhkoun
            List<NewsEntity> publishedOrOwnedNews = betweenDatesWithStatus.stream()
                                                                          .filter(newsEntity ->
                                                                                  newsEntity.getStatus() == NewsStatus.PUBLISHED.getStatusWeight()
                                                                                          || username.equals(newsEntity.getUsername()))
                                                                          .collect(Collectors.toList());
            return NewsMapper.toDTOList(publishedOrOwnedNews);
        } else {
            //oi epimelites vlepoune oles tis eidhshseis
            return NewsMapper.toDTOList(betweenDatesWithStatus);
        }
    }
}
