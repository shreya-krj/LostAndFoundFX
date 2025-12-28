package app.ui;

import app.model.Admin;
import app.model.Item;
import app.model.User;
import app.service.ItemService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewItemsView {

    private Scene scene;
    private boolean isAdminView = false;

    public ViewItemsView(Stage stage, User user) {
        isAdminView = false;
        buildUI(stage, user, null);
    }

    public ViewItemsView(Stage stage, Admin admin) {
        isAdminView = true;
        buildUI(stage, null, admin);
    }

    private void buildUI(Stage stage, User user, Admin admin) {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f3f4f6;");

        /*
         * =========================
         * HEADER
         * =========================
         */
        HBox header = new HBox(20);
        header.setPadding(new Insets(20, 40, 20, 40));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle(
                "-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 0, 4, 10, 0);");

        Label title = new Label(isAdminView ? "All Reported Items" : "Community Items");
        title.getStyleClass().add("title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backBtn = new Button("Back to Dashboard");
        backBtn.getStyleClass().add("button");
        backBtn.setOnAction(e -> {
            if (isAdminView) {
                stage.setScene(new AdminDashboardLayout(stage, admin).getScene());
            } else {
                stage.setScene(new UserDashboardLayout(stage, user).getScene());
            }
        });

        header.getChildren().addAll(title, spacer, backBtn);
        root.setTop(header);

        /*
         * =========================
         * GRID CONTENT
         * =========================
         */
        FlowPane grid = new FlowPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(30));
        grid.setAlignment(Pos.TOP_LEFT);

        if (ItemService.getAllItems().isEmpty()) {
            VBox emptyState = new VBox(15);
            emptyState.setAlignment(Pos.CENTER);
            emptyState.setPadding(new Insets(100));

            Label emptyTitle = new Label("No items found");
            emptyTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #9ca3af;");

            Text emptyDesc = new Text("It looks like there are no lost or found items reported yet.");
            emptyDesc.setStyle("-fx-fill: #6b7280; -fx-font-size: 14px;");

            emptyState.getChildren().addAll(emptyTitle, emptyDesc);
            root.setCenter(emptyState);
        } else {
            for (Item item : ItemService.getAllItems()) {
                grid.getChildren().add(createItemTile(stage, item));
            }
            ScrollPane scrollPane = new ScrollPane(grid);
            scrollPane.setFitToWidth(true);
            scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
            root.setCenter(scrollPane);
        }

        scene = new Scene(root, 1100, 700);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
    }

    /*
     * =========================
     * ITEM TILE (CARD)
     * =========================
     */
    private VBox createItemTile(Stage stage, Item item) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setPrefWidth(280);
        card.setMaxWidth(300);
        card.getStyleClass().add("item-card");
        card.setStyle("-fx-background-color: white; -fx-cursor: hand;");

        // Image Badge
        StackPane imageContainer = new StackPane();
        imageContainer.setStyle("-fx-background-color: #f3f4f6; -fx-background-radius: 8;");
        imageContainer.setPrefHeight(180);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(180);
        imageView.setFitWidth(250);
        imageView.setPreserveRatio(true);

        if (item.getImagePath() != null) {
            imageView.setImage(new Image(item.getImagePath()));
        } else {
            // Placeholder can go here if needed
        }
        imageContainer.getChildren().add(imageView);

        // Tags
        HBox tags = new HBox(8);
        Label statusLabel = new Label(item.getStatus());
        statusLabel.getStyleClass().add("status-pill");
        statusLabel.getStyleClass().add(
                "RETURNED".equals(item.getStatus()) ? "status-returned"
                        : "LOST".equals(item.getType()) ? "status-lost" : "status-found");

        Label typeLabel = new Label(item.getType());
        typeLabel.setStyle(
                "-fx-font-size: 10px; -fx-text-fill: #6b7280; -fx-background-color: #e5e7eb; -fx-padding: 2 6; -fx-background-radius: 4;");

        tags.getChildren().addAll(statusLabel, typeLabel);

        // Text Info
        Label name = new Label(item.getTitle());
        name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #111827;");

        Label location = new Label("📍 " + item.getLocation());
        location.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 12px;");

        Label category = new Label("Category: " + item.getCategory());
        category.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 12px;");

        card.getChildren().addAll(imageContainer, tags, name, location, category);

        card.setOnMouseClicked(e -> showItemDetails(stage, item));

        return card;
    }

    /*
     * =========================
     * DETAILS MODAL
     * =========================
     */
    private void showItemDetails(Stage owner, Item item) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Item Details");

        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setAlignment(Pos.TOP_CENTER);
        content.setStyle("-fx-background-color: white;");

        // Large Image
        ImageView imageView = new ImageView();
        imageView.setFitWidth(350);
        imageView.setPreserveRatio(true);
        if (item.getImagePath() != null) {
            imageView.setImage(new Image(item.getImagePath()));
        }

        Label title = new Label(item.getTitle());
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        VBox details = new VBox(8);
        details.setAlignment(Pos.CENTER_LEFT);

        details.getChildren().addAll(
                new Label("Type: " + item.getType()),
                new Label("Category: " + item.getCategory()),
                new Label("Location: " + item.getLocation()),
                new Label("Description: " + item.getDescription()));

        if (isAdminView) {
            Label contactTitle = new Label("Contact Information");
            contactTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-padding: 10 0 5 0;");

            details.getChildren().addAll(
                    contactTitle,
                    new Label("Reporter: " + item.getReporterName()),
                    new Label("Phone: " + item.getPhone()),
                    new Label("Email: " + item.getEmail()));
        }

        Button closeBtn = new Button("Close");
        closeBtn.getStyleClass().add("primary-btn");
        closeBtn.setPrefWidth(100);
        closeBtn.setOnAction(e -> dialog.close());

        content.getChildren().addAll(imageView, title, details, new Region(), closeBtn);
        VBox.setVgrow(content.getChildren().get(3), Priority.ALWAYS); // spacer before button

        Scene scene = new Scene(content, 450, 650);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        dialog.setScene(scene);
        dialog.showAndWait();
    }

    public Scene getScene() {
        return scene;
    }
}
