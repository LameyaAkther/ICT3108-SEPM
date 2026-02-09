package birthdaymanager;

import javafx.application.Application;
import javafx.collections.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;

public class Main extends Application {

    private TableView<Birthday> table;
    private TextField nameField;
    private DatePicker datePicker;
    private ObservableList<Birthday> data =
            FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {

        Label nameLbl = new Label("নাম:");
        Label dateLbl = new Label("জন্মদিন:");

        nameField = new TextField();
        datePicker = new DatePicker();

        Button addBtn = new Button("যোগ করুন");
        Button updateBtn = new Button("আপডেট করুন");
        Button deleteBtn = new Button("মুছে ফেলুন");
        Button searchBtn = new Button("অনুসন্ধান");
        Button refreshBtn = new Button("রিফ্রেশ");

        // TableView
        table = new TableView<>();

        TableColumn<Birthday, String> nameCol =
                new TableColumn<>("নাম");
        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("name"));
        nameCol.setMinWidth(200);

        TableColumn<Birthday, String> dateCol =
                new TableColumn<>("জন্মদিন");
        dateCol.setCellValueFactory(cell ->
                javafx.beans.binding.Bindings.createStringBinding(
                        () -> cell.getValue().getBirthdayBangla()));
        dateCol.setMinWidth(200);

        table.getColumns().addAll(nameCol, dateCol);

        HBox inputBox = new HBox(10,
                nameLbl, nameField, dateLbl, datePicker);

        HBox btnBox = new HBox(10,
                addBtn, updateBtn, deleteBtn, searchBtn, refreshBtn);

        VBox root = new VBox(10, inputBox, btnBox, table);
        root.setStyle("-fx-padding: 15");

        loadData();
        todayNotification();

        // Button actions
        addBtn.setOnAction(e -> addBirthday());
        updateBtn.setOnAction(e -> updateBirthday());
        deleteBtn.setOnAction(e -> deleteBirthday());
        searchBtn.setOnAction(e -> searchBirthday());
        refreshBtn.setOnAction(e -> loadData());

        stage.setTitle("জন্মদিন ব্যবস্থাপনা সিস্টেম");
        stage.setScene(new Scene(root, 650, 400));
        stage.show();
    }

    private void loadData() {
        data.clear();
        try (Connection con = Database.getConnection();
             Statement st = con.createStatement();
             ResultSet rs =
                     st.executeQuery("SELECT * FROM birthdays ORDER BY birthday")) {

            while (rs.next()) {
                data.add(new Birthday(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDate("birthday").toLocalDate()
                ));
            }
            table.setItems(data);

        } catch (Exception e) {
            alert("ত্রুটি", e.getMessage());
        }
    }

    private void addBirthday() {
        if (nameField.getText().isEmpty() || datePicker.getValue() == null) {
            alert("ভুল", "সব তথ্য দিন");
            return;
        }

        try (Connection con = Database.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(
                             "INSERT INTO birthdays(name,birthday) VALUES(?,?)")) {

            ps.setString(1, nameField.getText());
            ps.setDate(2, Date.valueOf(datePicker.getValue()));
            ps.executeUpdate();

            loadData();
            clear();

        } catch (Exception e) {
            alert("ত্রুটি", e.getMessage());
        }
    }

    private void updateBirthday() {
        Birthday b = table.getSelectionModel().getSelectedItem();
        if (b == null) {
            alert("ভুল", "একটি তথ্য নির্বাচন করুন");
            return;
        }

        try (Connection con = Database.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(
                             "UPDATE birthdays SET name=?, birthday=? WHERE id=?")) {

            ps.setString(1, nameField.getText());
            ps.setDate(2, Date.valueOf(datePicker.getValue()));
            ps.setInt(3, b.getId());
            ps.executeUpdate();

            loadData();
            clear();

        } catch (Exception e) {
            alert("ত্রুটি", e.getMessage());
        }
    }

    private void deleteBirthday() {
        Birthday b = table.getSelectionModel().getSelectedItem();
        if (b == null) {
            alert("ভুল", "একটি তথ্য নির্বাচন করুন");
            return;
        }

        try (Connection con = Database.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(
                             "DELETE FROM birthdays WHERE id=?")) {

            ps.setInt(1, b.getId());
            ps.executeUpdate();
            loadData();

        } catch (Exception e) {
            alert("ত্রুটি", e.getMessage());
        }
    }

    private void searchBirthday() {
        TextInputDialog d = new TextInputDialog();
        d.setHeaderText("নাম বা মাস লিখুন");
        d.showAndWait().ifPresent(q -> {
            data.clear();
            try (Connection con = Database.getConnection();
                 PreparedStatement ps =
                         con.prepareStatement(
                                 "SELECT * FROM birthdays WHERE name LIKE ? OR MONTH(birthday)=?")) {

                ps.setString(1, "%" + q + "%");
                try {
                    ps.setInt(2, Integer.parseInt(q));
                } catch (Exception e) {
                    ps.setInt(2, -1);
                }

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    data.add(new Birthday(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDate("birthday").toLocalDate()));
                }
                table.setItems(data);

            } catch (Exception e) {
                alert("ত্রুটি", e.getMessage());
            }
        });
    }

    private void todayNotification() {
        try (Connection con = Database.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(
                             "SELECT name FROM birthdays WHERE MONTH(birthday)=? AND DAY(birthday)=?")) {

            LocalDate today = LocalDate.now();
            ps.setInt(1, today.getMonthValue());
            ps.setInt(2, today.getDayOfMonth());

            ResultSet rs = ps.executeQuery();
            String msg = "";
            while (rs.next()) msg += rs.getString("name") + ", ";

            if (!msg.isEmpty())
                alert("আজ জন্মদিন 🎉", msg + "এর জন্মদিন আজ!");

        } catch (Exception ignored) {}
    }

    private void clear() {
        nameField.clear();
        datePicker.setValue(null);
    }

    private void alert(String t, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(t);
        a.setHeaderText(null);
        a.setContentText(m);
        a.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}
