package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Resource;

public class ResourceCardController {

    @FXML private ImageView resourceImage;
    @FXML private Label titleLabel;
    @FXML private Label categoryLabel;
    @FXML private Label difficultyLabel;
    @FXML private Label tagsLabel;
    @FXML private Label viewsLabel;
    @FXML private Label likesLabel;

    public void setResource(Resource resource) {
        titleLabel.setText(resource.getTitle());
        categoryLabel.setText(resource.getCategory());

        difficultyLabel.setText(resource.getDifficulty() != null ? resource.getDifficulty() : "All Levels");

        tagsLabel.setText("Tags: " + (resource.getTags() != null ? resource.getTags() : "none"));
        viewsLabel.setText("👁 " + resource.getViews() + " Views");
        likesLabel.setText("♥ " + resource.getLikesCount() + " Likes");

        try {
            if (resource.getImageUrl() != null && !resource.getImageUrl().isEmpty()) {
                resourceImage.setImage(new Image(resource.getImageUrl(), true));
            }
        } catch (Exception e) {
            System.out.println("Could not load image: " + resource.getImageUrl());
        }
    }
}