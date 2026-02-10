
package quiz;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;

public class QuizApp extends Application {

    Label questionLabel = new Label();
    RadioButton op1 = new RadioButton();
    RadioButton op2 = new RadioButton();
    RadioButton op3 = new RadioButton();
    ToggleGroup group = new ToggleGroup();
    Button nextBtn = new Button("Next");

    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    int score = 0;

    @Override
    public void start(Stage stage) throws Exception {

        op1.setToggleGroup(group);
        op2.setToggleGroup(group);
        op3.setToggleGroup(group);

        connectDB();
        loadQuestion();

        nextBtn.setOnAction(e -> checkAnswer());

        VBox root = new VBox(10, questionLabel, op1, op2, op3, nextBtn);
        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("Quiz Game");
        stage.setScene(scene);
        stage.show();
    }

    void connectDB() throws Exception {
        con = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/quizdb","root","1234");
        ps = con.prepareStatement("SELECT * FROM question ORDER BY RAND() LIMIT 1");
    }

    void loadQuestion() throws Exception {
        rs = ps.executeQuery();
        if(rs.next()) {
            questionLabel.setText(rs.getString("question"));
            op1.setText(rs.getString("op1"));
            op2.setText(rs.getString("op2"));
            op3.setText(rs.getString("op3"));
        }
    }

    void checkAnswer() {
        try {
            if(((RadioButton)group.getSelectedToggle())
                .getText().equals(rs.getString("answer"))) {
                score++;
            }
            loadQuestion();
        } catch (Exception ex) {
            Alert a = new Alert(Alert.AlertType.INFORMATION,
                    "Quiz Finished! Score = " + score);
            a.show();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
