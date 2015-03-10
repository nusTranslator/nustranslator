package sg.edu.nus.nustranslator.business;

import android.content.Context;

import java.io.File;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import sg.edu.nus.nustranslator.Configurations;

import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

/**
 * Created by Storm on 3/10/2015.
 */
class LocalSpeechRecognizer implements ISpeechRecognizer, RecognitionListener {

    private SpeechRecognizer recognizer;
    private Context context;
    private MainBusiness parent;

    public LocalSpeechRecognizer(final Context context, MainBusiness parent) {
        this.context = context;
        this.parent = parent;
        setupRecognizer(context);
    }

    @Override
    public void startListen() {
        this.recognizer.startListening(Configurations.Sphinx_keyword_search);
    }

    @Override
    public void stopListen() {
        this.recognizer.stop();
    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            this.parent.onSpeechRecognitionResultUpdate(text);
        }
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
    }

    //Private Helper Methods
    private void setupRecognizer(Context context) {
        try {
            Assets assets = new Assets(context);
            File assetDir = assets.syncAssets();
            File modelsDir = new File(assetDir, Configurations.Sphinx_models_dir);
            this.recognizer = defaultSetup()
                    .setAcousticModel(new File(modelsDir, Configurations.Sphinx_acousticModel_dir))
                    .setDictionary(new File(modelsDir, Configurations.Sphinx_dictionary_dir))
                    .setRawLogDir(assetDir)
                    .setKeywordThreshold(Configurations.Sphinx_keywordThreshold)
                    .getRecognizer();
            this.recognizer.addListener(this);

            // Create language model search.
            File languageModel = new File(modelsDir, Configurations.Sphinx_languageModel_dir);
            recognizer.addNgramSearch(Configurations.Sphinx_keyword_search, languageModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
