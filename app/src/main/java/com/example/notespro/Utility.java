package com.example.notespro;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public class Utility {
    static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    static CollectionReference getCollectionReferenceForNotes() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance()
                .collection("notes")
                .document(currentUser.getUid())
                .collection("my_notes");
    }

    static String timestampToString(Timestamp timestamp) {
        Date date = timestamp.toDate();
        String dayOfMonth = new SimpleDateFormat("dd").format(date);
        String monthString = new SimpleDateFormat("MMMM").format(date);
        String year = new SimpleDateFormat("yyyy").format(date);
        String time = new SimpleDateFormat("HH:mm").format(date);
        return String.format("%s_%s_%s // %s", dayOfMonth, monthString, year, time);
    }
}

