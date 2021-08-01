/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ringerfx;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Data;
import lombok.SneakyThrows;

/**
 * FXML Controller class
 *
 * @author MelnikovAI
 */
@Data
public class WIndowSearchController implements Initializable {

    @FXML
    private TextField textFieldSearch;
    @FXML
    private Button buttonSearch;
    @FXML
    private ListView<String> listViewSrc;
    @FXML
    private ListView<String> listViewFavorite;
    @FXML
    private Button buttonRing;
    @FXML
    private Button buttonAddFavorite;
    @FXML
    private Label nkLabel;
    @FXML
    private Label searchLabel;
    @FXML
    private Label favoriteNumbLabel;
    @FXML
    private Label labelInNumber;

    private ObservableList<String> numbers;
    private String buf = "";
    private int buf_id = 0;
    private HashMap<String, String> addList = new HashMap<>();
    private String resultSplit;
    private String ringMainNumber = "";
    private String ringMain2Number = "";
    private String ringInNumber = "";
    private int i = 0;


    /**
     * Initializes the controller class.
     */
    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nkLabel.setText(DataTransferObject.titleTransfer);
        labelInNumber.setText(DataTransferObject.phoneTransfer);
        RequestDb requestDb = new RequestDb();
        requestDb.connect();
        listViewFavorite.getItems().addAll(requestDb.fetchFavorite(DataTransferObject.idRequestTransfer));
    }
    @FXML
    //TODO create check database connect
    private void buttonSearchDriver(ActionEvent event) throws IOException {
        BaseAndJson baseAndJson = new BaseAndJson();
        baseAndJson.setEmplId(textFieldSearch.getText());
        baseAndJson.baseEncode();
        baseAndJson.sendJson();
        if (baseAndJson.checkResultSubString()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Информация");
            alert.setHeaderText(null);
            alert.setContentText("По табельному номеру (номер) мобильный телефон не найден. Попробуйте еще раз");
            alert.showAndWait();
        } else if (baseAndJson.checkResultSubStringUnrl()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Информация");
            alert.setHeaderText(null);
            alert.setContentText("Сервис Axapta недоступен! Попробуйте еще раз или обратитесь в службу технической поддержки");
            alert.showAndWait();
        } else {
            listViewSrc.getItems().clear();
            addList = baseAndJson.addListView(baseAndJson.getInputlineRes());
            resultSplit = addList.values().toString();
            listViewSrc.getItems().add(resultSplit);
            addList.clear();
        }
    }
    @FXML
    private void buttonRing(ActionEvent event) {

        if (!listViewSrc.getSelectionModel().isEmpty()) {

            String patternMobile = "[+][0-9]+";
            Pattern pMobile = Pattern.compile(patternMobile);
            Matcher mMobile = pMobile.matcher(listViewSrc.getSelectionModel().getSelectedItem());
            String patternInNumber = "\\b\\d{4}\\b";
            Pattern pInNumber = Pattern.compile(patternInNumber);
            Matcher mInNUmber = pInNumber.matcher(listViewSrc.getSelectionModel().getSelectedItem());

            if (mMobile.find()) {
                ringMainNumber = mMobile.group(0);
            }
            if (mInNUmber.find()) {
                ringInNumber = mInNUmber.group(0);
            }
            for (Matcher mMobile2 = pMobile.matcher(listViewSrc.getSelectionModel().getSelectedItem()); mMobile2.find(); i++) {
                if (i == 1) {
                    ringMain2Number = mMobile2.group();
                }
            }
            i = 0;

            if (!ringMain2Number.isEmpty()) {
                try {
                    Button btnMainRing = new Button(ringMainNumber);
                    Button btnMain2Ring = new Button(ringMain2Number);
                    Button btnInRing = new Button(ringInNumber);
                    Label routeRing = new Label("Маршрут звонка");

                    routeRing.setPrefWidth(130);
                    routeRing.setTranslateX(50);
                    routeRing.setTranslateY(15);
                    btnMainRing.setPrefWidth(115.0);
                    btnMainRing.setTranslateX(50.0);
                    btnMainRing.setTranslateY(50.0);
                    btnMain2Ring.setPrefWidth(115.0);
                    btnMain2Ring.setTranslateX(50.0);
                    btnMain2Ring.setTranslateY(100.0);
                    btnInRing.setPrefWidth(115.0);
                    btnInRing.setLayoutX(50.0);
                    btnInRing.setLayoutY(150.0);


                    Pane root = new Pane(btnMainRing, btnMain2Ring, btnInRing, routeRing);
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setTitle("Звонок");
                    stage.setWidth(224);
                    stage.setHeight(265);
                    stage.show();

                    btnMainRing.setOnAction(new EventHandler<ActionEvent>() {
                        @SneakyThrows
                        @Override
                        public void handle(ActionEvent event) {
                            HelloManager helloManager = new HelloManager();
                            helloManager.setNumb("8" + btnMainRing.getText().substring(2));
                            helloManager.run();

                        }
                    });

                    btnInRing.setOnAction(new EventHandler<ActionEvent>() {
                        @SneakyThrows
                        @Override
                        public void handle(ActionEvent event) {
                            HelloManager helloManager = new HelloManager();
                            helloManager.setNumb(btnInRing.getText());
                            helloManager.run();

                        }
                    });

                    btnMain2Ring.setOnAction(new EventHandler<ActionEvent>() {
                        @SneakyThrows
                        @Override
                        public void handle(ActionEvent event) {
                            HelloManager helloManager = new HelloManager();
                            helloManager.setNumb("8" + btnMain2Ring.getText().substring(2));
                            helloManager.run();

                        }
                    });

                } catch (Exception e) {
                    System.out.println("Cant load window");
                }
            } else {
                try {

                    HelloManager helloManager = new HelloManager();
                    helloManager.setNumb("8" + ringMainNumber.substring(2));
                    helloManager.run();

                } catch (Exception e) {
                    System.out.println("Cant load window");
                }
            }
            listViewSrc.getSelectionModel().clearSelection();
        } else {

            String patternMobile = "[+][0-9]+";
            Pattern pMobile = Pattern.compile(patternMobile);
            Matcher mMobile = pMobile.matcher(listViewFavorite.getSelectionModel().getSelectedItem());
            String patternInNumber = "\\b\\d{4}\\b";
            Pattern pInNumber = Pattern.compile(patternInNumber);
            Matcher mInNUmber = pInNumber.matcher(listViewFavorite.getSelectionModel().getSelectedItem());

            if (mMobile.find()) {
                ringMainNumber = mMobile.group(0);
            }
            if (mInNUmber.find()) {
                ringInNumber = mInNUmber.group(0);
            }
            for (Matcher mMobile2 = pMobile.matcher(listViewFavorite.getSelectionModel().getSelectedItem()); mMobile2.find(); i++) {
                if (i == 1) {
                    ringMain2Number = mMobile2.group();
                }
            }
            i = 0;

            if (!ringMain2Number.isEmpty()) {
                try {
                    Button btnMainRing = new Button(ringMainNumber);
                    Button btnMain2Ring = new Button(ringMain2Number);
                    Button btnInRing = new Button(ringInNumber);
                    Label routeRing = new Label("Маршрут звонка");

                    routeRing.setPrefWidth(130);
                    routeRing.setTranslateX(50);
                    routeRing.setTranslateY(15);
                    btnMainRing.setPrefWidth(115.0);
                    btnMainRing.setTranslateX(50.0);
                    btnMainRing.setTranslateY(50.0);
                    btnMain2Ring.setPrefWidth(115.0);
                    btnMain2Ring.setTranslateX(50.0);
                    btnMain2Ring.setTranslateY(100.0);
                    btnInRing.setPrefWidth(115.0);
                    btnInRing.setLayoutX(50.0);
                    btnInRing.setLayoutY(150.0);


                    Pane root = new Pane(btnMain2Ring, btnMainRing, btnInRing, routeRing);
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setTitle("Звонок");
                    stage.setWidth(224);
                    stage.setHeight(265);
                    stage.show();

                    btnMainRing.setOnAction(new EventHandler<ActionEvent>() {
                        @SneakyThrows
                        @Override
                        public void handle(ActionEvent event) {
                            HelloManager helloManager = new HelloManager();
                            helloManager.setNumb("8" + btnMainRing.getText().substring(2));
                            helloManager.run();
                        }
                    });

                    btnInRing.setOnAction(new EventHandler<ActionEvent>() {
                        @SneakyThrows
                        @Override
                        public void handle(ActionEvent event) {
                            HelloManager helloManager = new HelloManager();
                            helloManager.setNumb(btnInRing.getText());
                            helloManager.run();

                        }
                    });

                    btnMain2Ring.setOnAction(new EventHandler<ActionEvent>() {
                        @SneakyThrows
                        @Override
                        public void handle(ActionEvent event) {
                            System.out.println("=============>" + ringMain2Number);
                            HelloManager helloManager = new HelloManager();
                            helloManager.setNumb("8" + btnMain2Ring.getText().substring(2));
                            helloManager.run();

                        }
                    });

                } catch (Exception e) {
                    System.out.println("Cant load window");
                }
            } else {
                try {
                    HelloManager helloManager = new HelloManager();
                    helloManager.setNumb("8" + ringMainNumber.substring(2));
                    helloManager.run();

                } catch (Exception e) {
                    System.out.println("Cant load window");
                }
            }
        }
        listViewFavorite.getSelectionModel().clearSelection();
        ringMainNumber = "";
        ringMain2Number = "";
        ringInNumber = "";
    }
    @FXML
    private void buttonAddFavorite(ActionEvent event) throws SQLException, FileNotFoundException, UnsupportedEncodingException {
        RequestDb requestDb = new RequestDb();
        buf = listViewSrc.getSelectionModel().getSelectedItem();
        if (listViewFavorite.getItems().contains(buf)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Информация");
            alert.setHeaderText(null);
            alert.setContentText("Такой номер уже существует");
            alert.showAndWait();
        } else {
            listViewFavorite.getItems().add(buf);
            requestDb.connect();
            requestDb.insertDataToDb(buf, DataTransferObject.idRequestTransfer);
        }
    }
    public void handle(KeyEvent event) throws SQLException, FileNotFoundException, UnsupportedEncodingException {
        if (event.getCode() == KeyCode.DELETE) {
            RequestDb requestDb = new RequestDb();
            requestDb.connect();
            requestDb.deleteDataToDb(listViewFavorite.getSelectionModel().getSelectedItem(), DataTransferObject.idRequestTransfer);
            listViewFavorite.getItems().remove(listViewFavorite.getSelectionModel().getSelectedIndex());
        }
    }
    public void setChangeChanel(ActionEvent actionEvent) {
        TextField textField1 = new TextField();
        Button button2 = new Button("Изменить");
        button2.setPrefWidth(91.0);
        button2.setTranslateX(20.0);
        button2.setTranslateY(40.0);
        textField1.setPrefWidth(91.0);
        textField1.setTranslateX(20.0);
        textField1.setTranslateY(10.0);

        Pane root = new Pane(textField1, button2);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Канал");
        stage.setHeight(113.0);
        stage.setWidth(142.0);
        stage.show();

        button2.setOnAction(new EventHandler<ActionEvent>() {
            @SneakyThrows
            @Override
            public void handle(ActionEvent event) {
                if (textField1.getText().length() == 4 && textField1.getText().matches("\\b\\d{4}\\b")) {
                    labelInNumber.setText(textField1.getText());
                    HelloManager.chanNumb = textField1.getText();
                    stage.close();
                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Информация");
                    alert.setHeaderText(null);
                    alert.setContentText("Ошибка ввода! Введите пожалуйста 4-х значеный номер");
                    alert.showAndWait();
                }
            }
        });
    }

    public void handleEnter(KeyEvent keyEvent) throws IOException {
        System.out.println("In method");
        if(keyEvent.getCode()==KeyCode.ENTER){
            BaseAndJson baseAndJson = new BaseAndJson();
            baseAndJson.setEmplId(textFieldSearch.getText());
            baseAndJson.baseEncode();
            baseAndJson.sendJson();
            if (baseAndJson.checkResultSubString()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Информация");
                alert.setHeaderText(null);
                alert.setContentText("По табельному номеру (номер) мобильный телефон не найден. Попробуйте еще раз");
                alert.showAndWait();
            } else if (baseAndJson.checkResultSubStringUnrl()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Информация");
                alert.setHeaderText(null);
                alert.setContentText("Сервис Axapta недоступен! Попробуйте еще раз или обратитесь в службу технической поддержки");
                alert.showAndWait();
            } else {
                listViewSrc.getItems().clear();
                addList = baseAndJson.addListView(baseAndJson.getInputlineRes());
                resultSplit = addList.values().toString();
                listViewSrc.getItems().add(resultSplit);
                addList.clear();
            }
        }
    }
}

