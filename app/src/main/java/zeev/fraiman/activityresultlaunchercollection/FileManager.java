package zeev.fraiman.activityresultlaunchercollection;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.util.ArrayList;

public class FileManager extends AppCompatActivity {

    private Context context;
    private ArrayList<String> fileName;
    private String dirPath;
    private Button bBack;
    private ListView lv;
    private ArrayAdapter<String> adapter;
    private TextView currentPathtextView;
    // Define the launcher to handle permission requests
    private final ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            result -> {
                Boolean readGranted = result.getOrDefault(android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
                        false);
                if (readGranted) {
                    // Permission granted, proceed with loading directory content
                    loadDirectoryContent(dirPath);
                } else {
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);

        initComponents();

        checkPermissions();

        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goUpDirectory();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = fileName.get(position);
                if (selectedItem.endsWith("/")) {
                    dirPath += File.separator + selectedItem.substring(0, selectedItem.length() - 1);
                    loadDirectoryContent(dirPath);
                } else {
                    Toast.makeText(context, "Selected file: " + selectedItem, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initComponents() {
        context=this;
        lv=findViewById(R.id.lv);
        bBack=findViewById(R.id.bBack);
        currentPathtextView=findViewById(R.id.current_path);
        fileName=new ArrayList<>();
        dirPath= Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    private void checkPermissions() {
        // Check for permissions
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, proceed with loading directory content
            loadDirectoryContent(dirPath);
        } else {
            // Request permissions if not already granted
            requestPermissionLauncher.launch(new String[]{android.Manifest.permission.READ_MEDIA_AUDIO,
                    android.Manifest.permission.READ_MEDIA_IMAGES,
                    android.Manifest.permission.READ_MEDIA_IMAGES,
                    android.Manifest.permission.READ_MEDIA_VIDEO,
                    android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED});
        }

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, "External storage is OK", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadDirectoryContent(String directoryPath) {
        currentPathtextView.setText(directoryPath);
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        if (files != null) {
            fileName.clear();
            for (File file : files) {
                fileName.add(file.getName() + (file.isDirectory() ? "/" : ""));
            }
            if (adapter == null) {
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileName);
                lv.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
        bBack.setEnabled(directoryPath.length() > Environment.getExternalStorageDirectory().getAbsolutePath().length());
    }

    private void goUpDirectory() {
        int lastIndexOfSlash = dirPath.lastIndexOf(File.separator);
        if (lastIndexOfSlash != -1) {
            dirPath = dirPath.substring(0, lastIndexOfSlash);
            loadDirectoryContent(dirPath);
        } else {
            Toast.makeText(this, "You are already in the root directory", Toast.LENGTH_SHORT).show();
        }
    }



}