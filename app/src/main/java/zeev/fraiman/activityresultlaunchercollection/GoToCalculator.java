package zeev.fraiman.activityresultlaunchercollection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class GoToCalculator extends AppCompatActivity {

    Context context;
    Button bCalculator;
    TextView tvCalcResult;
    private ActivityResultLauncher<Intent> calculatorLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_to_calculator);

        initComponents();

        bCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_CALCULATOR);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    calculatorLauncher.launch(intent);
                } else {
                    tvCalcResult.setText("Calculator app not found");
                }
            }
        });
    }

    private void initComponents() {
        bCalculator=findViewById(R.id.bCalculator);
        tvCalcResult=findViewById(R.id.tvCalcResult);
        calculatorLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            // Checking the returned data if the calculator supports returning the result
                            Intent data = result.getData();
                            String calculationResult = data.getStringExtra("result");
                            if (calculationResult != null) {
                                tvCalcResult.setText("Result: " + calculationResult);
                            }
                            else {
                                tvCalcResult.setText("Not return result");
                            }
                        }
                    }
                }
        );
    }
}