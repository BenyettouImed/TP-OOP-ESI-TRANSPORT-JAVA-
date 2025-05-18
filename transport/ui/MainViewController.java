package transport.ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import transport.core.*;
import transport.control.MainController;
import java.time.LocalDate;
import java.util.stream.Collectors;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.List;

public class MainViewController {
    private final MainController controller = new MainController();

    // Navigation controls
    @FXML private StackPane contentArea;
    @FXML private VBox userManagementView;
    @FXML private VBox ticketPurchaseView;
    @FXML private VBox validationView;
    @FXML private VBox complaintsView;
    @FXML private TextField txtComplaintTarget;
    
    // User management controls
    @FXML private GridPane userForm;
    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private DatePicker dateBirthDate;
    @FXML private CheckBox chkHandicap;
    @FXML private TextField txtEmployeeId;
    @FXML private ComboBox<TypeFunction> cmbEmployeeType;
    @FXML private TextArea txtUsersOutput;
    
    // Ticket controls
    @FXML private TextArea txtTicketsOutput;
    
    // Validation controls
    @FXML private TextField txtValidateId;
    @FXML private TextArea txtValidationOutput;
    
    // Complaint controls
    @FXML private GridPane complaintForm;
    @FXML private ComboBox<Complaint.ComplaintType> cmbComplaintType;
    @FXML private TextField txtComplaintDesc;
    @FXML private TextArea txtComplaintsOutput;
    @FXML private ComboBox<Person> cmbReporter;

    @FXML
    private void initialize() {
        cmbEmployeeType.getItems().setAll(TypeFunction.values());
        cmbComplaintType.getItems().setAll(Complaint.ComplaintType.values());
        
        // Configure the reporter ComboBox to display person names
        cmbReporter.setCellFactory(param -> new ListCell<Person>() {
            @Override
            protected void updateItem(Person person, boolean empty) {
                super.updateItem(person, empty);
                if (empty || person == null) {
                    setText(null);
                } else {
                    setText(person.getPrenom() + " " + person.getNom());
                }
            }
        });
        
        cmbReporter.setButtonCell(new ListCell<Person>() {
            @Override
            protected void updateItem(Person person, boolean empty) {
                super.updateItem(person, empty);
                if (empty || person == null) {
                    setText(null);
                } else {
                    setText(person.getPrenom() + " " + person.getNom());
                }
            }
        });
        
        complaintForm.setVisible(false);
    }

    @FXML 
    private void showComplaintForm() {
        // Load available users
        cmbReporter.getItems().setAll(controller.getPersonnes());
        
        // Clear previous values
        cmbReporter.getSelectionModel().clearSelection();
        cmbComplaintType.getSelectionModel().clearSelection();
        txtComplaintTarget.clear();
        txtComplaintDesc.clear();
        
        complaintForm.setVisible(true);
        txtComplaintsOutput.clear();
    }

    @FXML 
    private void handleAddComplaint() {
        try {
            // Validate fields
            if (cmbReporter.getValue() == null) {
                throw new Exception("Please select a reporter");
            }
            if (cmbComplaintType.getValue() == null) {
                throw new Exception("Please select a complaint type");
            }
            if (txtComplaintTarget.getText().trim().isEmpty()) {
                throw new Exception("Please enter a target");
            }
            if (txtComplaintDesc.getText().trim().isEmpty()) {
                throw new Exception("Please enter a description");
            }
            
            // Get selected reporter
            Person reporter = cmbReporter.getValue();
            
            // Add the complaint
            Complaint newComplaint = controller.ajouterComplaint(
                cmbComplaintType.getValue(),
                txtComplaintDesc.getText().trim(),
                reporter,
                txtComplaintTarget.getText().trim()
            );
            
            // Show success message
            txtComplaintsOutput.setStyle("-fx-text-fill: green;");
            txtComplaintsOutput.setText("Complaint #" + newComplaint.getComplaintNumber() + " registered successfully");
            
            // Hide the form
            hideComplaintForm();
        } catch (Exception e) {
            showError(txtComplaintsOutput, e.getMessage());
        }
    }

    @FXML 
    private void hideComplaintForm() {
        complaintForm.setVisible(false);
        clearComplaintForm();
    }

