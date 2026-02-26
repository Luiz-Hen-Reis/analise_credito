package com.henr.analise_credito.exception;

public class CustomerNotFoundException extends RuntimeException {
  public CustomerNotFoundException() {
    super("Usuário não encontrado");
  }
}
