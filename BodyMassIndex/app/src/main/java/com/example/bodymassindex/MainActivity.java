package com.example.bodymassindex;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    DecimalFormat formatter = new DecimalFormat("#,###.00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText editWeight = findViewById(R.id.editText_weight);
        editWeight.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(this,8, 2)});

        EditText editHeight = findViewById(R.id.editText_height);
        editHeight.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(this,8,2)});

        Button calBtn = findViewById(R.id.button_calculate);

        EditText editBMI = findViewById(R.id.editText_bmi);

        calBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                double weight = Double.parseDouble(editWeight.getText().toString());
                double heightCM = Double.parseDouble(editHeight.getText().toString());
                double heightM = heightCM / 100;
                double bmi = weight / (heightM*heightM);
                double roundedBMI = (double)Math.round(bmi * 10) / 10;
                formatter.setRoundingMode(RoundingMode.DOWN);
                editBMI.setText(formatter.format(roundedBMI));
                editBMI.setBackgroundResource(R.drawable.edittext_border_normal);
            }
        });



    }
}



/*class DecimalDigitsInputFilter implements InputFilter {
    private Pattern mPattern;
    DecimalDigitsInputFilter(int digits, int digitsAfterZero) {
        mPattern = Pattern.compile("[0-9]{0," + (digits - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) +

            "})?)||(\\.)?");
    }
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Matcher matcher = mPattern.matcher(dest);
        if (!matcher.matches()){
            Toast.makeText(MainActivity.this, getText(R.string.matcher_not_match), Toast.LENGTH_LONG).show();
            return "";
        }
            //Toast.makeText(MainActivity.this, getText(R.string.matcher_not_match), Toast.LENGTH_LONG).show();
            //return "";
        //return null;
    }
}*/

class DecimalDigitsInputFilter implements InputFilter {

    private final Pattern mPattern;
    private final Context mContext;

    public DecimalDigitsInputFilter(Context context, int digits, int digitsAfterZero) {
        mContext = context.getApplicationContext();
        String regex = "[0-9]{0," + digits + "}(\\.[0-9]{0," + digitsAfterZero + "})?";
        mPattern = Pattern.compile(regex);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {

        String newText = dest.subSequence(0, dstart)
                + source.subSequence(start, end).toString()
                + dest.subSequence(dend, dest.length());

        Matcher matcher = mPattern.matcher(newText);

        if (!matcher.matches()) {
            Toast.makeText(mContext, R.string.matcher_not_match, Toast.LENGTH_LONG).show();
            return "";
        }

        return null;
    }
}
