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
  dateReceived: string; // Ensure this is present in your API response
}

export interface DiaDeTransacoes {
  transacoes: { [stock: string]: Transacao }; // This defines the shape of transacoes
}

export interface ApiResponse {
  [date: string]: DiaDeTransacoes; // This indicates that each date maps to a DiaDeTransacoes
}
