// models/rendimentos.model.ts

export interface Transacao {
  quantity: number;
  cost: number;
  rendimento: number;
  quantidadeVendida: number;
  valorTotalVendas: number;
  valorTotalComprado: number;
  saldo: number;
  rendimentoFormatado: string;
  venda: boolean;
  transacao: boolean;
  dateReceived: string; 
}

export interface DiaDeTransacoes {
  transacoes: { [stock: string]: Transacao };
}

export interface ApiResponse {
  [date: string]: DiaDeTransacoes; 
}
