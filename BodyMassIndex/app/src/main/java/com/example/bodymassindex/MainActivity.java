package com.example.bodymassindex;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    DecimalFormat formatter = new DecimalFormat("#,###.##");

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

        EditText editWeight = (EditText)findViewById(R.id.editText_weight);
        editWeight.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(this,8, 2)});

        EditText editHeight = (EditText)findViewById(R.id.editText_height);
        editHeight.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(this,8,2)});

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

        // Build the resulting string if this change is applied
        String newText = dest.subSequence(0, dstart)
                + source.subSequence(start, end).toString()
                + dest.subSequence(dend, dest.length());

        Matcher matcher = mPattern.matcher(newText);

        if (!matcher.matches()) {
            Toast.makeText(mContext, R.string.matcher_not_match, Toast.LENGTH_LONG).show();
            return ""; // reject this input
        }

        return null; // accept the change
    }
}
