package com.michael.readablecolors;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {

    PopupWindow aboutPopupWindow;
    Button aboutOKButton;

    PopupWindow pw;
    Button popupOKButton;
    RadioGroup contrastRadioGroup;


    RadioGroup sortRadioGroup;

    int sortCode0;
    int sortCode1;
    int sortCode2;
    int sortCode3;

    TextView appHeader;

    ImageButton helpImageButton;
    ImageButton menuImageButton;

    GridLayout colorGridLayout;

    RadioGroup textColorRadioGroup;
    RadioButton textRadioButton1;
    RadioButton textRadioButton2;


    RadioGroup infoColorRadioGroup;
    RadioButton infoRadioButton1;
    RadioButton infoRadioButton2;
    RadioButton infoRadioButton3;


    TextView seekBarTextView1;
    TextView seekBarTextView2;
    TextView seekBarTextView3;

    SeekBar seekBar;

    int currentProgress;
    float progressScaleFactor;

    float minContrastRatio;

    int screenPixelWidth;
    int screenPixelHeight;

    float TEXT_SIZE_FACTOR;
    final int COLORS_DISPLAY = 68;
    static float[][] arrayHSVColors = new float[7400][4];


    int count;

    String colorR;
    String colorG;
    String colorB;
    double contrastRatio;

    DecimalFormat decimalFormat = new DecimalFormat("#.00");


    final double RGB_THRESHOLD = 0.03928;
    final double RGB_NUMERATOR_1 = 12.92;
    final double RGB_OFFSET = 0.055;
    final double RGB_NUMERATOR_2 = 1.055;
    final double RGB_EXPONENT = 2.4;

    final double R_FACTOR = 0.2126;
    final double G_FACTOR = 0.7152;
    final double B_FACTOR = 0.0722;

    final double RGB_MAX = 255;


    DBHelper dbHelper;

    private Settings settings =  new Settings(0, 255, 255, 255, 0, 1, 1, 0); //default settings


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        screenPixelWidth = getResources().getDisplayMetrics().widthPixels;
        screenPixelHeight = getResources().getDisplayMetrics().heightPixels;
        float xdpi = getResources().getDisplayMetrics().xdpi;

        if(screenPixelWidth > screenPixelHeight){
            int temp = screenPixelWidth;
            screenPixelWidth = screenPixelHeight;
            screenPixelHeight = temp;
        }

        float screenInchesHeight = screenPixelHeight / xdpi;
        if(screenInchesHeight < 7.7f) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        float screenWidthDPI = getResources().getDisplayMetrics().xdpi;
        float ratio = screenPixelWidth / screenWidthDPI;

        colorGridLayout = (GridLayout) findViewById(R.id.colorGridLayout);
        appHeader = (TextView) findViewById(R.id.appHeader);

        helpImageButton = (ImageButton) findViewById(R.id.helpImageButton);
        helpImageButton.setOnClickListener(HelpImageButtonHandler);

        menuImageButton = (ImageButton) findViewById(R.id.menuImageButton);
        menuImageButton.setOnClickListener(MoreOptionsButtonHandler);

        textColorRadioGroup = (RadioGroup) findViewById(R.id.textColorRadioGroup);
        textColorRadioGroup.setOnCheckedChangeListener(TextColorRadioGroupHandler);

        textRadioButton1 = (RadioButton) findViewById(R.id.textRadioButton1);

        textRadioButton2 = (RadioButton) findViewById(R.id.textRadioButton2);

        infoColorRadioGroup = (RadioGroup) findViewById(R.id.infoColorRadioGroup);
        infoColorRadioGroup.setOnCheckedChangeListener(InfoColorRadioGroupHandler);

        infoRadioButton1 = (RadioButton) findViewById(R.id.infoRadioButton1);

        infoRadioButton2 = (RadioButton) findViewById(R.id.infoRadioButton2);

        infoRadioButton3 = (RadioButton) findViewById(R.id.infoRadioButton3);


        seekBarTextView1 = (TextView) findViewById(R.id.seekBarTextView1);
        seekBarTextView2 = (TextView) findViewById(R.id.seekBarTextView2);
        seekBarTextView3 = (TextView) findViewById(R.id.seekBarTextView3);


        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(SeekBarHandler);


        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            TEXT_SIZE_FACTOR = 0.012f;
            for (int i = 0; i < colorGridLayout.getChildCount(); i++) {
                colorGridLayout.getChildAt(i).setMinimumWidth((int) ((screenPixelHeight - 16 * ratio) * 0.25));
                colorGridLayout.getChildAt(i).setMinimumHeight((int) ((screenPixelWidth - 24 * ratio) * 0.032));
                ((TextView) colorGridLayout.getChildAt(i)).setTextSize(0, screenPixelHeight * (TEXT_SIZE_FACTOR));
            }
            appHeader.setTextSize(0, (screenPixelHeight * (TEXT_SIZE_FACTOR * 1.5f)));
            textRadioButton1.setTextSize(0, (screenPixelHeight * (TEXT_SIZE_FACTOR)));
            textRadioButton2.setTextSize(0, (screenPixelHeight * (TEXT_SIZE_FACTOR)));
            infoRadioButton1.setTextSize(0, (screenPixelHeight * (TEXT_SIZE_FACTOR)));
            infoRadioButton2.setTextSize(0, (screenPixelHeight * (TEXT_SIZE_FACTOR)));
            infoRadioButton3.setTextSize(0, (screenPixelHeight * (TEXT_SIZE_FACTOR)));
            seekBarTextView1.setTextSize(0, screenPixelHeight * (TEXT_SIZE_FACTOR * 1.3f));
            seekBarTextView2.setTextSize(0, screenPixelHeight * (TEXT_SIZE_FACTOR * 1.3f));
            seekBarTextView3.setTextSize(0, screenPixelHeight * (TEXT_SIZE_FACTOR * 1.3f));
        }else {
            TEXT_SIZE_FACTOR = 0.033f;
            for (int i = 0; i < colorGridLayout.getChildCount(); i++) {
                colorGridLayout.getChildAt(i).setMinimumWidth((int) ((screenPixelWidth - 16 * ratio) * 1 / 4));
                colorGridLayout.getChildAt(i).setMinimumHeight((int) ((screenPixelHeight - 24 * ratio) * 1 / 30));
                ((TextView) colorGridLayout.getChildAt(i)).setTextSize(0, screenPixelWidth * TEXT_SIZE_FACTOR);
            }
            appHeader.setTextSize(0, (screenPixelWidth * (TEXT_SIZE_FACTOR * 1.5f)));
            textRadioButton1.setTextSize(0, (screenPixelWidth * (TEXT_SIZE_FACTOR)));
            textRadioButton2.setTextSize(0, (screenPixelWidth * (TEXT_SIZE_FACTOR)));
            infoRadioButton1.setTextSize(0, (screenPixelWidth * (TEXT_SIZE_FACTOR)));
            infoRadioButton2.setTextSize(0, (screenPixelWidth * (TEXT_SIZE_FACTOR)));
            infoRadioButton3.setTextSize(0, (screenPixelWidth * (TEXT_SIZE_FACTOR)));
            seekBarTextView1.setTextSize(0, screenPixelWidth * (TEXT_SIZE_FACTOR * 1.3f));
            seekBarTextView2.setTextSize(0, screenPixelWidth * (TEXT_SIZE_FACTOR * 1.3f));
            seekBarTextView3.setTextSize(0, screenPixelWidth * (TEXT_SIZE_FACTOR * 1.3f));
        }



        dbHelper = new DBHelper(getApplicationContext());

        if(!dbHelper.isDatabase(getApplicationContext())){
            long settingsId = dbHelper.insertSettings(settings);
            settings.setId(settingsId);
        }else{
            settings = dbHelper.getSettings();
        }


        switch(settings.getContrastCode()){
            case 0: minContrastRatio = 3.0f; break;
            case 1: minContrastRatio = 4.5f; break;
            case 2: minContrastRatio = 7.0f; break;
            default: break;
        }

        ((RadioButton)textColorRadioGroup.getChildAt(settings.getTextColorCode())).setChecked(true);
        ((RadioButton)infoColorRadioGroup.getChildAt(settings.getInfoColorCode())).setChecked(true);


        recreateAllForNewSettings();
    }






    public double luminance(double valueR, double valueG, double valueB){
        double sRBG_R = valueR / RGB_MAX;
        double sRGB_R_base = (sRBG_R + RGB_OFFSET) / RGB_NUMERATOR_2;
        double sRBG_G = valueG / RGB_MAX;
        double sRGB_G_base = (sRBG_G + RGB_OFFSET) / RGB_NUMERATOR_2;
        double sRBG_B = valueB / RGB_MAX;
        double sRGB_B_base = (sRBG_B + RGB_OFFSET) / RGB_NUMERATOR_2;

        double R = sRBG_R <= RGB_THRESHOLD ? sRBG_R / RGB_NUMERATOR_1 : Math.pow(sRGB_R_base, RGB_EXPONENT);

        double G = sRBG_G <= RGB_THRESHOLD ? sRBG_G / RGB_NUMERATOR_1 : Math.pow(sRGB_G_base, RGB_EXPONENT);

        double B = sRBG_B <= RGB_THRESHOLD ? sRBG_B / RGB_NUMERATOR_1 : Math.pow(sRGB_B_base, RGB_EXPONENT);

        double luminance = R_FACTOR * R + G_FACTOR * G + B_FACTOR * B;
        return luminance;
    }


    //minimum contrastRatio is 7:1 (https://www.w3.org/TR/WCAG20-TECHS/G17.html)
    public double contrastRatio(double textR, double textG, double textB, double backgroundR, double backgroundG, double backgroundB){

        double contrastRatio = (luminance(textR, textG, textB) + 0.05) / (luminance(backgroundR, backgroundG, backgroundB) + 0.05);
        contrastRatio = contrastRatio < 1 ? 1 / contrastRatio : contrastRatio;
        return contrastRatio;
    }


    private void makeHexCode(int progress){
        int color;
        for(int i = 0; i < COLORS_DISPLAY; i++){
            color = Color.HSVToColor(arrayHSVColors[progress * COLORS_DISPLAY + i]);
            colorR = Integer.toHexString(Color.red(color)).length() == 2 ? Integer.toHexString(Color.red(color)) : "0" + Integer.toHexString(Color.red(color));
            colorG = Integer.toHexString(Color.green(color)).length() == 2 ? Integer.toHexString(Color.green(color)) : "0" + Integer.toHexString(Color.green(color));
            colorB = Integer.toHexString(Color.blue(color)).length() == 2 ? Integer.toHexString(Color.blue(color)) : "0" + Integer.toHexString(Color.blue(color));

            ((TextView)(colorGridLayout.getChildAt(i))).setText("#" + colorR.toUpperCase() + colorG.toUpperCase() + colorB.toUpperCase());
        }
    }

    private void makeRGBCode(int progress){
        int color;
        for(int i = 0; i < COLORS_DISPLAY; i++){
            color = Color.HSVToColor(arrayHSVColors[progress * COLORS_DISPLAY + i]);
            colorR = String.valueOf(Color.red(color));
            colorG = String.valueOf(Color.green(color));
            colorB = String.valueOf(Color.blue(color));

            ((TextView)(colorGridLayout.getChildAt(i))).setText(colorR + "," + colorG + "," + colorB);
        }
    }

    private void makeContrastRatio(int progress){
        int color;
        double backgroundR;
        double backgroundG;
        double backgroundB;
        for(int i = 0; i < COLORS_DISPLAY; i++){
            color = Color.HSVToColor(arrayHSVColors[progress * COLORS_DISPLAY + i]);
            backgroundR = Color.red(color);
            backgroundG = Color.green(color);
            backgroundB = Color.blue(color);
            contrastRatio = contrastRatio(settings.getTextR(),settings.getTextG(),settings.getTextB(), backgroundR, backgroundG, backgroundB);

            ((TextView)(colorGridLayout.getChildAt(i))).setText(decimalFormat.format(contrastRatio));
        }
    }

    private void makeBackgroundColor(int progress){
        int color;
        for(int i = 0; i < COLORS_DISPLAY; i++){
            color = Color.HSVToColor(arrayHSVColors[progress * COLORS_DISPLAY + i]);
            colorR = Integer.toHexString(Color.red(color)).length() == 2 ? Integer.toHexString(Color.red(color)) : "0" + Integer.toHexString(Color.red(color));
            colorG = Integer.toHexString(Color.green(color)).length() == 2 ? Integer.toHexString(Color.green(color)) : "0" + Integer.toHexString(Color.green(color));
            colorB = Integer.toHexString(Color.blue(color)).length() == 2 ? Integer.toHexString(Color.blue(color)) : "0" + Integer.toHexString(Color.blue(color));

            colorGridLayout.getChildAt(i).setBackgroundColor(Color.parseColor("#"  + colorR + colorG + colorB));
        }
    }

    private void makeTextColor(int textR, int textG, int textB){
        for(int i = 0; i < COLORS_DISPLAY; i++) {
            ((TextView) (colorGridLayout.getChildAt(i))).setTextColor(Color.rgb(textR, textG, textB));
        }
    }


    private void makeColorArray(){

        for(int i = 0; i < arrayHSVColors.length; i++){
            for(int j = 0; j < 4; j++){
                arrayHSVColors[i][j] = 0.0f;
            }
        }

        count = 0;
        for(short i = 0; i <= 255; i += 15){
            for(short j = 0; j <= 255; j += 5){
                for(short k = 0; k <= 255; k += 30){
                    contrastRatio = contrastRatio(settings.getTextR(),settings.getTextG(),settings.getTextB(), i,j,k);
                    if(contrastRatio >= minContrastRatio){
                        Color.colorToHSV(Color.rgb(i,j,k), arrayHSVColors[count]);
                        arrayHSVColors[count][3] = (float)contrastRatio;
                        count++;
                    }
                }
            }
        }
    }


    private void sortColorArray(final int sortCode){
        switch(sortCode){
            case 0: sortCode0 = 0; sortCode1 = 2; sortCode2 = 1; sortCode3 = 3; break;
            case 1: sortCode0 = 1; sortCode1 = 2; sortCode2 = 0; sortCode3 = 3; break;
            case 2: sortCode0 = 2; sortCode1 = 1; sortCode2 = 0; sortCode3 = 3; break;
            case 3: sortCode0 = 3; sortCode1 = 1; sortCode2 = 2; sortCode3 = 0; break;
            default: break;
        }

        java.util.Arrays.sort(arrayHSVColors, new java.util.Comparator<float[]>() {
            public int compare(float [] a, float [] b) {
                if(a[sortCode0] < b[sortCode0]){
                    return 1;
                }else if(a[sortCode0] > b[sortCode0]){
                    return -1;
                }else if(a[sortCode1] < b[sortCode1]){
                    return 1;
                }else if(a[sortCode1] > b[sortCode1]){
                    return -1;
                }else if(a[sortCode2] < b[sortCode2]){
                    return 1;
                }else if(a[sortCode2] > b[sortCode2]){
                    return -1;
                }else if(a[sortCode3] < b[sortCode3]){
                    return 1;
                }else if(a[sortCode3] > b[sortCode3]){
                    return -1;
                }
                return 0;
            }
        });
    }


    private void setProgressScaleFactor(){
        int textColorCode = settings.getTextColorCode();
        if(textColorCode == 0 && minContrastRatio == 3.0f){
            progressScaleFactor = 0.7f;
        }else if(textColorCode == 1 && minContrastRatio == 3.0f){
            progressScaleFactor = 1.0f;
        }else if(textColorCode == 0 && minContrastRatio == 4.5f){
            progressScaleFactor = 0.44f;
        }else if(textColorCode == 1 && minContrastRatio == 4.5f){
            progressScaleFactor = 0.81f;
        }else if(textColorCode == 0 && minContrastRatio == 7.0f){
            progressScaleFactor = 0.23f;
        }else if(textColorCode == 1 && minContrastRatio == 7.0f){
            progressScaleFactor = 0.535f;
        }
    }


    private void showColorInfo(){
        switch(settings.getInfoColorCode()){
            case 0: makeHexCode(currentProgress); break;
            case 1: makeRGBCode(currentProgress); break;
            case 2: makeContrastRatio(currentProgress); break;
            default: break;
        }
    }


    private void recreateAllForNewSettings(){

        makeColorArray();
        sortColorArray(settings.getSortCode());
        setProgressScaleFactor();
        currentProgress = (int)(settings.getSeekbarPosition() * progressScaleFactor);
        seekBar.setProgress(settings.getSeekbarPosition());
        makeBackgroundColor(currentProgress);
        makeTextColor(settings.getTextR(), settings.getTextG(), settings.getTextB());
        showColorInfo();
    }


    View.OnClickListener HelpImageButtonHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            initiateAboutWindow();
        }
    };


    RadioGroup.OnCheckedChangeListener TextColorRadioGroupHandler = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            View checkedRadioButton = group.findViewById(checkedId);
            int textColorCode = group.indexOfChild(checkedRadioButton);

            int textR = 0;
            int textG = 0;
            int textB = 0;
            switch(textColorCode){
                case 0: textR = 255; textG = 255; textB = 255; break;
                case 1: break;
                default: break;
            }

            settings.setTextColorCode(textColorCode);
            settings.setTextR(textR);
            settings.setTextG(textG);
            settings.setTextB(textB);

            recreateAllForNewSettings();
        }
    };


    View.OnClickListener MoreOptionsButtonHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            initiatePopupWindow();
        }
    };

    RadioGroup.OnCheckedChangeListener InfoColorRadioGroupHandler = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            View checkedRadioButton = group.findViewById(checkedId);
            int infoColorCode = group.indexOfChild(checkedRadioButton);
            settings.setInfoColorCode(infoColorCode);

            showColorInfo();
        }
    };



    SeekBar.OnSeekBarChangeListener SeekBarHandler = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int seekBarPosition = progress;
            settings.setSeekbarPosition(seekBarPosition);

            currentProgress = (int)(progress * progressScaleFactor);
            makeBackgroundColor(currentProgress);
            showColorInfo();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };



    private void initiatePopupWindow() {
        if(pw != null){
            pw.dismiss();
        }

        try {
            LayoutInflater inflater = LayoutInflater.from(this);
            View layout = inflater.inflate(R.layout.popup_menu_layout, (ViewGroup) findViewById(R.id.popupElement));

            pw = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);


            pw.showAtLocation(layout, Gravity.CLIP_VERTICAL, 0, 0);

            pw.getContentView().setEnabled(true);
            pw.getContentView().setOnClickListener(CancelPopupElementHandler);

            TextView headerTextView = (TextView) pw.getContentView().findViewById(R.id.headerTextView);
            headerTextView.setTextSize(0, (screenPixelWidth * 0.05f));

            popupOKButton = (Button) pw.getContentView().findViewById(R.id.popupOKButton);
            popupOKButton.setOnClickListener(PopupOKButtonHandler);
            popupOKButton.setTextSize(0, (screenPixelWidth * 0.04f));

            TextView textView1 = (TextView) pw.getContentView().findViewById(R.id.textView1);
            textView1.setTextSize(0, (screenPixelWidth * 0.04f));
            TextView textView2 = (TextView) pw.getContentView().findViewById(R.id.textView2);
            textView2.setTextSize(0, (screenPixelWidth * 0.04f));

            RadioButton contrastRadioButton1 = (RadioButton) pw.getContentView().findViewById(R.id.contrastRadioButton1);
            contrastRadioButton1.setTextSize(0, (screenPixelWidth * 0.03f));
            RadioButton contrastRadioButton2 = (RadioButton) pw.getContentView().findViewById(R.id.contrastRadioButton2);
            contrastRadioButton2.setTextSize(0, (screenPixelWidth * 0.03f));
            RadioButton contrastRadioButton3 = (RadioButton) pw.getContentView().findViewById(R.id.contrastRadioButton3);
            contrastRadioButton3.setTextSize(0, (screenPixelWidth * 0.03f));

            RadioButton sortRadioButton1 = (RadioButton) pw.getContentView().findViewById(R.id.sortRadioButton1);
            sortRadioButton1.setTextSize(0, (screenPixelWidth * 0.03f));
            RadioButton sortRadioButton2 = (RadioButton) pw.getContentView().findViewById(R.id.sortRadioButton2);
            sortRadioButton2.setTextSize(0, (screenPixelWidth * 0.03f));
            RadioButton sortRadioButton3 = (RadioButton) pw.getContentView().findViewById(R.id.sortRadioButton3);
            sortRadioButton3.setTextSize(0, (screenPixelWidth * 0.03f));
            RadioButton sortRadioButton4 = (RadioButton) pw.getContentView().findViewById(R.id.sortRadioButton4);
            sortRadioButton4.setTextSize(0, (screenPixelWidth * 0.03f));

            contrastRadioGroup = (RadioGroup) pw.getContentView().findViewById(R.id.contrastRadioGroup);

            ((RadioButton) contrastRadioGroup.getChildAt(settings.getContrastCode())).setChecked(true);
            contrastRadioGroup.setOnCheckedChangeListener(ContrastRadioGroupOnCheckedChangeListener);

            sortRadioGroup = (RadioGroup) pw.getContentView().findViewById(R.id.sortRadioGroup);

            ((RadioButton) sortRadioGroup.getChildAt(settings.getSortCode())).setChecked(true);
            sortRadioGroup.setOnCheckedChangeListener(SortRadioGroupOnCheckedChangeListener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public View.OnClickListener CancelPopupElementHandler = new View.OnClickListener() {
        public void onClick(View v) {
            if(pw != null){
                pw.dismiss();
            }
        }
    };

    View.OnClickListener PopupOKButtonHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(pw != null){
                pw.dismiss();
            }
        }
    };


    RadioGroup.OnCheckedChangeListener ContrastRadioGroupOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            View checkedRadioButton = group.findViewById(checkedId);
            int contrastCode = group.indexOfChild(checkedRadioButton);
            switch(contrastCode){
                case 0: minContrastRatio = 3.0f; break;
                case 1: minContrastRatio = 4.5f; break;
                case 2: minContrastRatio = 7.0f; break;
                default: break;
            }

            settings.setContrastCode(contrastCode);

            recreateAllForNewSettings();
        }
    };


    RadioGroup.OnCheckedChangeListener SortRadioGroupOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            View checkedRadioButton = group.findViewById(checkedId);
            int sortCode = group.indexOfChild(checkedRadioButton);

            settings.setSortCode(sortCode);

            recreateAllForNewSettings();
        }
    };



    @Override
    public void onPause(){
        super.onPause();
        if(pw != null){
            pw.dismiss();
        }
        if(aboutPopupWindow != null){
            aboutPopupWindow.dismiss();
        }
    }

    @Override
    public void onStop(){
        super.onStop();

        dbHelper.updateSettingsTable(settings);

        if(pw != null){
            pw.dismiss();
        }
        if(aboutPopupWindow != null){
            aboutPopupWindow.dismiss();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(pw != null){
            pw.dismiss();
        }
        if(aboutPopupWindow != null){
            aboutPopupWindow.dismiss();
        }
    }



    private void initiateAboutWindow() {
        if(aboutPopupWindow != null){
            aboutPopupWindow.dismiss();
        }

        try {
            LayoutInflater inflater = LayoutInflater.from(this);
            View layout = inflater.inflate(R.layout.popup_about_layout, (ViewGroup) findViewById(R.id.popupAboutElement));

            aboutPopupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);


            aboutPopupWindow.showAtLocation(layout, Gravity.CLIP_VERTICAL, 0, 0);

            aboutPopupWindow.getContentView().setEnabled(true);
            aboutPopupWindow.getContentView().setOnClickListener(CancelAboutPopupElementHandler);


            aboutOKButton = (Button) aboutPopupWindow.getContentView().findViewById(R.id.aboutOKButton);
            aboutOKButton.setOnClickListener(AboutPopupOKButtonHandler);
            aboutOKButton.setTextSize(0, (screenPixelWidth * 0.04f));

            TextView aboutHeaderTextView = (TextView) aboutPopupWindow.getContentView().findViewById(R.id.aboutHeaderTextView);
            aboutHeaderTextView.setTextSize(0, (screenPixelWidth * 0.05f));

            TextView aboutTextView = (TextView) aboutPopupWindow.getContentView().findViewById(R.id.aboutTextView);
            aboutTextView.setTextSize(0, (screenPixelWidth * 0.04f));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public View.OnClickListener CancelAboutPopupElementHandler = new View.OnClickListener() {
        public void onClick(View v) {
            if(aboutPopupWindow != null){
                aboutPopupWindow.dismiss();
            }
        }
    };

    View.OnClickListener AboutPopupOKButtonHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(aboutPopupWindow != null){
                aboutPopupWindow.dismiss();
            }
        }
    };

}
