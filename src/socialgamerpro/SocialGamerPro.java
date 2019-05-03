/*
Capstone Team 1n 
 */
package socialgamerpro;

import java.awt.Desktop;
import javafx.scene.input.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Screen;
import javafx.stage.Stage;
/**
 * Keaton, Will, Mike, and Amin 1/21/2019
 *
 */
public class SocialGamerPro extends Application{

    private final TextField tfUsername = new TextField();
    private final PasswordField tfPassword = new PasswordField();
    private final Label lbUsername = new Label("Username");
    private final Label lbPassword = new Label("Password");
    private final Button btnLogin = new Button("Login");
    private final Button btnCreateAccount = new Button("Create Account");
    private final ImageView imgViewLogo = new ImageView();
    private final Image imgLogo = new Image("background.png");
    private final Label wrongLogin = new Label("");
    private final Button btnForgotPassword = new Button("Forgot Password");

    private FileChooser fileChooser;
    private File file;
    private Desktop desktop = Desktop.getDesktop();
    //private ImageView imgProfile = new ImageView();
    private Image image;
    //needed to pull picture as a blob into database
    private FileInputStream fis;
    //private Statement stmt;
    private final PreparedStatement stmt = null;
    Text text = new Text();

    // New comment to test pushing to GitHub
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Datbase Utility object used for checking login credentials
        // This database is connected to my localhost:3306.  I believe its the default port 
        // when setting up a database locally using phpmyadmin
        DBUtility db = new DBUtility();

        //The vertical Box holds the text fields, labels, and the log in button
        VBox vBox = new VBox();
        StackPane logoStackpane = new StackPane();
        imgViewLogo.setImage(imgLogo);
        imgViewLogo.setFitHeight(150);
        imgViewLogo.setPreserveRatio(true);
        logoStackpane.getChildren().add(imgViewLogo);
        logoStackpane.setPadding(new Insets(25));
        //imgViewLogo.set

//        tfUsername.setMaxWidth(200);
//        tfPassword.setMaxWidth(200);
        //VBox vBoxDashboard = new VBox();
        // adding the labesl, textfields and button to the vertical box
        vBox.getChildren().addAll(logoStackpane, text, tfUsername, lbUsername, tfPassword, lbPassword, btnLogin, btnCreateAccount, btnForgotPassword, wrongLogin);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(5);
        //vBox.set

        // Adding the vertical box to the scene
        Scene scene = new Scene(vBox, 250, 500);
 
