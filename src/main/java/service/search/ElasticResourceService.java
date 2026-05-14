package service.search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

import config.ElasticsearchConfig;

import model.Resource;

import java.util.ArrayList;
import java.util.List;

public class ElasticResourceService {

    private final ElasticsearchClient client =
            ElasticsearchConfig.getClient();

    public void indexResource(
            Resource resource
    ) {

        try {

            IndexResponse response =

                    client.index(i -> i

                            .index("resources")

                            .id(
                                    String.valueOf(
                                            resource.getId()
                                    )
                            )

                            .document(resource)
                    );

            System.out.println(

                    "Indexed resource: "

                            + response.result()
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

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

                                                            "tags^2",

                                                            "description"

                                                    )

                                                    .fuzziness("AUTO")
                                            )
                                    ),

                            Resource.class
                    );

            for (

                    Hit<Resource> hit

                    : response.hits().hits()

            ) {

                if(hit.source() != null) {

                    resources.add(
                            hit.source()
                    );
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return resources;
    }

    public List<Resource> getTrendingResources() {

        List<Resource> resources =
                new ArrayList<>();

        try {

            SearchResponse<Resource> response =

                    client.search(s -> s

                                    .index("resources")

                                    .size(10)

                                    .sort(sort -> sort

                                            .field(f -> f

                                                    .field(
                                                            "recommendationScore"
                                                    )

                                                    .order(
                                                            co.elastic.clients.elasticsearch._types.SortOrder.Desc
                                                    )
                                            )
                                    ),

                            Resource.class
                    );

            for (

                    Hit<Resource> hit

                    : response.hits().hits()

            ) {

                if(hit.source() != null) {

                    resources.add(
                            hit.source()
                    );
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return resources;
    }

    public void deleteResource(
            int resourceId
    ) {

        try {

            client.delete(d -> d

                    .index("resources")

                    .id(
                            String.valueOf(resourceId)
                    )
            );

            System.out.println(
                    "Resource deleted from ElasticSearch"
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void updateResource(
            Resource resource
    ) {

        try {

            client.update(u -> u

                            .index("resources")

                            .id(
                                    String.valueOf(
                                            resource.getId()
                                    )
                            )

                            .doc(resource),

                    Resource.class
            );

            System.out.println(
                    "Resource updated in ElasticSearch"
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}