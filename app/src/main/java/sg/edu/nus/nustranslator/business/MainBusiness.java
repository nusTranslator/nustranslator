package sg.edu.nus.nustranslator.business;

import android.content.Context;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import sg.edu.nus.nustranslator.Configurations;
import sg.edu.nus.nustranslator.datatransportation.DataTransporter;
import sg.edu.nus.nustranslator.model.AppModel;
import sg.edu.nus.nustranslator.model.States;

/**
 * Created by Storm on 3/5/2015.
 */
public class MainBusiness {

    //attributes
    AppModel appModel = new AppModel();
    AudioRecord recorder;
    DataTransporter dataTransporter;
    Context context;

    //constructor
    public MainBusiness(Context context) {
        this.context = context;
        this.dataTransporter = new DataTransporter(appModel);
    }

    //Public methods
    public States changeState() {
        States state = appModel.getAppState();
        if (state == States.ACTIVE) {
            appModel.setAppState(States.INACTIVE);
            stopListen();
        } else {
            appModel.setAppState(States.ACTIVE);
            startListen();
        }
        return appModel.getAppState();
    }

    //Private Helper Methods
    private void startListen() {
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                Configurations.Recorder_sampleRate,
                Configurations.Recorder_channelConfig,
                Configurations.Recorder_audioFormat,
                Configurations.Recorder_minBuffSize);
        this.recorder.startRecording();
        this.dataTransporter.startAudioStream(recorder, this.context);
    }

    private void stopListen() {
        this.recorder.release();
        this.dataTransporter.stopStream();
    }

}
