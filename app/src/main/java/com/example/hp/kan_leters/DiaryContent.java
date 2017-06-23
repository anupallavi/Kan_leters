package com.example.hp.kan_leters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * Created by Hp on 5/17/2017.
 */

public class DiaryContent  extends AppCompatActivity implements TextToSpeech.OnInitListener,AdapterView.OnItemSelectedListener{
    TextView textView;
    Context context=this;
    DatabaseHelper myDB;
    ContentDatabase contentdb;
    Button save;
    ImageButton calendar,startRecognizer;
    private static final int RQS_RECOGNITION = 1;

    private static final String DB_NAME = "kan_let.db";
    EditText editText;
    TextToSpeech tts;

    String language;

    private static final HashSet<Character> vowels = new HashSet<Character>();

    //Initialize vowels hashSet to contain vowel characters
    static{
        vowels.add('a');
        vowels.add('e');
        vowels.add('i');
        vowels.add('o');
        vowels.add('u');
        vowels.add('A');
        vowels.add('E');
        vowels.add('I');
        vowels.add('O');
        vowels.add('U');
    }

    private static final HashSet<String> varNa = new HashSet<String>();
    static {
        varNa.add("kh");
        varNa.add("gh");
        varNa.add("ch");
        varNa.add("jh");
        varNa.add("th");
        varNa.add("dh");
        varNa.add("ph");
        varNa.add("bh");
        varNa.add("sh");
           }




    Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_content);


        Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.language_arrays, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // Notify the selected item text
                Toast.makeText
                        (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                        .show();
                language = String.valueOf(selectedItemText);
              //  language = String.valueOf(spinner.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        Bundle dateData = getIntent().getExtras();
        if(dateData == null) {
            return;
        }
        String dateSet = dateData.getString("Date");
        String content = dateData.getString("Content");

        textView = (TextView) findViewById(R.id.date);
        textView.setText(dateSet);
        calendar = (ImageButton) findViewById(R.id.calendarButton);
        startRecognizer = (ImageButton) findViewById(R.id.micButton);
        startRecognizer.setEnabled(false);
        editText = (EditText) findViewById(R.id.edittext);
        startRecognizer.setOnClickListener(startRecognizerOnClickListener);
        tts = new TextToSpeech(this, this);
        // myDB = new DatabaseHelper(this);
        myDB = DatabaseHelper.getInstance(this, DB_NAME);
        contentdb = new ContentDatabase(this);
        save = (Button) findViewById(R.id.save_button);
        editText.append(content);

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiaryContent.this, CalendarActivity.class);
                startActivity(intent);
            }
        });}


    public static boolean isVowel(Character c){

        return vowels.contains(c);
    }
    public static boolean isVarna(String s){

        return varNa.contains(s);
    }

    public void AddData(View view){
        String content = editText.getText().toString();
        String date = textView.getText().toString();
        contentdb = new ContentDatabase(context);
        SQLiteDatabase db = contentdb.getWritableDatabase();
        contentdb.addData(date,content,db);
        Toast.makeText(getBaseContext(),"saved",Toast.LENGTH_LONG).show();

    }


    private Button.OnClickListener startRecognizerOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View arg0) {






            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
          //  intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-IN");
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en-IN");
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,getString(R.string.speak));
            startActivityForResult(intent, RQS_RECOGNITION);


        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == RQS_RECOGNITION) && (resultCode == RESULT_OK)) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //String query = "select " + DatabaseHelper.COL2 + "from " + DatabaseHelper.TABLE_NAME + "where " +DatabaseHelper.COL1+ " = " +result;
            //DisplayStringArray.append(result.get(0));
            String eng = getString(R.string.english);
            String kan = getString(R.string.kannada);
            if (language.equals(eng)) {
                editText.append(result.get(0));
                editText.append(" ");



            } else if (language.equals(kan)) {

             //  String test = "chandra ";
              //  String vowel = "a|e|i|o|u";
               // String[] words = test.split(" ");
               String[] words = result.get(0).split(" ");
                for (String word : words) {
                    int i = 0, index = 0, len = 0;
                    String str = word;
                    List<String> splits = new ArrayList<String>();
                    //String[] splits = new String[0];
                    len = str.length();
                    char nextindex = 0;
                    char ch;
                    while (len != 0) {
                       // char ch = str.charAt(index);
                        do{
                            ch = str.charAt(index++);
                        }
                        while (!isVowel(ch));

                        if(index < len) {

                            nextindex = str.charAt(index);
                            if (isVowel(nextindex))

                            {
                                index = index + 1;
                                String subs = str.substring(0, index);
                                //splits[i] = subs;
                                splits.add(subs);
                                i++;
                                String repstr = str.substring(index, str.length());
                                //  str.replace(str, repstr);
                                str = repstr;
                                // str = str.substring(index + 1, str.length()+1);
                                len = str.length();
                                index = 0;
                            } else {
                                String subs = str.substring(0, index);
                              //  splits[i] = subs;
                                splits.add(subs);
                                i++;
                                String repstr = str.substring(index, str.length());
                                //   str.replace(str, repstr);
                                str = repstr;
                                // str = str.substring(index + 1, str.length()+1);
                                len = str.length();
                                index = 0;
                            }
                        }
                        else{
                            String subs = str.substring(0, index);
                           // splits[i] = subs;
                            splits.add(subs);
                            i++;
                            String repstr = str.substring(index, str.length());
                            //   str.replace(str, repstr);
                            str = repstr;
                            // str = str.substring(index + 1, str.length()+1);
                            len = str.length();
                            index = 0;
                        }
                    }

                        //  String[] splits = str.split("(?<=au|ai|ah|am|ru|oo|ii|uu|ee|aa|o|i|u|e|a)");

                        for (String sp : splits) {


                            Cursor cursor = DatabaseHelper.translate(sp);
                            //  String let = cursor.getString(0);
                            if (cursor.moveToNext()) {
                                editText.append(cursor.getString(0));

                            } else {

                                List<String> vatvol = new ArrayList<String>();
                                int indv = 0, count = 0, two = 2;
                                char vat;
                                //   sp = "ndra";

                                if (sp.startsWith("n")) {
                                    editText.append("O");
                                    vatvol.add("");
                                    sp = sp.substring(1, sp.length());
                                } else if (sp.startsWith("r")) {
                                    vatvol.add("೯");
                                    sp = sp.substring(1, sp.length());
                                } else {
                                    vatvol.add("");
                                }

                                do {
                                    vat = sp.charAt(indv++);
                                }
                                while (!isVowel(vat));

                                String subvat = sp.substring(indv - 1, sp.length());
                                vatvol.add(subvat);

                                String repvat = sp.substring(0, indv - 1);
                                sp = repvat;


                                while (sp.length() >= 2) {

                                    String varna = sp.substring(0, 2);
                                    Boolean var = isVarna(varna);
                                    if (var) {
                                        vatvol.add(varna);
                                        sp = sp.substring(2, sp.length());
                                    } else {
                                        varna = sp.substring(0, 1);
                                        vatvol.add(varna);
                                        sp = sp.substring(1, sp.length());
                                    }
                                }
                                if (sp.length() != 0) {
                                    vatvol.add(sp);
                                }
                                String conso = vatvol.get(2) + vatvol.get(1);

                                Cursor curcon = DatabaseHelper.translate(conso);
                                //  String let = cursor.getString(0);
                                if (curcon.moveToNext()) {
                                    editText.append(curcon.getString(0));


                                } else {
                                    editText.append(conso);


                                }
                                curcon.close();
                                int sj = 3;
                                int size = vatvol.size();
                                while (sj < size) {
                                    String vatu = vatvol.get(sj);
                                    sj = sj + 1;
                                    Cursor curvat = DatabaseHelper.translate(vatu);
                                    //  String let = cursor.getString(0);
                                    if (curvat.moveToNext()) {
                                        editText.append(curvat.getString(0));

                                    } else {
                                        editText.append(vatu);
                                    }
                                    curvat.close();
                                }
                                editText.append(vatvol.get(0));
                            }
                            cursor.close();

                        }
                        editText.append("  ");

                    }

            }

        }
    }


    @Override
    public void onInit(int status) {
        startRecognizer.setEnabled(true);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
