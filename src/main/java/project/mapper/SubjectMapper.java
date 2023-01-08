package project.mapper;

import java.util.ArrayList;
import java.util.List;
import project.dto.SubjectDTO;
import project.persistence.entity.SubjectEntity;

public class SubjectMapper {

    public static SubjectDTO toDto(SubjectEntity subjectEntity) {
        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.setTitle(subjectEntity.getTitle());
        subjectDTO.setFatherSubject(subjectEntity.getFatherSubject());
        subjectDTO.setChildSubjects(subjectEntity.getChildSubjects());
        subjectDTO.setCreatedDateTime(subjectEntity.getCreatedDateTime());
        subjectDTO.setId(subjectEntity.getId());
        subjectDTO.setStatus(subjectEntity.getStatus());

        return subjectDTO;
    }

    public static SubjectEntity toEntity(SubjectDTO subjectDTO) {
        SubjectEntity subjectEntity = new SubjectEntity();
        subjectEntity.setTitle(subjectDTO.getTitle());
        subjectEntity.setFatherSubject(subjectDTO.getFatherSubject());
        subjectEntity.setChildSubjects(subjectDTO.getChildSubjects());
        subjectEntity.setCreatedDateTime(subjectDTO.getCreatedDateTime());
        subjectEntity.setId(subjectDTO.getId());
        subjectEntity.setStatus(subjectDTO.getStatus());

        return subjectEntity;
    }

    public static List<SubjectEntity> toEntityList(List<SubjectDTO> subjectDTOS) {
        List<SubjectEntity> subjectEntities = new ArrayList<>();

        subjectDTOS.forEach(subjectDTO -> subjectEntities.add(toEntity(subjectDTO)));

        return subjectEntities;
    }

    public static List<SubjectDTO> toDTOList(List<SubjectEntity> subjectEntities) {
        List<SubjectDTO> subjectDTOS = new ArrayList<>();
        subjectEntities.forEach(subjectEntity -> subjectDTOS.add(toDto(subjectEntity)));

        return subjectDTOS;
    }
}
