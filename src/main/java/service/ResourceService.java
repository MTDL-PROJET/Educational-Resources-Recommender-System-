package service;

import dao.ResourceDAO;
import model.Resource;
import model.ResourceStatus;

import java.util.List;
import service.search.ElasticResourceService;
import service.search.ElasticSearchService;

public class ResourceService {

    private final ElasticResourceService
            elasticResourceService =
            new ElasticResourceService();

    private final ElasticSearchService
            elasticSearchService =
            new ElasticSearchService();

    private final ResourceDAO resourceDAO =
            new ResourceDAO();

    public boolean addResource(
            String title,
            String description,
            String imageUrl,
            String externalLink,
            String category,
            String tags,
            String difficulty,
            int expertId
    ) {

        if(title.isBlank()
                || description.isBlank()
                || imageUrl.isBlank()
                || externalLink.isBlank()) {

            return false;
        }

        Resource resource =
                new Resource();

        resource.setTitle(title);

        resource.setDescription(description);

        resource.setImageUrl(imageUrl);

        resource.setExternalLink(externalLink);

        resource.setCategory(category);

        resource.setTags(tags);

        resource.setDifficulty(difficulty);

        resource.setStatus(
                ResourceStatus.UNPUBLISHED
        );

        resource.setExpertId(expertId);

        return resourceDAO.addResource(resource);
    }

    public List<Resource> getPublishedResources() {

        return resourceDAO
                .getAllPublishedResources();
    }
    public boolean publishResource(int id) {

        return resourceDAO.publishResource(id);
    }

    public List<Resource> searchResources(
            String keyword
    ) {

        return elasticSearchService
                .searchResources(keyword);
    }

    public List<Resource> getRecommendedResources(
            int studentId
    ) {

        return resourceDAO
                .getRecommendedResources(studentId);
    }
}