package transport.ui;

import transport.control.MainController;
import transport.core.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.collections.FXCollections;

public class AddFareMediumWindow extends VBox {
    private final MainController controller;
    private final ComboBox<Person> personComboBox;
    private final ToggleGroup fareTypeGroup = new ToggleGroup(); // Initialize here
    private final TextField priceField;
    private final Label priceLabel;

    public AddFareMediumWindow(MainController controller) {
        this.controller = controller;
        this.personComboBox = new ComboBox<>();
        this.priceField = new TextField();
        this.priceLabel = new Label("Price: 50 DA");
        
        this.setSpacing(10);
        this.setPadding(new Insets(15));
        initializeUI();
    }

    private void initializeUI() {
        // Title
        Label titleLabel = new Label("Purchase Fare Medium");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Fare type selection
        Label typeLabel = new Label("Select Fare Type:");
        
        RadioButton ticketRadio = new RadioButton("Single Ticket (50 DA)");
        ticketRadio.setToggleGroup(fareTypeGroup);
        ticketRadio.setSelected(true);
        
        RadioButton cardRadio = new RadioButton("Personal Navigation Card");
        cardRadio.setToggleGroup(fareTypeGroup);

        // Person selection (for cards)
        Label personLabel = new Label("Select Passenger:");
        personComboBox.setItems(FXCollections.observableArrayList(controller.getPersonnes()));
        personComboBox.setDisable(true);

        // Price display
        priceField.setVisible(false);

        // Payment method
        Label paymentLabel = new Label("Payment Method:");
        ComboBox<PaymentMethod> paymentCombo = new ComboBox<>();
        paymentCombo.setItems(FXCollections.observableArrayList(PaymentMethod.values()));

        // Purchase button
        Button purchaseButton = new Button("Purchase");
        purchaseButton.setOnAction(e -> handlePurchase(paymentCombo.getValue()));

        // Layout
        VBox fareTypeBox = new VBox(5, typeLabel, ticketRadio, cardRadio);
        HBox priceBox = new HBox(5, priceLabel, priceField);
        
        this.getChildren().addAll(
            titleLabel,
            fareTypeBox,
            personLabel,
            personComboBox,
            priceBox,
            paymentLabel,
            paymentCombo,
            purchaseButton
        );

        // Event handlers
        ticketRadio.selectedProperty().addListener((obs, oldVal, newVal) -> {
            personComboBox.setDisable(true);
            priceLabel.setText("Price: 50 DA");
            priceField.setVisible(false);
        });

        cardRadio.selectedProperty().addListener((obs, oldVal, newVal) -> {
            personComboBox.setDisable(false);
            priceLabel.setText("Calculated Price:");
            priceField.setVisible(true);
            updateCardPrice();
        });

        personComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) updateCardPrice();
        });
    }

    private void updateCardPrice() {
        Person selectedPerson = personComboBox.getValue();
        if (selectedPerson != null) {
            try {
                double price = 5000 * (1 - CartePersonnelle.determinerTypeCarte(selectedPerson).getReduction());
                priceField.setText(String.format("%.2f DA", price));
            } catch (ReductionImpossibleException e) {
                priceField.setText("Not eligible");
            }
        }
    }

    private void handlePurchase(PaymentMethod paymentMethod) {
        try {
            RadioButton selected = (RadioButton)fareTypeGroup.getSelectedToggle();
            TitreTransport titre = selected.getText().startsWith("Single") 
                ? controller.acheterTicket()
                : controller.acheterCartePersonnelle(personComboBox.getValue());
            
            showSuccessMessage(titre);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void showSuccessMessage(TitreTransport titre) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Purchase Complete");
        alert.setHeaderText("Fare Medium Purchased Successfully");
        
        String details = titre instanceof Ticket 
            ? "Ticket ID: " + titre.getId()
            : "Personal Card ID: " + titre.getId() + "\nOwner: " + ((CartePersonnelle)titre).getProprietaire().getNom();
        
        alert.setContentText(details);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Purchase Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }
}