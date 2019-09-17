
package com.example.transaction_monitor_app;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;


import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import static java.sql.Types.NULL;


public class MainActivity extends AppCompatActivity {
private EditText paytm;
private EditText gpay;
private EditText others;
private EditText cash;
private Button push;
private Button history;
private Button clear;
    private static final String TAG = "MyActivity";
    private SQLiteDatabase db;
    private TextView txt;
    private DatabaseReference ref;
    private  node n;
     private ViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            RecyclerView recyclerView=findViewById(R.id.recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
             recyclerView.setHasFixedSize(true);
             final adapter adap=new adapter();
             recyclerView.setAdapter(adap);
         viewModel = ViewModelProviders.of(this).get(ViewModel.class);
         viewModel.getAllnotes().observe(this, new Observer<List<History>>() {
             @Override
             public void onChanged(List<History> histories) {
                // Toast.makeText(MainActivity.this,"HELLO",Toast.LENGTH_SHORT).show();
               adap.setNotes(histories);
             }
         });

         new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                 ItemTouchHelper.LEFT) {
             @Override
             public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                 return false;
             }

             @Override
             public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                 new RecyclerViewSwipeDecorator.Builder(MainActivity.this, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                         .addBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.melon))
                         .addActionIcon(R.drawable.ic_delete_black_24dp)
                         .create()
                         .decorate();
                 super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
             }

             @Override
             public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    viewModel.delete(adap.getHistoryAt(viewHolder.getAdapterPosition()));

             }
         }).attachToRecyclerView(recyclerView);
        paytm=findViewById(R.id.paytm);
        gpay=findViewById(R.id.gpay);
        others=findViewById(R.id.others);
        cash=findViewById(R.id.cash);
        push=findViewById(R.id.button);

      //  ref=FirebaseDatabase.getInstance().getReference().child("node");

        clear=findViewById(R.id.history_clear);
        final ArrayList<node> ls=new ArrayList<>();
         push.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String temp_paytm=paytm.getText().toString().trim();
                 String temp_cash=cash.getText().toString().trim();
                 String temp_gpay=gpay.getText().toString().trim();
                 String temp_others=others.getText().toString().trim();
                 n= new node();
                // ContentValues values=new ContentValues();
                 if(temp_cash != null && !temp_cash.isEmpty()){
                     int t=Integer.parseInt(temp_cash);
                   History history=new History("cash",t);
                   viewModel.insert(history);

                 }
                 if(temp_paytm != null && !temp_paytm.isEmpty()){
                     int t=Integer.parseInt(temp_paytm);
                     History history=new History("paytm",t);
                     viewModel.insert(history);

                 }
                 if(temp_gpay != null && !temp_gpay.isEmpty() ){
                     int t=Integer.parseInt(temp_gpay);
                     History history=new History("gpay",t);
                     viewModel.insert(history);

                 }
                 if(temp_others != null && !temp_others.isEmpty()){
                     int t=Integer.parseInt(temp_others);
                     History history=new History("others",t);
                     viewModel.insert(history);

                 }

                 gpay.setText("");
                 others.setText("");
                 paytm.setText("");
                 cash.setText("");

             }
         });

         clear.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 viewModel.deleteall();

             }
         });
    }
}
