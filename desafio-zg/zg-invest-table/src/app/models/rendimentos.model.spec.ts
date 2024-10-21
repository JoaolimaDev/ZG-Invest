import { Transacao } from './rendimentos.model';

describe('Transacao', () => {
  it('should create an instance of Transacao', () => {
    const transacao: Transacao = {
      quantity: 12,
      cost: 442.79999999999995,
      rendimento: 0,
      quantidadeVendida: 0,
      valorTotalVendas: 0,
      valorTotalComprado: 0,
      saldo: 0,
      rendimentoFormatado: "0.00%",
      venda: false,
      transacao: false,
      dateReceived: "2024-01-01" 
    };
    expect(transacao).toBeTruthy();
  });
});
