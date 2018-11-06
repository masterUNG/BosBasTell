package masterung.androidthai.in.th.bosbastell;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView tempTextView, humidTextView;
    private String currentTempString, currentHumidString,
            alertTempString, alertHumidString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tempTextView = findViewById(R.id.txtTemp);
        humidTextView = findViewById(R.id.txtHumid);

//        RealTime Service
        realTimeService();


    }   // Main Method

    private void realTimeService() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Map map = (Map) dataSnapshot.getValue();
                currentTempString = String.valueOf(map.get("Temp"));
                currentHumidString = String.valueOf(map.get("Humidity"));

                tempTextView.setText(currentTempString);
                humidTextView.setText(currentHumidString);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }   // realTime

}   // Main Class
