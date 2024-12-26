package zeev.fraiman.activityresultlaunchercollection;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FromContacts extends AppCompatActivity {

    Context context;
    private ActivityResultLauncher<Intent> contactPickerLauncher;
    Button bContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_contacts);

        initComponents();

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_CONTACTS},
                1);

        bContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickContact();
            }
        });
    }

    private void getPhoneNumber(Uri contactUri) {
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};

        try (Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{contactUri.getLastPathSegment()},
                null)) {
            if (cursor != null && cursor.moveToFirst()) {
                String phoneNumber = cursor.getString((int)cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Toast.makeText(context, "Phone="+phoneNumber, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error="+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleContactResult(Intent data) {
        if (data == null) {
            Toast.makeText(context, "Not found data about contacts", Toast.LENGTH_SHORT).show();
            return;
        }
        Uri contactUri = data.getData();
        if (contactUri == null) {
            Toast.makeText(context, "Not found data", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] projection = new String[]{
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
        };

        try (Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                String name = cursor.getString((int)cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                boolean hasPhoneNumber = Integer.parseInt(cursor.getString((int)cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0;

                // Используйте полученные данные
                Toast.makeText(context, "Name="+name+"\nPhone="+hasPhoneNumber, Toast.LENGTH_SHORT).show();
                if (hasPhoneNumber) {
                    getPhoneNumber(contactUri);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pickContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        contactPickerLauncher.launch(intent);
    }

    private void initComponents() {
        context=this;
        bContacts=findViewById(R.id.bContacts);
        contactPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Обработка полученных данных
                        handleContactResult(result.getData());
                    }
                }
        );
    }
}