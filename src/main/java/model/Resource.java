package model;

public class Resource {

    private int id;

    private String title;

    private String description;

    private String imageUrl;

    private String externalLink;

    private String category;

    private ResourceStatus status;

    private int expertId;

    private String tags;

    private String difficulty;

    private int views;

    private int likesCount;

    private int searchHits;

    private double recommendationScore;


    public Resource() {
    }

    public Resource(
            int id,
            String title,
            String description,
            String imageUrl,
            String externalLink,
            String category,
            ResourceStatus status,
            int expertId
    ) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.externalLink = externalLink;
        this.category = category;
        this.status = status;
        this.expertId = expertId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getExternalLink() {
        return externalLink;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ResourceStatus getStatus() {
        return status;
    }

    public void setStatus(ResourceStatus status) {
        this.status = status;
    }

    public int getExpertId() {
        return expertId;
    }

    public void setExpertId(int expertId) {
        this.expertId = expertId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getSearchHits() {
        return searchHits;
    }

    public void setSearchHits(int searchHits) {
        this.searchHits = searchHits;
    }

    public double getRecommendationScore() {
        return recommendationScore;
    }

    public void setRecommendationScore(double recommendationScore) {
        this.recommendationScore = recommendationScore;
    }

    @Override
    public String toString() {

        return title
                + " | "
                + category
                + " | "
                + difficulty;
    }
}