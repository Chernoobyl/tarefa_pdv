package com.al.taref_dc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.al.taref_dc.model.Pedidos;
import com.al.taref_dc.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Form extends AppCompatActivity {

    RecyclerView lista_produtos_rc;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        lista_produtos_rc = findViewById(R.id.lista_produtos_rc);
        lista_produtos_rc.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        FirebaseDatabase.getInstance().getReference().child("Pedidos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter = new Adapter(getApplicationContext(), (int) snapshot.getChildrenCount());
                lista_produtos_rc.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public class Adapter extends RecyclerView.Adapter<Adapter.Holder>{

        Context mContext;
        List<Produto> lista_produto = new ArrayList<>();
        int numberP;
        public Adapter(Context mContext, int numberP) {
            this.mContext = mContext;
            this.numberP = numberP;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            if(viewType == 0){
                view = LayoutInflater.from(mContext).inflate(R.layout.form_fragment,parent,false);
            }else{
                view = LayoutInflater.from(mContext).inflate(R.layout.lista_produtos_fragment,parent,false);
            }
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {

            if(position == 0){
                holder.numeroPedido_tv.setText("Pedido: Nº"+numberP);
                holder.adicionar_Produto_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nomeCliente = holder.nomeCliente_edt.getText().toString();
                        String nomeProduto = holder.nomeProduto_edt.getText().toString();
                        String quantidade = holder.quantidade_edt.getText().toString();
                        String valor_Unit = holder.valor_unitario_edt.getText().toString();

                        String valorTotal_Unit = holder.valor_total_unitario_edt.getText().toString();
                        String quantidadeTotal = holder.quantidade_total_edt.getText().toString();

                        if(!nomeCliente.isEmpty() & !nomeProduto.isEmpty() & !quantidade.isEmpty() & !valor_Unit.isEmpty()){
                            if(Integer.parseInt(quantidade) > 1 | Double.parseDouble(valor_Unit) > 1){

                                Produto pd = new Produto();
                                pd.setNomeProduto(nomeProduto);
                                pd.setQuantidade(quantidade);
                                pd.setValor_Unit(valor_Unit);
                                Double mult_valorTotal = (Integer.parseInt(quantidade) * Double.parseDouble(valor_Unit));
                                pd.setTotal(mult_valorTotal+"");

                                Double somaValorTotal = (Double.parseDouble(valorTotal_Unit) + mult_valorTotal);
                                int somaQuantidadeTotal = (Integer.parseInt(quantidadeTotal) + Integer.parseInt(quantidade));

                                holder.valor_total_unitario_edt.setText(somaValorTotal+"");
                                holder.quantidade_total_edt.setText(somaQuantidadeTotal+"");

                                lista_produto.add(pd);
                                adapter.notifyDataSetChanged();
                                clean(holder);
                            }else{
                                Toast.makeText(mContext, "valor ou quantidade insuficiente", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(mContext, "preenchas os campos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                holder.cancelar_Produto_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

                holder.salvar_Produto_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        
                        if(lista_produto.size()!=0){
                            Pedidos pdd = new Pedidos();
                            pdd.setNomeCliente(holder.nomeCliente_edt.getText().toString());
                            pdd.setListaProdutos(lista_produto);
                            pdd.setValorTotalPedido(holder.valor_total_unitario_edt.getText().toString());

                            FirebaseDatabase.getInstance().getReference().child("Pedidos").push().setValue(pdd);
                            somaValorTotal(holder.valor_total_unitario_edt.getText().toString());

                            Toast.makeText(mContext, "Salvo", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(mContext, "Adicione pelo menos 1 item", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }else{
                holder.nomeProduto_show.setText(lista_produto.get(position-1).getNomeProduto());
                holder.quantidade_show.setText(lista_produto.get(position-1).getQuantidade());
                holder.valorUnit_show.setText(lista_produto.get(position-1).getValor_Unit());
                holder.valorTotal_show.setText(lista_produto.get(position-1).getTotal());
            }
        }

        private void somaValorTotal(String valor) {


            FirebaseDatabase.getInstance().getReference().child("Vendas").child("valorTotal").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        Double val = Double.parseDouble(snapshot.getValue(String.class))+ Double.parseDouble(valor);
                        FirebaseDatabase.getInstance().getReference().child("Vendas").child("valorTotal").setValue(val+"");
                    }else{
                        FirebaseDatabase.getInstance().getReference().child("Vendas").child("valorTotal").setValue(Double.parseDouble(valor)+"");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        }

        private void clean(Adapter.Holder holder) {
            holder.nomeProduto_edt.setText("");
            holder.quantidade_edt.setText("");
            holder.valor_unitario_edt.setText("");
        }

        @Override
        public int getItemViewType(int position) {
            return (position == 0)? 0:1;
        }

        @Override
        public int getItemCount() {
            return lista_produto.size()+1;
        }

        public class Holder extends RecyclerView.ViewHolder{

            //layout form
            Button adicionar_Produto_btn;
            Button salvar_Produto_btn;
            Button cancelar_Produto_btn;
            EditText nomeProduto_edt;
            EditText nomeCliente_edt;
            EditText quantidade_edt;
            EditText valor_unitario_edt;
            EditText valor_total_unitario_edt;
            EditText quantidade_total_edt;
            TextView numeroPedido_tv;

            //layout show
            EditText nomeProduto_show;
            EditText quantidade_show;
            EditText valorUnit_show;
            EditText valorTotal_show;



            public Holder(@NonNull View itemView) {
                super(itemView);

                //layout form
                quantidade_total_edt = itemView.findViewById(R.id.quantidade_total_tv);
                valor_total_unitario_edt = itemView.findViewById(R.id.valor_total_unitario_edt);
                valor_unitario_edt = itemView.findViewById(R.id.valor_unitario_edt);
                quantidade_edt = itemView.findViewById(R.id.quantidade_edt);
                nomeCliente_edt = itemView.findViewById(R.id.nomeCliente_edt);
                adicionar_Produto_btn = itemView.findViewById(R.id.adicionar_Produto_btn);
                nomeProduto_edt = itemView.findViewById(R.id.nomeProduto_edt);
                cancelar_Produto_btn = itemView.findViewById(R.id.cancelar_Produto_btn);
                salvar_Produto_btn = itemView.findViewById(R.id.salvar_Produto_btn);
                numeroPedido_tv = itemView.findViewById(R.id.numeroPedido_tv);

                //layout shom
                nomeProduto_show = itemView.findViewById(R.id.nomeProduto_show);
                quantidade_show = itemView.findViewById(R.id.quantidade_show);
                valorUnit_show = itemView.findViewById(R.id.valorUnit_show);
                valorTotal_show = itemView.findViewById(R.id.valorTotal_show);
            }
        }

    }


}