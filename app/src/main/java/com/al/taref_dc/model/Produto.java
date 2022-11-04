package com.al.taref_dc.model;

public class Produto {

    String nomeProduto;
    String quantidade;
    String valor_Unit;
    String total;

    public Produto(){}

    public Produto(String nomeCliente, String nomeProduto, String quantidade, String valor_Unit, String total) {
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.valor_Unit = valor_Unit;
        this.total = total;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    public String getValor_Unit() {
        return valor_Unit;
    }

    public void setValor_Unit(String valor_Unit) {
        this.valor_Unit = valor_Unit;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
