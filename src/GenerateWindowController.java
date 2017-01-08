import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;

import javafx.scene.control.*;
import javafx.util.Duration;

/**
 * Kontroler okna podczas tworzenia tablicy
 */
public class GenerateWindowController  {




    @FXML
    ProgressBar pogressBar;

    @FXML
    ProgressIndicator progressIndicator;

    @FXML
    Button endButton, interruptButton;

    @FXML
    Label infoLabel,timeLabel;

    Timeline timeLine;

    boolean stopClock;

    Thread generate;

    public void initialize() {
        stopClock=false;
        timeLabel.setText("");
        endButton.setVisible(false);
        //timeLabel.setText("00:00:00");
        long startTime=System.currentTimeMillis();
       // System.out.println("Poczatkowy:"+startTime);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss") ;
        final Timeline timeline = new Timeline( new KeyFrame( Duration.millis( 500 ), event -> {
          if (stopClock==false)  {
              Calendar calendar = Calendar.getInstance();


              long diff = (System.currentTimeMillis() - startTime);

              calendar.set(Calendar.HOUR_OF_DAY, 0);
              calendar.set(Calendar.MINUTE, 0);
              calendar.set(Calendar.MILLISECOND, (int)diff);
              calendar.set(Calendar.SECOND, 0);
             // System.out.println("nowy"+diff);
                if (diff < 0) {

                    //timeLabel.setText( timeFormat.format( 0 ) );
                } else {

                   // timeLabel.setText(timeFormat.format(diff));
                    timeLabel.setText(timeFormat.format(calendar.getTime()));
                }
            }
        }));
        timeline.setCycleCount( Animation.INDEFINITE );
        timeline.play();
        timeLine=timeline;

    }

    /**
     * Metoda wywoływana przy naciśnieciu klawisza Zakończ
     * @param event
     */
    public void handleEndButtonAction (ActionEvent event)
    {
       Stage stage= (Stage)endButton.getScene().getWindow();
        stage.close();


    }

    /**
     * Metoda wywołana przy naciśnieciu przycisku Przerwij generacje tablicy
     * @param event
     */

    public void handleInterruptButtonAction (ActionEvent event)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Błąd");
        alert.setHeaderText(null);
        alert.setContentText("Czy napewno chcesz przerwać generacje tablicy?");

        ButtonType buttonTypeYes = new ButtonType("Tak");
        ButtonType buttonTypeNo = new ButtonType("Nie");

        alert.getButtonTypes().setAll(buttonTypeYes,buttonTypeNo);



        Optional<ButtonType> result =alert.showAndWait();

        if (result.get() == buttonTypeYes) {

            Stage stage= (Stage)endButton.getScene().getWindow();
            stage.close();
           System.exit(1);
        }

        }





}