        // Key Pressed Event starts here - needs a switch statement
        scene.setOnKeyPressed(event -> {
            switch (event.getCode())
            {
                case ENTER:
            // connecting to the database
            try {
                db.dbConnect();
            } catch (SQLException ex) {
                Logger.getLogger(SocialGamerPro.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                if (db.checkLogin(tfUsername.getText(), tfPassword.getText())) {

                    primaryStage.close();
                    User user = new User(db.getUserInfo(tfUsername.getText()));
                    user.getDashboard().launchDashboard();
                    db.dbClose();

                } else {
                    //loginError();
                    wrongLogin.setText("WRONG USERNAME OR PASSWORD");
                    wrongLogin.setStyle("-fx-text-fill: red; -fx-font-size: 12");
                }
            } catch (SQLException ex) {
                Logger.getLogger(SocialGamerPro.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SocialGamerPro.class.getName()).log(Level.SEVERE, null, ex);
            }
                    break;
                default:
                    break;
            }
            
            });
        //scene.setOnKeyReleased(event -> text.setText(""));

        // adding the stylesheet
        scene.getStylesheets().add(SocialGamerPro.class.getResource("Login.css").toExternalForm());
        primaryStage.show();
        // this is the listener that calls the function to check if the username matches the password
        // if correct, the window will close and a new window will open that will be the user's dashboard
        btnLogin.setOnAction((javafx.event.ActionEvent e) -> {

            // connecting to the database
            try {
                db.dbConnect();
            } catch (SQLException ex) {
                Logger.getLogger(SocialGamerPro.class.getName()).log(Level.SEVERE, null, ex);
            }

            // This method, "checkLogin", takes the text entered in the username and password field and passes
            // them along as strings.  Click over to the DBUtility class to see the method.
            // It is a boolean.  If returns true, it opens a new window, the future dashboard
            // if false, it will pop up with a error message (not complete)
            try {
                if (db.checkLogin(tfUsername.getText(), tfPassword.getText())) {

                    primaryStage.close();
                    User user = new User(db.getUserInfo(tfUsername.getText()));
                    user.getDashboard().launchDashboard();
                    db.dbClose();

                } else {
                    //loginError();
                    wrongLogin.setText("WRONG USERNAME OR PASSWORD");
                    wrongLogin.setStyle("-fx-text-fill: red; -fx-font-size: 12");
                }
            } catch (SQLException ex) {
                Logger.getLogger(SocialGamerPro.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SocialGamerPro.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        btnCreateAccount.setOnAction((javafx.event.ActionEvent e) -> {

            createAccountWindow();
        });

        btnForgotPassword.setOnAction((javafx.event.ActionEvent e) -> {
            
            forgotPasswordWindow();
        });
        
        primaryStage.setTitle("Social Gaming Pro!");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
       Rectangle2D primScreenBounds1 = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds1.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds1.getHeight() - primaryStage.getHeight()) / 2);
        
        primaryStage.show();

    }

    public void createAccountWindow() {

        TextField tfUsernameNew = new TextField();
        TextField tfFirstName = new TextField();
        TextField tfLastName = new TextField();
        TextField tfEmail = new TextField();
        TextField tfDob = new TextField();
        DatePicker dpDob = new DatePicker();
        TextArea tfBio = new TextArea();
        PasswordField tfPassword1 = new PasswordField();
        PasswordField tfPassword2 = new PasswordField();
        Label lbUsernameNew = new Label("Username");
        Label lbPassword1 = new Label("Password");
        Label lbPassword2 = new Label("Confirm Password");
        Label lbFirstName = new Label("First Name");
        Label lbLastName = new Label("Last Name");
        Label lbEmail = new Label("Email");
        Label lbDob = new Label("Enter Birthday (ex 1990-01-31)");
        Label lbBio = new Label("Bio");
        Button btnCreateAccountNew = new Button("Create Account");
        Button btnExit = new Button("Exit");
        Button btnBrowse = new Button("Browse");
        Label lbBrowsePath = new Label("");
        ImageView imgProfile = new ImageView(image);

//Allows you to select an Image File
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif")
        );

        Stage createAccountStage = new Stage();
        //sets title at top of window
        createAccountStage.setTitle("Create Account");

        VBox vBox = new VBox();

        GridPane gridpane = new GridPane();
        // gridpane.getColumnConstraints().add(new ColumnConstraints(50));

        StackPane logoStackpane = new StackPane();
        imgViewLogo.setImage(imgLogo);
        imgViewLogo.setFitHeight(150);
        imgViewLogo.setPreserveRatio(true);
        logoStackpane.getChildren().add(imgViewLogo);
        logoStackpane.setPadding(new Insets(25));
        //imgViewLogo.set
        gridpane.add(lbUsernameNew, 0, 0);
        gridpane.add(tfUsernameNew, 1, 0);
        gridpane.add(lbPassword1, 0, 1);
        gridpane.add(tfPassword1, 1, 1);
        gridpane.add(lbPassword2, 0, 2);
        gridpane.add(tfPassword2, 1, 2);
        gridpane.add(lbFirstName, 0, 3);
        gridpane.add(tfFirstName, 1, 3);
        gridpane.add(lbLastName, 0, 4);
        gridpane.add(tfLastName, 1, 4);
        gridpane.add(lbEmail, 0, 5);
        gridpane.add(tfEmail, 1, 5);
        gridpane.add(lbDob, 0, 6);
        gridpane.add(dpDob, 1, 6);

        vBox.getChildren().addAll(gridpane, lbBrowsePath, btnBrowse, imgProfile, lbBio, tfBio, btnCreateAccountNew, btnExit);

        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);

        //width, height of actual scene
        Scene createAccountDashboard = new Scene(vBox, 450, 650);
        createAccountDashboard.getStylesheets().add(SocialGamerPro.class.getResource("Login.css").toExternalForm());

        createAccountStage.setScene(createAccountDashboard);
//        createAccountStage.setMinHeight(650);
//        createAccountStage.setMinWidth(1100);
        //primaryStage.close();
		createAccountStage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        createAccountStage.setX((primScreenBounds.getWidth() - createAccountStage.getWidth()) / 2);
        createAccountStage.setY((primScreenBounds.getHeight() - createAccountStage.getHeight()) / 2);
        createAccountStage.show();
        //set background color to a light grey
//        vBox.setStyle("-fx-background-color: #DCDCDC;");

        btnCreateAccountNew.setOnAction((javafx.event.ActionEvent e) -> {
//validates password using regex to require a number, a lowercase letter, an uppercase letter, a special character (!@#$%^&+=), and has to be 8 characters or more).
            Pattern p = Pattern.compile("((?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=]).{8,})");
            Matcher m = p.matcher(tfPassword1.getText());

//confirming that both password fields match            
            String pwd = tfPassword1.getText();
            String confpwd = tfPassword2.getText();

            if (tfUsernameNew.getText().isEmpty()
                    | tfFirstName.getText().isEmpty()
                    | tfLastName.getText().isEmpty()
                    | tfEmail.getText().isEmpty()
                    | tfBio.getText().isEmpty()
                    | !m.matches()) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Error in Validating Fields");
                alert.setHeaderText(null);
                alert.setContentText("Please make sure your fields are not empty and that your password is 8 or more characters, has a number, a lowercase letter, an uppercase letter, and a special character.");
                alert.showAndWait();
            } else {

//verifying that the password matches in order to create the account
                if (pwd.equals(confpwd)) {

                    System.out.println("Test create new account!");
                    DBUtility dbNewAccount = new DBUtility();

                    try {
                        dbNewAccount.dbConnect();
                    } catch (SQLException ex) {
                        Logger.getLogger(SocialGamerPro.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    try {
                        dbNewAccount.createNewAccount(tfUsernameNew.getText(), tfPassword1.getText(), tfFirstName.getText(), tfLastName.getText(), tfEmail.getText(), dpDob.getValue(), file, tfBio.getText());
                    } catch (SQLException | IOException ex) {
                        Logger.getLogger(SocialGamerPro.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        dbNewAccount.dbClose();
                        createAccountStage.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(SocialGamerPro.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Error in Password Fields");
                    alert.setHeaderText(null);
                    alert.setContentText("Please make sure your passwords match.");
                    alert.showAndWait();
                }
            }
        });
        
//closes the create account page and returns to login page
        btnExit.setOnAction((javafx.event.ActionEvent e) -> {
            createAccountStage.close();

        });

        btnBrowse.setOnAction((javafx.event.ActionEvent e) -> {
            file = fileChooser.showOpenDialog(createAccountStage);
            if (file != null) {
                //desktop.open(file);
                lbBrowsePath.setText(file.getAbsolutePath());
                //image = new Image(path, width, height, preserved ratio, smooth);
                image = new Image(file.toURI().toString(), 100, 150, true, true);
                imgProfile.setImage(image);
                imgProfile.setFitWidth(100);
                imgProfile.setFitHeight(150);
                imgProfile.setPreserveRatio(true);

                //layout.setCenter(imgProfile);
                //BorderPane.setAlignment(imgPro, Pos.TOP_LEFT);
            }
        });

    }
    
    public void forgotPasswordWindow(){

        TextField tfUsernameNew = new TextField();
        //TextField tfFirstName = new TextField();
        //TextField tfLastName = new TextField();
        TextField tfEmail = new TextField();
        //TextField tfDob = new TextField();
        //DatePicker dpDob = new DatePicker();
        //TextArea tfBio = new TextArea();
        //PasswordField tfPassword3 = new PasswordField();
        //PasswordField tfPassword4 = new PasswordField();
        Label lbUsernameNew = new Label("What is your username?  ");
        //Label lbPassword3 = new Label("Password");
        //Label lbPassword4 = new Label("Confirm Password");
        //Label lbFirstName = new Label("First Name");
        //Label lbLastName = new Label("Last Name");
        Label lbEmail = new Label("What is your email?   ");
        //Label lbDob = new Label("Enter Birthday (ex 1990-01-31)");
        //Label lbBio = new Label("Bio");
        Button btnSubmit = new Button("Submit");
        Button btnExit = new Button("Exit");
        //Button btnBrowse = new Button("Browse");
        Label lbWrongUsernamePwd = new Label("");
        //ImageView imgProfile = new ImageView(image);

        Stage forgotPasswordStage = new Stage();
        //sets title at top of window
        forgotPasswordStage.setTitle("Forgot Password?");

        VBox vBox = new VBox();

        GridPane gridpane = new GridPane();
        // gridpane.getColumnConstraints().add(new ColumnConstraints(50));

        StackPane logoStackpane = new StackPane();
        imgViewLogo.setImage(imgLogo);
        imgViewLogo.setFitHeight(150);
        imgViewLogo.setPreserveRatio(true);
        logoStackpane.getChildren().add(imgViewLogo);
        logoStackpane.setPadding(new Insets(25));
        //imgViewLogo.set
        gridpane.add(lbUsernameNew, 0, 0);
        gridpane.add(tfUsernameNew, 1, 0);
        gridpane.add(lbEmail, 0, 1);
        gridpane.add(tfEmail, 1, 1);
        //gridpane.add(lbPassword3, 0, 2);
        //gridpane.add(tfPassword3, 1, 2);
        //gridpane.add(lbPassword4, 0, 3);
        //gridpane.add(tfPassword4, 1, 3);
        vBox.getChildren().addAll(gridpane, btnSubmit, btnExit, lbWrongUsernamePwd);

        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);

        //width, height of actual scene
        Scene forgotPasswordDashboard = new Scene(vBox, 400, 450);
        forgotPasswordDashboard.getStylesheets().add(SocialGamerPro.class.getResource("Login.css").toExternalForm());

        forgotPasswordStage.setScene(forgotPasswordDashboard);
//        createAccountStage.setMinHeight(650);
//        createAccountStage.setMinWidth(1100);
        //primaryStage.close();
		forgotPasswordStage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        forgotPasswordStage.setX((primScreenBounds.getWidth() - forgotPasswordStage.getWidth()) / 2);
        forgotPasswordStage.setY((primScreenBounds.getHeight() - forgotPasswordStage.getHeight()) / 2);
        forgotPasswordStage.show();
        //set background color to a light grey
//        vBox.setStyle("-fx-background-color: #DCDCDC;");

        btnSubmit.setOnAction((javafx.event.ActionEvent e) -> {
            DBUtility db = new DBUtility();
            // connecting to the database
            try {
                db.dbConnect();
            } catch (SQLException ex) {
                Logger.getLogger(SocialGamerPro.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                if (db.checkEmail(tfUsernameNew.getText(), tfEmail.getText())) {

                    changePasswordWindow();
                    forgotPasswordStage.close();
                    db.dbClose();

                } else {
                    //loginError();
                    lbWrongUsernamePwd.setText("WRONG USERNAME OR EMAIL");
                    lbWrongUsernamePwd.setStyle("-fx-text-fill: red; -fx-font-size: 12");
                }
            } catch (SQLException ex) {
                Logger.getLogger(SocialGamerPro.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        //closes the create account page and returns to login page
        btnExit.setOnAction((javafx.event.ActionEvent e) -> {
            forgotPasswordStage.close();

        });
    }
    
    public void changePasswordWindow(){

        TextField tfUsernameDisplay = new TextField();
        //TextField tfFirstName = new TextField();
        //TextField tfLastName = new TextField();
        //TextField tfEmail = new TextField();
        //TextField tfDob = new TextField();
        //DatePicker dpDob = new DatePicker();
        //TextArea tfBio = new TextArea();
        PasswordField tfPassword3 = new PasswordField();
        PasswordField tfPassword4 = new PasswordField();
        Label lbUsernameDisplay = new Label("Username:  ");
        Label lbPassword3 = new Label("Enter New Password:  ");
        Label lbPassword4 = new Label("Confirm New Password:  ");
        //Label lbFirstName = new Label("First Name");
        //Label lbLastName = new Label("Last Name");
        //Label lbEmail = new Label("What is your email?   ");
        //Label lbDob = new Label("Enter Birthday (ex 1990-01-31)");
        //Label lbBio = new Label("Bio");
        Button btnChangePassword = new Button("Change");
        Button btnExit = new Button("Exit");
        //Button btnBrowse = new Button("Browse");
        Label lbMessage = new Label("");
        //ImageView imgProfile = new ImageView(image);

        Stage changePasswordStage = new Stage();
        //sets title at top of window
        changePasswordStage.setTitle("Update Password Please");

        VBox vBox = new VBox();

        GridPane gridpane = new GridPane();
        // gridpane.getColumnConstraints().add(new ColumnConstraints(50));

        StackPane logoStackpane = new StackPane();
        imgViewLogo.setImage(imgLogo);
        imgViewLogo.setFitHeight(150);
        imgViewLogo.setPreserveRatio(true);
        logoStackpane.getChildren().add(imgViewLogo);
        logoStackpane.setPadding(new Insets(25));
        //imgViewLogo.set
        gridpane.add(lbUsernameDisplay, 0, 0);
        gridpane.add(tfUsernameDisplay, 1, 0);
        //gridpane.add(lbEmail, 0, 1);
        //gridpane.add(tfEmail, 1, 1);
        gridpane.add(lbPassword3, 0, 2);
        gridpane.add(tfPassword3, 1, 2);
        gridpane.add(lbPassword4, 0, 3);
        gridpane.add(tfPassword4, 1, 3);
        vBox.getChildren().addAll(gridpane, btnChangePassword, btnExit, lbMessage);

        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);

        //width, height of actual scene
        Scene changePasswordDashboard = new Scene(vBox, 400, 450);
        changePasswordDashboard.getStylesheets().add(SocialGamerPro.class.getResource("Login.css").toExternalForm());

        changePasswordStage.setScene(changePasswordDashboard);
//        createAccountStage.setMinHeight(650);
//        createAccountStage.setMinWidth(1100);
        //primaryStage.close();
		changePasswordStage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        changePasswordStage.setX((primScreenBounds.getWidth() - changePasswordStage.getWidth()) / 2);
        changePasswordStage.setY((primScreenBounds.getHeight() - changePasswordStage.getHeight()) / 2);
        changePasswordStage.show();
        //set background color to a light grey
//        vBox.setStyle("-fx-background-color: #DCDCDC;");

        btnChangePassword.setOnAction((javafx.event.ActionEvent e) -> {
            
//validates password using regex to require a number, a lowercase letter, an uppercase letter, a special character (!@#$%^&+=), and has to be 8 characters or more).
            Pattern p = Pattern.compile("((?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=]).{8,})");
            Matcher m = p.matcher(tfPassword3.getText());

//confirming that both password fields match            
            String pwd = tfPassword3.getText();
            String confpwd = tfPassword4.getText();

            if ( !m.matches()) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Error in Validating Fields");
                alert.setHeaderText(null);
                alert.setContentText("Please make sure your fields are not empty and that your password is 8 or more characters, has a number, a lowercase letter, an uppercase letter, and a special character.");
                alert.showAndWait();
            } else {
            //verifying that the password matches in order to update password
                if (pwd.equals(confpwd)) {

                    System.out.println("Updating password complete");
                    DBUtility dbSendEmail = new DBUtility();

                    try {
                        dbSendEmail.dbConnect();
                    } catch (SQLException ex) {
                        Logger.getLogger(SocialGamerPro.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    try {
                        dbSendEmail.updateEmail(tfUsernameDisplay.getText(), tfPassword3.getText());
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(SocialGamerPro.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        lbMessage.setText("Password Changed Successfully!!");
                        lbMessage.setStyle("-fx-text-fill: red; -fx-font-size: 12");
                        changePasswordStage.close();
                        dbSendEmail.dbClose();

                    } catch (SQLException ex) {
                        Logger.getLogger(SocialGamerPro.class.getName()).log(Level.SEVERE, null, ex);
                    }  
                } 
                else {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Error in Password Fields");
                    alert.setHeaderText(null);
                    alert.setContentText("Please make sure your passwords match.");
                    alert.showAndWait();
                }
            }
        });
        //closes the create account page and returns to login page
        btnExit.setOnAction((javafx.event.ActionEvent e) -> {
            changePasswordStage.close();

        });
    }
    
    public void loginError() {

        VBox errorVbox = new VBox();
        Label lbError = new Label("Wrong Username or Password!");

        errorVbox.getChildren()
                .add(lbError);
        errorVbox.setAlignment(Pos.CENTER);

        Scene loginErrorScene = new Scene(errorVbox, 200, 200);
        Stage loginErrorStage = new Stage();

        loginErrorStage.setTitle(
                "ALERT!");
        loginErrorStage.setScene(loginErrorScene);

        loginErrorStage.show();

    }

    public static void main(String[] args) {
        // TODO code application logic here
        launch(args);
    }
    
}