    @FXML 
    private void handleShowComplaints() {
        StringBuilder output = new StringBuilder();
        
        // Group by type (TECHNIQUE first, then SERVICE, then PAIEMENT)
        output.append("=== Complaints by Type ===\n\n");
        
        // Technical complaints
        output.append("Technical Complaints:\n");
        controller.getComplaints().stream()
            .filter(c -> c.getType() == Complaint.ComplaintType.TECHNIQUE)
            .sorted((c1, c2) -> Integer.compare(c1.getComplaintNumber(), c2.getComplaintNumber()))
            .forEach(c -> output.append(formatComplaint(c)).append("\n---\n"));
        
        // Service complaints
        output.append("\nService Complaints:\n");
        controller.getComplaints().stream()
            .filter(c -> c.getType() == Complaint.ComplaintType.SERVICE)
            .sorted((c1, c2) -> Integer.compare(c1.getComplaintNumber(), c2.getComplaintNumber()))
            .forEach(c -> output.append(formatComplaint(c)).append("\n---\n"));
        
        // Payment complaints
        output.append("\nPayment Complaints:\n");
        controller.getComplaints().stream()
            .filter(c -> c.getType() == Complaint.ComplaintType.PAIEMENT)
            .sorted((c1, c2) -> Integer.compare(c1.getComplaintNumber(), c2.getComplaintNumber()))
            .forEach(c -> output.append(formatComplaint(c)).append("\n---\n"));
        
        // Check for suspensions
        if (controller.shouldSuspendStation()) {
            output.append("\n\nWARNING: Station should be suspended (more than 3 complaints)");
        }
        
        txtComplaintsOutput.setStyle("-fx-text-fill: black;");
        txtComplaintsOutput.setText(output.toString().isEmpty() ? "No complaints found" : output.toString());
    }

    private String formatComplaint(Complaint c) {
        return String.format(
            "Complaint #%d\nDate: %s\nType: %s\nTarget: %s\nDescription: %s\nSubmitted by: %s %s\nStatus: %s",
            c.getComplaintNumber(),
            c.getDate().toLocalDate(),
            c.getType(),
            c.getTarget(),
            c.getDescription(),
            c.getPlaignant().getPrenom(),
            c.getPlaignant().getNom(),
            c.isResolved() ? "RESOLVED" : "PENDING"
        );
    }

    private void clearComplaintForm() {
        cmbComplaintType.getSelectionModel().clearSelection();
        txtComplaintDesc.clear();
        txtComplaintTarget.clear();
    }

