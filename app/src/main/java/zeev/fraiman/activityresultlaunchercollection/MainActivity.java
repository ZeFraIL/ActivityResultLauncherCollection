package zeev.fraiman.activityresultlaunchercollection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Context context;
    String[] names={"Camera & Gallery","File Manager","Contacts","Calculator"};
    int n;
    Button[] buttons;
    LinearLayout LLallButtons;
    Class[] classes={CameraAndGallery.class, FileManager.class, FromContacts.class, GoToCalculator.class};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
    }

    private void initComponents() {
        context=MainActivity.this;
        n=names.length;
        buttons=new Button[n];
        LLallButtons=findViewById(R.id.LLallButtons);

        for (int i = 0; i < n; i++) {
            buttons[i]=new Button(context);
            buttons[i].setText(names[i]);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 150 );
            layoutParams.setMargins(10,10,10,40);
            buttons[i].setLayoutParams(new LinearLayout.LayoutParams(layoutParams));
            buttons[i].requestLayout();
            buttons[i].setPadding(10,10,10,10);
            buttons[i].setTextSize(20f);
            Intent go=new Intent(context,classes[i]);
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(go);
                }
            });
            LLallButtons.addView(buttons[i]);
        }
    }
}