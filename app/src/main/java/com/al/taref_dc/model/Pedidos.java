package com.al.taref_dc.model;

import java.util.ArrayList;
import java.util.List;

public class Pedidos {

    String NomeCliente;
    String ValorTotalPedido;
    List<Produto> listaProdutos = new ArrayList<>();

    public String getNomeCliente() {
        return NomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        NomeCliente = nomeCliente;
    }

    public String getValorTotalPedido() {
        return ValorTotalPedido;
    }

    public void setValorTotalPedido(String valorTotalPedido) {
        ValorTotalPedido = valorTotalPedido;
    }

    public List<Produto> getListaProdutos() {
        return listaProdutos;
    }

    public void setListaProdutos(List<Produto> listaProdutos) {
        this.listaProdutos = listaProdutos;
    }
}
