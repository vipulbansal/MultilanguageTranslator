package com.vipulsoftwares.speechtotext;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Handler;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import org.angmarch.views.NiceSpinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainActivity extends AppCompatActivity implements OnInitListener {
    android.support.v7.widget.Toolbar toolbar;
    private TextToSpeech tts;
    ImageView help;
    ImageButton stt;
    protected static final int RESULT_SPEECH = 1;
    Button select;
    String lanSelected;
    AlertDialog actions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        final EditText t=(EditText) findViewById(R.id.etUserText);
        lanSelected="";

        DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Delete
                        lanSelected="Hindi";
                        select.setText("To Hindi");
                        break;
                    case 1: // Copy
                        lanSelected="Chinese";
                        select.setText("To Chinese");
                        break;
                    case 2: // Edit
                        lanSelected="French";
                        select.setText("To French");
                        break;
                    case 3: // Edit
                        lanSelected="Spanish";
                        select.setText("To Spanish");
                        break;
                    case 4: // Edit
                        lanSelected="Arabic";
                        select.setText("To Arabic");
                        break;
                    default:
                        lanSelected="";
                        select.setText("Select translation language");
                        break;
                }
            }
        };
        String [] lan=getResources().getStringArray(R.array.languages);
        final List<String> lans= Arrays.asList(lan);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Translation Language");
        builder.setItems(lan, actionListener);
        builder.setNegativeButton("Cancel", null);
        actions = builder.create();
        select=(Button)findViewById(R.id.optSelect);

        select.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                 actions.show();
            }
        });



        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        help=(ImageView)toolbar.findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog1 = new Dialog(MainActivity.this, R.style.DialogSlideAnim1);
                dialog1.setContentView(R.layout.dialog_help);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog1.show();

            }
        });

        ((ImageButton)toolbar.findViewById(R.id.btnSpeak)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    t.setText("");
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Opps! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });





        tts = new TextToSpeech(this, this);
//        ((Button) findViewById(R.id.bSpeak)).setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//              //  speakOut(((TextView) findViewById(R.id.tvTranslatedText)).getText().toString());
//                setContentView(R.layout.main);
//            }
//        });



        ((Button) findViewById(R.id.bTranslate)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                ((Button) findViewById(R.id.bTranslate)).setText("Translating...");


                    class bgStuff extends AsyncTask<String, Void, Void> {

                                String translatedText = "";
                                String text = "";

                                public bgStuff(String viewById) {
                                    text = viewById;
                                }

                                @Override
                                protected Void doInBackground(String... params) {
                                    // TODO Auto-generated method stub
                                    try {


                                        //            ((EditText) findViewById(R.id.etUserText)).getText().toString();
                                        translatedText = translate(text);

                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                        translatedText = "Select a translation language";

                                    }

                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void result) {
                                    // TODO Auto-generated method stub
                                    ((TextView) findViewById(R.id.tvTranslatedText)).setText(translatedText);
                                    super.onPostExecute(result);
                                    ((Button) findViewById(R.id.bTranslate)).setText("Translate");
                                    if(translatedText.equalsIgnoreCase("kuta"))
                                    {
                                        ((TextView) findViewById(R.id.tvTranslatedText)).setText("");
                                        new AlertDialog.Builder(MainActivity.this)
                                                .setTitle("Select Language")
                                                .setMessage("No translation language selected")
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }

                                                })

                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();
                                    }
                                }

                            }

                            new bgStuff(t.getText().toString()).execute();
                        }
            });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final EditText t=(EditText) findViewById(R.id.etUserText);
        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    t.setText(text.get(0));
                }
                break;
            }

        }
    }

    public String translate(String text) throws Exception{


        // Set the Client ID / Client Secret once per JVM. It is set statically and applies to all services
        Translate.setClientId("VipulBansalTranslator"); //Change this
        Translate.setClientSecret("ulSU9QeqMfACRLA3S+3uMyYmwnzfrUMGxzliYZXqqA0="); //change


        String translatedText = "";

        switch(lanSelected)
        {
            case "Hindi":
                translatedText = Translate.execute(text,Language.HINDI);
                return translatedText;
            case "Chinese":
                translatedText = Translate.execute(text,Language.CHINESE_TRADITIONAL);
                return translatedText;
            case "French":
                translatedText = Translate.execute(text,Language.FRENCH);
                return translatedText;
            case "Spanish":
                translatedText = Translate.execute(text,Language.SPANISH);
                return translatedText;

            case "Arabic":
                translatedText = Translate.execute(text,Language.ARABIC);
                return translatedText;

            default:
                return "kuta";
        }

    }

    @Override
    public void onInit(int status) {
        // TODO Auto-generated method stub
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.ENGLISH  );

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {

                //speakOut("Ich");
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void speakOut(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

}