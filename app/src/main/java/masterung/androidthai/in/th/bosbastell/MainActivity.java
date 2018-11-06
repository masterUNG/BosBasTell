package masterung.androidthai.in.th.bosbastell;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView tempTextView, humidTextView;
    private String currentTempString = "20", currentHumidString = "30",
            alertTempString, alertHumidString;
    private int alertTempAnInt = 30, alertHumidAnInt = 60;
    int greenInt = 0xFF00FF00;
    int redInt = 0xFFFF0000;
    private boolean statusABoolean = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tempTextView = findViewById(R.id.txtTemp);
        humidTextView = findViewById(R.id.txtHumid);
        tempTextView.setTextColor(greenInt);
        humidTextView.setTextColor(greenInt);

//        RealTime Service
        realTimeService();


    }   // Main Method

    private void realTimeService() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        try {

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Map map = (Map) dataSnapshot.getValue();
                    currentTempString = String.valueOf(map.get("Temp"));
                    currentHumidString = String.valueOf(map.get("Humidity"));

                    tempTextView.setText(currentTempString);
                    humidTextView.setText(currentHumidString);


                    if (statusABoolean) {

                        //                Check Temp
                        if (findInt(currentTempString) > alertTempAnInt) {
                            setupAlert(true);
                        }

                        //                Check Humid
                        if (findInt(currentHumidString) > alertHumidAnInt) {
                            setupAlert(false);
                        }

                    }else {

                        //                Check Temp
                        if (findInt(currentTempString) <= alertTempAnInt) {
                            tempTextView.setTextColor(greenInt);
                        }

                        //                Check Humid
                        if (findInt(currentHumidString) <= alertHumidAnInt) {
                            humidTextView.setTextColor(greenInt);
                        }
                    }






                }   // Data change

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }   // realTime

    private int findInt(String currentString) {

        try {

            return Integer.valueOf(currentString);

        } catch (Exception e) {
            return 0;
        }



    }

    private void setupAlert(boolean status) {

        statusABoolean = false;
        Map<String, Object> objectMap = new HashMap<>();

        if (status) {
//            Alert Temp
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference().child("MyTemp");
            objectMap.put("alertTemp", 1);
            databaseReference.setValue(objectMap);
            tempTextView.setTextColor(redInt);

        } else {
//            Alert Humid
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference().child("MyHumid");
            objectMap.put("alertHumid", 1);
            databaseReference.setValue(objectMap);
            humidTextView.setTextColor(redInt);
        }   // if








    }   // setup

}   // Main Class
