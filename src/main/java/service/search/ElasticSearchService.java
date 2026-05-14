package service.search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

import co.elastic.clients.elasticsearch._types.SortOrder;

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

                                    .size(20)

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
                                    )

                                    .sort(sort -> sort

                                            .score(sc -> sc

                                                    .order(
                                                            SortOrder.Desc
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
                                                            SortOrder.Desc
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

    public List<Resource> searchByCategory(
            String category
    ) {

        List<Resource> resources =
                new ArrayList<>();

        try {

            SearchResponse<Resource> response =

                    client.search(s -> s

                                    .index("resources")

                                    .query(q -> q

                                            .match(m -> m

                                                    .field("category")

                                                    .query(category)
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

    public List<Resource> searchByDifficulty(
            String difficulty
    ) {

        List<Resource> resources =
                new ArrayList<>();

        try {

            SearchResponse<Resource> response =

                    client.search(s -> s

                                    .index("resources")

                                    .query(q -> q

                                            .match(m -> m

                                                    .field("difficulty")

                                                    .query(difficulty)
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
}