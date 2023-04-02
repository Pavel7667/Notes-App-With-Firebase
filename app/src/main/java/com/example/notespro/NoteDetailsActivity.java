package com.example.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText;
    EditText contentEditText;
    ImageButton saveNoteBtn;
    TextView pageTitleTextView;
    String title, content, docId;
    boolean isEditMode;
    TextView deleteNodeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditText = findViewById(R.id.notes_title_text);
        contentEditText = findViewById(R.id.notes_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteNodeBtn = findViewById(R.id.delete_note_text_view_btn);

        //receive data
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if (docId != null && !docId.isEmpty()) {
            isEditMode=true;
        }
        titleEditText.setText(title);
        contentEditText.setText(content);

        if (isEditMode){
            pageTitleTextView.setText("Edit your Note");
            deleteNodeBtn.setVisibility(View.VISIBLE);
        }
        saveNoteBtn.setOnClickListener(v -> saveNote());
        deleteNodeBtn.setOnClickListener(v->deleteNodeFromFireBase());
    }

    private void deleteNodeFromFireBase() {
        DocumentReference documentReference;

            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    //note is del
                    Utility.showToast(NoteDetailsActivity.this, "Note is DEL");
                    finish();
                } else {
                    // note NOT del
                    Utility.showToast(NoteDetailsActivity.this, "Note is NOT del");
                }
            }
        });
    }

    private void saveNote() {
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();
        if (noteTitle == null || noteTitle.isEmpty()) {
            titleEditText.setError("Title is required");
            return;
        }
        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteToFirebase(note);
    }

    void saveNoteToFirebase(Note note) {
        DocumentReference documentReference;
        if (isEditMode) {
            //update
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        } else {
            //create new
             documentReference = Utility.getCollectionReferenceForNotes().document();
        }
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    //note is added
                    Utility.showToast(NoteDetailsActivity.this, "Note added successfully");
                    finish();
                } else {
                    // note NOT added
                    Utility.showToast(NoteDetailsActivity.this, "Note NOT added ");
                }
            }
        });
    }
}