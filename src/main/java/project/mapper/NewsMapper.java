package project.mapper;

import java.util.ArrayList;
import java.util.List;
import project.dto.NewsDTO;
import project.dto.SubjectDTO;
import project.persistence.entity.NewsEntity;
import project.persistence.entity.SubjectEntity;
import project.service.enums.NewsStatus;

public class NewsMapper {

    public static NewsEntity toEntity(NewsDTO newsDTO){
        NewsEntity newsEntity = new NewsEntity();
        newsEntity.setTitle(newsDTO.getTitle());

        if(newsDTO.getStatus() != null){
            newsEntity.setStatus(NewsStatus.valueOf(newsDTO.getStatus()).getStatusWeight());
        }
        newsEntity.setContent(newsDTO.getContent());
        newsEntity.setCreatedDateTime(newsDTO.getCreatedDateTime());
        newsEntity.setPublicationDateTime(newsDTO.getPublicationDateTime());
        newsEntity.setComments(newsDTO.getComments());
        newsEntity.setSubjects(newsDTO.getSubjects());
        newsEntity.setRejectionReason(newsDTO.getRejectionReason());

        return newsEntity;
    }

    public static NewsDTO toDTO(NewsEntity newsEntity){

        NewsStatus[] values = NewsStatus.values();

        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setTitle(newsEntity.getTitle());

        for (NewsStatus value : values) {
            if (value.getStatusWeight() == newsEntity.getStatus()) {
                newsDTO.setStatus(value.name());
            }
        }

        newsDTO.setContent(newsEntity.getContent());
        newsDTO.setCreatedDateTime(newsEntity.getCreatedDateTime());
        newsDTO.setPublicationDateTime(newsEntity.getPublicationDateTime());
        newsDTO.setComments(newsEntity.getComments());
        newsDTO.setSubjects(newsEntity.getSubjects());
        newsDTO.setRejectionReason(newsEntity.getRejectionReason());
        newsDTO.setId(newsEntity.getId());

        return newsDTO;
    }

    public static List<NewsEntity> toEntityList(List<NewsDTO> newsDTOS) {
        List<NewsEntity> newsEntities = new ArrayList<>();

        newsDTOS.forEach(newsDTO -> newsEntities.add(toEntity(newsDTO)));

        return newsEntities;
    }

    public static List<NewsDTO> toDTOList(List<NewsEntity> newsEntities) {
        List<NewsDTO> newsDTOS = new ArrayList<>();

        newsEntities.forEach(newsEntity -> newsDTOS.add(toDTO(newsEntity)));

        return newsDTOS;
    }
}