    @FXML
    private void handlePurchaseFareMedium() {
        Stage stage = new Stage();
        AddFareMediumWindow window = new AddFareMediumWindow(controller);
        Scene scene = new Scene(window, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Purchase Fare Medium");
        stage.show();
    }

    @FXML private void showUserManagement() {
        hideAllViews();
        userManagementView.setVisible(true);
    }
    
    @FXML private void showTicketPurchase() {
        hideAllViews();
        ticketPurchaseView.setVisible(true);
    }
    
    @FXML private void showValidation() {
        hideAllViews();
        validationView.setVisible(true);
    }
    
    @FXML private void showComplaints() {
        hideAllViews();
        complaintsView.setVisible(true);
    }
    
    private void hideAllViews() {
        userManagementView.setVisible(false);
        ticketPurchaseView.setVisible(false);
        validationView.setVisible(false);
        complaintsView.setVisible(false);
        userForm.setVisible(false);
        complaintForm.setVisible(false);
    }

    @FXML private void showUserForm() {
        userForm.setVisible(true);
        txtUsersOutput.clear();
    }
    
    @FXML private void hideUserForm() {
        userForm.setVisible(false);
        clearUserForm();
    }
    
    @FXML private void handleAddUser() {
        try {
            Person person = createPersonFromInput();
            controller.ajouterPersonne(person);
            txtUsersOutput.setText("User added successfully:\n" + formatPersonInfo(person));
            hideUserForm();
        } catch (Exception e) {
            txtUsersOutput.setStyle("-fx-text-fill: red;");
            txtUsersOutput.setText("Error: " + e.getMessage());
        }
    }
    
    @FXML private void handleShowUsers() {
        String users = controller.getPersonnes().stream()
            .map(this::formatPersonInfo)
            .collect(Collectors.joining("\n\n"));
        txtUsersOutput.setStyle("-fx-text-fill: black;");
        txtUsersOutput.setText(users.isEmpty() ? "No users found" : users);
    }
    
    private String formatPersonInfo(Person p) {
        return String.format("%s %s\nBorn: %s\nType: %s%s",
            p.getPrenom(), p.getNom(),
            p.getDateNaissance(),
            p instanceof Employee ? "Employee" : "Usager",
            p.isHandicap() ? " (Handicap)" : ""
        );
    }

    @FXML
    private void handleBuyTicket() {
        try {
            TitreTransport ticket = controller.acheterTicket();
            txtTicketsOutput.setText(String.format(
                "Ticket #%d purchased\nPrice: %.0f DA\nValid on: %s",
                ticket.getId(), ticket.getPrix(), ticket.getDateAchat()
            ));
        } catch (Exception e) {
            showError(txtTicketsOutput, e.getMessage());
        }
    }

    @FXML
    private void handleBuyCard() {
        try {
            if (controller.getPersonnes().isEmpty()) {
                throw new Exception("Please create a user first");
            }
            showUserForm();
            txtUsersOutput.setText("Please select a user from the list or create a new one");
        } catch (Exception e) {
            showError(txtTicketsOutput, e.getMessage());
        }
    }

    @FXML
    private void handleValidate() {
        try {
            int id = Integer.parseInt(txtValidateId.getText());
            boolean isValid = controller.verifierValidite(
                controller.getTitresTransport().stream()
                    .filter(t -> t.getId() == id)
                    .findFirst()
                    .orElseThrow(() -> new Exception("Title not found")),
                LocalDate.now()
            );
            txtValidationOutput.setStyle("-fx-text-fill: " + (isValid ? "green" : "red") + ";");
            txtValidationOutput.setText("Title #" + id + " is " + (isValid ? "VALID" : "INVALID"));
        } catch (Exception e) {
            showError(txtValidationOutput, e.getMessage());
        }
    }

    private Person createPersonFromInput() throws Exception {
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        LocalDate birthDate = dateBirthDate.getValue();
        
        if (firstName.isEmpty() || lastName.isEmpty() || birthDate == null) {
            throw new Exception("Please fill all required fields");
        }
        
        if (!txtEmployeeId.getText().isEmpty()) {
            return new Employee(
                firstName, lastName, birthDate, chkHandicap.isSelected(),
                txtEmployeeId.getText(), cmbEmployeeType.getValue()
            );
        }
        
        return new Usager(firstName, lastName, birthDate, chkHandicap.isSelected());
    }
    
    private void clearUserForm() {
        txtFirstName.clear();
        txtLastName.clear();
        dateBirthDate.setValue(null);
        chkHandicap.setSelected(false);
        txtEmployeeId.clear();
        cmbEmployeeType.getSelectionModel().clearSelection();
    }
    
    private void showError(TextArea output, String message) {
        output.setStyle("-fx-text-fill: red;");
        output.setText("ERROR: " + message);
    }
    @FXML
private void handleShowTickets() {
    List<TitreTransport> tickets = controller.getTitresTransport().stream()
        .filter(t -> t instanceof Ticket)
        .collect(Collectors.toList());

    if (tickets.isEmpty()) {
        txtTicketsOutput.setText("No tickets found");
        return;
    }

    StringBuilder output = new StringBuilder();
    output.append("=== TICKETS ===\n\n");
    
    for (TitreTransport ticket : tickets) {
        output.append(String.format(
            "Ticket #%d\n" +
            "Purchase Date: %s\n" +
            "Price: %.0f DA\n" +
            "Valid on: %s\n" +
            "-----------------\n",
            ticket.getId(),
            ticket.getDateAchat(),
            ticket.getPrix(),
            ticket.getDateAchat()
        ));
    }
    
    txtTicketsOutput.setText(output.toString());
}

@FXML
private void handleShowCards() {
    List<CartePersonnelle> cards = controller.getTitresTransport().stream()
        .filter(t -> t instanceof CartePersonnelle)
        .map(t -> (CartePersonnelle)t)
        .collect(Collectors.toList());

    if (cards.isEmpty()) {
        txtTicketsOutput.setText("No personal cards found");
        return;
    }

    StringBuilder output = new StringBuilder();
    output.append("=== PERSONAL CARDS ===\n\n");
    
    for (CartePersonnelle card : cards) {
        output.append(String.format(
            "Card #%d\n" +
            "Owner: %s %s\n" +
            "Type: %s\n" +
            "Purchase Date: %s\n" +
            "Expiry Date: %s\n" +
            "Price: %.0f DA\n" +
            "-----------------\n",
            card.getId(),
            card.getProprietaire().getPrenom(),
            card.getProprietaire().getNom(),
            card.getType(),
            card.getDateAchat(),
            card.getDateAchat().plusYears(1),
            card.getPrix()
        ));
    }
    
    txtTicketsOutput.setText(output.toString());
}
}