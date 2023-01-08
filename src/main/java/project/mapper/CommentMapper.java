package project.mapper;

import java.util.ArrayList;
import java.util.List;
import project.dto.CommentDTO;
import project.persistence.entity.CommentEntity;

public class CommentMapper {

    public static CommentEntity toEntity(CommentDTO commentDTO) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(commentDTO.getId());
        commentEntity.setStatus(commentDTO.getStatus());
        commentEntity.setContent(commentDTO.getContent());
        commentEntity.setCreatedDateTime(commentDTO.getCreatedDateTime());
        commentEntity.setSubmitterName(commentDTO.getSubmitterName());
        commentEntity.setNewsId(commentDTO.getNewsId());

        return commentEntity;
    }

    public static CommentDTO toDTO(CommentEntity commentEntity) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setStatus(commentEntity.getStatus());
        commentDTO.setContent(commentEntity.getContent());
        commentDTO.setCreatedDateTime(commentEntity.getCreatedDateTime());
        commentDTO.setSubmitterName(commentEntity.getSubmitterName());
        commentDTO.setNewsId(commentEntity.getNewsId());

        return commentDTO;
    }

    public static List<CommentEntity> toEntityList(List<CommentDTO> commentDTOS) {
        List<CommentEntity> commentEntities = new ArrayList<>();

        commentDTOS.forEach(commentDTO -> commentEntities.add(toEntity(commentDTO)));

        return commentEntities;
    }

    public static List<CommentDTO> toDTOList(List<CommentEntity> commentEntities) {
        List<CommentDTO> commentDTOS = new ArrayList<>();

        commentEntities.forEach(commentEntity -> commentDTOS.add(toDTO(commentEntity)));

        return commentDTOS;
    }
}
