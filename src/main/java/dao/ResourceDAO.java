package dao;

import config.DatabaseConfig;
import model.Resource;
import model.ResourceStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import service.search.ElasticResourceService;

public class ResourceDAO {

    private final ElasticResourceService
            elasticResourceService =
            new ElasticResourceService();

    public boolean addResource(Resource resource) {

        String sql =
                "INSERT INTO resources " +
                        "(title, description, image_url, external_link,\n" +
                        " category, tags, difficulty,\n" +
                        " status, expert_id,\n" +
                        " views, likes_count, search_hits,\n" +
                        " recommendation_score) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (

                Connection connection =
                        DatabaseConfig.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql,
                                PreparedStatement.RETURN_GENERATED_KEYS
                        )

        ) {

            statement.setString(1, resource.getTitle());

            statement.setString(2, resource.getDescription());

            statement.setString(3, resource.getImageUrl());

            statement.setString(4, resource.getExternalLink());

            statement.setString(5, resource.getCategory());

            statement.setString(6, resource.getTags());

            statement.setString(7, resource.getDifficulty());

            statement.setString(
                    8,
                    resource.getStatus().name()
            );

            statement.setInt(
                    9,
                    resource.getExpertId()
            );

            statement.setInt(
                    10,
                    resource.getViews()
            );

            statement.setInt(
                    11,
                    resource.getLikesCount()
            );

            statement.setInt(
                    12,
                    resource.getSearchHits()
            );

            statement.setDouble(
                    13,
                    resource.getRecommendationScore()
            );

            int rows =
                    statement.executeUpdate();

            ResultSet generatedKeys =
                    statement.getGeneratedKeys();

            if(generatedKeys.next()) {

                resource.setId(
                        generatedKeys.getInt(1)
                );
            }

            if(rows > 0) {

                elasticResourceService
                        .indexResource(resource);

                return true;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    public List<Resource> getAllPublishedResources() {

        List<Resource> resources =
                new ArrayList<>();

        String sql =
                "SELECT * FROM resources " +
                        "WHERE status = 'PUBLISHED' " +
                        "ORDER BY recommendation_score DESC";

        try (

                Connection connection =
                        DatabaseConfig.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)

        ) {

            ResultSet resultSet =
                    statement.executeQuery();

            while(resultSet.next()) {

                Resource resource =
                        new Resource();

                resource.setId(
                        resultSet.getInt("id")
                );

                resource.setTitle(
                        resultSet.getString("title")
                );

                resource.setDescription(
                        resultSet.getString("description")
                );

                resource.setImageUrl(
                        resultSet.getString("image_url")
                );

                resource.setExternalLink(
                        resultSet.getString("external_link")
                );

                resource.setCategory(
                        resultSet.getString("category")
                );

                resource.setStatus(
                        ResourceStatus.valueOf(
                                resultSet.getString("status")
                        )
                );

                resource.setExpertId(
                        resultSet.getInt("expert_id")
                );

                resource.setTags(
                        resultSet.getString("tags")
                );

                resource.setDifficulty(
                        resultSet.getString("difficulty")
                );

                resource.setViews(
                        resultSet.getInt("views")
                );

                resource.setLikesCount(
                        resultSet.getInt("likes_count")
                );

                resource.setSearchHits(
                        resultSet.getInt("search_hits")
                );

                resource.setRecommendationScore(
                        resultSet.getDouble(
                                "recommendation_score"
                        )
                );

                resources.add(resource);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return resources;
    }

    public boolean publishResource(int resourceId) {

        String sql =
                "UPDATE resources " +
                        "SET status = 'PUBLISHED' " +
                        "WHERE id = ?";

        try (

                Connection connection =
                        DatabaseConfig.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)

        ) {

            statement.setInt(1, resourceId);

            int rows =
                    statement.executeUpdate();

            if(rows > 0) {

                Resource resource =
                        getResourceById(resourceId);

                elasticResourceService
                        .updateResource(resource);
            }

            return rows > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }
    public List<Resource> getRecommendedResources(
            int studentId
    ) {

        List<Resource> resources =
                new ArrayList<>();

        String sql =

                "SELECT r.*, COUNT(*) AS score " +

                        "FROM resources r " +

                        "JOIN resource_tags rt " +
                        "ON r.id = rt.resource_id " +

                        "JOIN student_interests si " +
                        "ON rt.interest_id = si.interest_id " +

                        "WHERE si.student_id = ? " +

                        "AND r.status = 'PUBLISHED' " +

                        "GROUP BY r.id " +

                        "ORDER BY score DESC";

        try (

                Connection connection =
                        DatabaseConfig.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)

        ) {

            statement.setInt(1, studentId);

            ResultSet resultSet =
                    statement.executeQuery();

            while(resultSet.next()) {

                Resource resource =
                        new Resource();

                resource.setId(
                        resultSet.getInt("id")
                );

                resource.setTitle(
                        resultSet.getString("title")
                );

                resource.setDescription(
                        resultSet.getString("description")
                );

                resource.setCategory(
                        resultSet.getString("category")
                );

                resource.setTags(
                        resultSet.getString("tags")
                );

                resource.setDifficulty(
                        resultSet.getString("difficulty")
                );

                resource.setViews(
                        resultSet.getInt("views")
                );

                resource.setLikesCount(
                        resultSet.getInt("likes_count")
                );

                resource.setSearchHits(
                        resultSet.getInt("search_hits")
                );

                resource.setRecommendationScore(
                        resultSet.getDouble(
                                "recommendation_score"
                        )
                );


                resources.add(resource);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return resources;
    }

    public List<Resource> searchResources(
            String keyword
    ) {

        List<Resource> resources =
                new ArrayList<>();

        String sql =

                "SELECT * FROM resources " +

                        "WHERE status = 'PUBLISHED' " +

                        "AND (" +

                        "title LIKE ? " +

                        "OR category LIKE ? " +

                        "OR description LIKE ?" +

                        ")";

        try (

                Connection connection =
                        DatabaseConfig.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)

        ) {

            String search =
                    "%" + keyword + "%";

            statement.setString(1, search);

            statement.setString(2, search);

            statement.setString(3, search);

            ResultSet resultSet =
                    statement.executeQuery();

            while(resultSet.next()) {

                Resource resource =
                        new Resource();

                resource.setId(
                        resultSet.getInt("id")
                );

                resource.setTitle(
                        resultSet.getString("title")
                );

                resource.setCategory(
                        resultSet.getString("category")
                );

                resource.setDescription(
                        resultSet.getString("description")
                );

                resource.setTags(
                        resultSet.getString("tags")
                );

                resource.setDifficulty(
                        resultSet.getString("difficulty")
                );

                resource.setViews(
                        resultSet.getInt("views")
                );

                resource.setLikesCount(
                        resultSet.getInt("likes_count")
                );

                resource.setSearchHits(
                        resultSet.getInt("search_hits")
                );

                resource.setRecommendationScore(
                        resultSet.getDouble(
                                "recommendation_score"
                        )
                );

                resources.add(resource);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return resources;
    }

    public void incrementSearchHits(
            int resourceId
    ) {

        String sql =

                "UPDATE resources " +

                        "SET search_hits = " +

                        "search_hits + 1 " +

                        "WHERE id = ?";

        try (

                Connection connection =
                        DatabaseConfig.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)

        ) {

            statement.setInt(1, resourceId);

            statement.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void updateRecommendationScore(int resourceId) {

        String sql =

                "UPDATE resources " +

                        "SET recommendation_score = " +

                        "(views * 0.4) + " +
                        "(likes_count * 0.4) + " +
                        "(search_hits * 0.2) " +

                        "WHERE id = ?";

        try (

                Connection connection =
                        DatabaseConfig.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)

        ) {

            statement.setInt(1, resourceId);

            statement.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void incrementViews(
            int resourceId
    ) {

        String sql =

                "UPDATE resources " +

                        "SET views = views + 1 " +

                        "WHERE id = ?";

        try (

                Connection connection =
                        DatabaseConfig.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)

        ) {

            statement.setInt(
                    1,
                    resourceId
            );

            statement.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    public List<Resource> getTrendingResources() {

        List<Resource> resources =
                new ArrayList<>();

        String sql =

                "SELECT * FROM resources " +

                        "WHERE status = 'PUBLISHED' " +

                        "ORDER BY recommendation_score DESC, views DESC " +

                        "LIMIT 5";

        try (

                Connection connection =
                        DatabaseConfig.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)

        ) {

            ResultSet resultSet =
                    statement.executeQuery();

            while(resultSet.next()) {

                Resource resource =
                        new Resource();

                resource.setId(
                        resultSet.getInt("id")
                );

                resource.setTitle(
                        resultSet.getString("title")
                );

                resource.setCategory(
                        resultSet.getString("category")
                );

                resource.setDifficulty(
                        resultSet.getString("difficulty")
                );

                resources.add(resource);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return resources;
    }

    public Resource getResourceById(
            int resourceId
    ) {

        String sql =
                "SELECT * FROM resources " +
                        "WHERE id = ?";

        try (

                Connection connection =
                        DatabaseConfig.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)

        ) {

            statement.setInt(1, resourceId);

            ResultSet resultSet =
                    statement.executeQuery();

            if(resultSet.next()) {

                Resource resource =
                        new Resource();

                resource.setId(
                        resultSet.getInt("id")
                );

                resource.setTitle(
                        resultSet.getString("title")
                );

                resource.setDescription(
                        resultSet.getString("description")
                );

                resource.setImageUrl(
                        resultSet.getString("image_url")
                );

                resource.setExternalLink(
                        resultSet.getString("external_link")
                );

                resource.setCategory(
                        resultSet.getString("category")
                );

                resource.setStatus(
                        ResourceStatus.valueOf(
                                resultSet.getString("status")
                        )
                );

                resource.setExpertId(
                        resultSet.getInt("expert_id")
                );

                resource.setTags(
                        resultSet.getString("tags")
                );

                resource.setDifficulty(
                        resultSet.getString("difficulty")
                );

                resource.setViews(
                        resultSet.getInt("views")
                );

                resource.setLikesCount(
                        resultSet.getInt("likes_count")
                );

                resource.setSearchHits(
                        resultSet.getInt("search_hits")
                );

                resource.setRecommendationScore(
                        resultSet.getDouble(
                                "recommendation_score"
                        )
                );

                return resource;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

}