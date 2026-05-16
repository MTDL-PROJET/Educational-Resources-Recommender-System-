package service.search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;

import config.ElasticsearchConfig;
import model.Resource;

import java.util.ArrayList;
import java.util.List;

public class ElasticSearchService {

    private final ElasticsearchClient client =
            ElasticsearchConfig.getClient();

    public List<Resource> searchResources(
            String keyword
    ) {

        List<Resource> resources =
                new ArrayList<>();

        try {

            SearchResponse<Resource> response =

                    client.search(s -> s

                                    .index("resources")

                                    .query(q -> q

                                            .multiMatch(m -> m

                                                    .query(keyword)

                                                    .fields(
                                                            "title^3",
                                                            "category^2",
                                                            "description",
                                                            "tags"
                                                    )

                                                    .fuzziness("AUTO")
                                            )
                                    ),

                            Resource.class
                    );

            response.hits()
                    .hits()
                    .forEach(hit -> {

                        if(hit.source() != null) {

                            resources.add(
                                    hit.source()
                            );
                        }
                    });

        } catch (Exception e) {

            e.printStackTrace();
        }

        return resources;
    }
}