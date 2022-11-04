package com.al.taref_dc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button btn_venda;
    TextView valortotalDeVendas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_venda = findViewById(R.id.btn_venda);
        valortotalDeVendas = findViewById(R.id.valortotalDeVendas);

        btn_venda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Form.class));
            }
        });

        getValue();

    }

    private void getValue() {
        FirebaseDatabase.getInstance().getReference().child("Vendas").child("valorTotal").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    valortotalDeVendas.setText("Total de vendas: R$"+snapshot.getValue(String.class));
                }else{
                    valortotalDeVendas.setText("Total de vendas: R$0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Verifique sua internet", Toast.LENGTH_SHORT).show();
                valortotalDeVendas.setText("0");
            }
        });
    }
}