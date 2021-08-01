/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ringerfx;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.Data;
import org.asteriskjava.manager.ManagerConnection;


/**
 * @author MelnikovAI
 */

@Data
public class FXMLDocumentController implements Initializable {
    private String login = "";
    private String password = "";
    private String title = "";
    private String idRequest = "";
    private String[] test;
    private String phone;

    private ManagerConnection managerConnection;
    @FXML
    private Button buttonInSystem;
    @FXML
    private TextField textFieldLogin;
    @FXML
    private Label labelInSystem;
    @FXML
    private TextField textFieldPassword;
    @FXML
    private ImageView imageViewPicLogo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void buttonActionEnter(ActionEvent event) throws SQLException, FileNotFoundException, UnsupportedEncodingException {

        RequestDb requestDb = new RequestDb();
        requestDb.connect();
        test = requestDb.checkUsers(textFieldLogin.getText());

        for (int i = 0; i < test.length; i++) {
            login = test[0];
            password = test[1];
            title = test[2];
            idRequest = test[3];
            phone = test[4];
        }

        DataTransferObject.loginTransfer = login;
        DataTransferObject.passwordTransfer = password;
        DataTransferObject.titleTransfer = title;
        DataTransferObject.idRequestTransfer = idRequest;
        DataTransferObject.phoneTransfer = phone;
        HelloManager.chanNumb = phone;

        if (textFieldPassword.getText().equals(password)) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WIndowSearch.fxml"));
                Parent rootSearch = (Parent) fxmlLoader.load();


                Stage stage = new Stage();
                stage.setTitle("SipDriver");
                stage.setScene((new Scene(rootSearch)));
                stage.show();
                stage.setResizable(false);
                stage = (Stage) buttonInSystem.getScene().getWindow();
                stage.close();

            } catch (Exception e) {
                System.out.println("Cant load second window");
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка входа");
            alert.setHeaderText(null);
            alert.setContentText("Логин или пароль не верен! Пожалуйста, попробуйте еще раз.");
            alert.showAndWait();
        }
    }

    public void handleEnter(KeyEvent keyEvent) throws SQLException {
        if(keyEvent.getCode()== KeyCode.ENTER){
            RequestDb requestDb = new RequestDb();
            requestDb.connect();
            test = requestDb.checkUsers(textFieldLogin.getText());

            for (int i = 0; i < test.length; i++) {
                login = test[0];
                password = test[1];
                title = test[2];
                idRequest = test[3];
                phone = test[4];
            }

            DataTransferObject.loginTransfer = login;
            DataTransferObject.passwordTransfer = password;
            DataTransferObject.titleTransfer = title;
            DataTransferObject.idRequestTransfer = idRequest;
            DataTransferObject.phoneTransfer = phone;
            HelloManager.chanNumb = phone;

            if (textFieldPassword.getText().equals(password)) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WIndowSearch.fxml"));
                    Parent rootSearch = (Parent) fxmlLoader.load();


                    Stage stage = new Stage();
                    stage.setTitle("SipDriver");
                    stage.setScene((new Scene(rootSearch)));
                    stage.show();
                    stage.setResizable(false);
                    stage = (Stage) buttonInSystem.getScene().getWindow();
                    stage.close();

                } catch (Exception e) {
                    System.out.println("Cant load second window");
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ошибка входа");
                alert.setHeaderText(null);
                alert.setContentText("Логин или пароль не верен! Пожалуйста, попробуйте еще раз.");
                alert.showAndWait();
            }
        }
    }
}

