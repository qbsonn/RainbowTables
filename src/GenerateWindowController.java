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
import javafx.scene.control.*;
import javafx.util.Duration;

/**
 * Created by Kuba on 2016-12-17.
 */
public class GenerateWindowController  {




    @FXML
    ProgressBar pogressBar;

    @FXML
    ProgressIndicator progressIndicator;

    @FXML
    Button endButton;

    @FXML
    Label infoLabel,timeLabel;

    Timeline timeLine;

    boolean stopClock;

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

    public void handleEndButtonAction (ActionEvent event)
    {
       Stage stage= (Stage)endButton.getScene().getWindow();
        stage.close();


    }



}
