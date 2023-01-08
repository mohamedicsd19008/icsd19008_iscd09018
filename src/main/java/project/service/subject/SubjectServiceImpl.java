package project.service.subject;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.dto.SubjectDTO;
import project.errorhandling.exception.IncorrectRoleException;
import project.errorhandling.exception.InvalidStatusException;
import project.errorhandling.exception.SubjectNotFoundException;
import project.mapper.SubjectMapper;
import project.persistence.entity.SubjectEntity;
import project.persistence.entity.UserEntity;
import project.persistence.repository.SubjectRepository;
import project.service.authorization.AuthService;
import project.service.enums.SubjectStatus;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    private final AuthService authService;

    @Autowired
    public SubjectServiceImpl(SubjectRepository subjectRepository, AuthService authService) {
        this.subjectRepository = subjectRepository;
        this.authService = authService;
    }

    @Override
    public List<SubjectDTO> findByTitleLike(String title, String username, String password) {
        UserEntity user = authService.login(username, password);

        List<SubjectEntity> subjectEntities = subjectRepository.findByTitleLike("%" + title + "%");

        boolean isVisitor = authService.isVisitor(user.getRole());

        //o episkepths mporei na dei mono egkekrimena sxolia
        if (isVisitor) {
            subjectEntities = subjectEntities.stream().filter(subjectEntity -> SubjectStatus.APPROVED.name().equals(subjectEntity.getStatus())).collect(
                    Collectors.toList());
            //o dhmosiografos mporei na dei egkekrimena kai ta dika tou
        } else if (authService.isJournalist(user.getRole())) {
            subjectEntities = subjectEntities.stream().filter(subjectEntity -> SubjectStatus.APPROVED.name().equals(subjectEntity.getStatus())
                    || username.equals(subjectEntity.getUsername())).collect(Collectors.toList());
        }

        //o epimelhths ola
        return SubjectMapper.toDTOList(subjectEntities);
    }

    @Override
    public List<SubjectDTO> findAll(String username, String password) {
        UserEntity user = authService.login(username, password);

        List<SubjectEntity> subjectEntities = subjectRepository.findAllByOrderByStatusDescTitleDesc();
        boolean isVisitor = authService.isVisitor(user.getRole());

        if (isVisitor) {
            subjectEntities = subjectEntities.stream().filter(subjectEntity -> SubjectStatus.APPROVED.name().equals(subjectEntity.getStatus())).collect(
                    Collectors.toList());
        } else if (authService.isJournalist(user.getRole())) {
            subjectEntities = subjectEntities.stream().filter(subjectEntity -> SubjectStatus.APPROVED.name().equals(subjectEntity.getStatus())
                    || username.equals(subjectEntity.getUsername())).collect(Collectors.toList());
        }
        return SubjectMapper.toDTOList(subjectEntities);
    }

    @Override
    public SubjectDTO findById(Long id, String username, String password) {
        UserEntity user = authService.login(username, password);

        if (authService.isVisitor(user.getRole())) {
            Optional<SubjectEntity> approvedSubject = subjectRepository.findByIdAndStatus(id, SubjectStatus.APPROVED.name());

            if (approvedSubject.isPresent()) {
                return SubjectMapper.toDto(approvedSubject.get());
            }
        } else if (authService.isJournalist(user.getRole())) {

            Optional<SubjectEntity> subjectEntityOptional = subjectRepository.findByIdAndStatusOrUsername(id, SubjectStatus.APPROVED.name(),username);

            if (subjectEntityOptional.isPresent()) {
                return SubjectMapper.toDto(subjectEntityOptional.get());
            }
        } else {
            Optional<SubjectEntity> subjectEntityOptional = subjectRepository.findById(id);

            if (subjectEntityOptional.isPresent()) {
                return SubjectMapper.toDto(subjectEntityOptional.get());
            }
        }

        throw new SubjectNotFoundException(id.toString());
    }

    @Override
    public SubjectDTO save(SubjectDTO subjectDTO, String username, String password) {

        UserEntity user = authService.login(username, password);
        if (authService.isSupervisor(user.getRole()) || authService.isJournalist(user.getRole())) {
            SubjectEntity subjectEntity = SubjectMapper.toEntity(subjectDTO);
            subjectEntity.setStatus(SubjectStatus.CREATED.name());
            subjectEntity.setUsername(username);
            return SubjectMapper.toDto(subjectRepository.save(subjectEntity));
        }

        throw new IncorrectRoleException(user.getRole());
    }

    @Override
    public SubjectDTO update(SubjectDTO subjectDTO, Long id, String username, String password) {
        UserEntity user = authService.login(username, password);


        if (authService.isSupervisor(user.getRole()) || authService.isJournalist(user.getRole())) {
            Optional<SubjectEntity> subjectOptional = subjectRepository.findById(id);

            if (subjectOptional.isPresent() && SubjectStatus.CREATED.name().equals(subjectOptional.get().getStatus())) {
             subjectOptional.get().setTitle(subjectDTO.getTitle());
             subjectOptional.get().setFatherSubject(subjectDTO.getFatherSubject());
                return SubjectMapper.toDto(subjectRepository.save(subjectOptional.get()));
            }

            throw new InvalidStatusException("Subjects", SubjectStatus.CREATED.name());
        }
        throw new IncorrectRoleException(user.getRole());
    }

    @Override
    public void approve(Long id, String username, String password) {
        UserEntity user = authService.login(username, password);

        if (authService.isSupervisor(user.getRole())) {
            SubjectDTO existingSubject = findById(id, username, password);

            if (SubjectStatus.CREATED.name().equals(existingSubject.getStatus())) {
                existingSubject.setStatus(SubjectStatus.APPROVED.name());

                SubjectEntity subjectEntity = SubjectMapper.toEntity(existingSubject);
                subjectEntity.setUsername(username);
                subjectRepository.save(subjectEntity);
                return;
            }
            throw new InvalidStatusException("Subjects", SubjectStatus.CREATED.name());
        }

        throw new IncorrectRoleException(user.getRole());
    }

    @Override
    public void reject(Long id, String username, String password) {
        UserEntity user = authService.login(username, password);

        if (authService.isSupervisor(user.getRole())) {
            SubjectDTO subject = findById(id, username, password);
            if (SubjectStatus.CREATED.name().equals(subject.getStatus())) {
                subjectRepository.deleteById(id);
                return;
            }
            throw new InvalidStatusException("Subjects", SubjectStatus.CREATED.name());
        }
        throw new IncorrectRoleException(user.getRole());
    }

}
