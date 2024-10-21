import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { ApiResponse, Transacao, DiaDeTransacoes } from '../../models/rendimentos.model'; 

@Component({
  selector: 'app-rendimentos-table',
  templateUrl: './rendimentos-table.component.html',
  styleUrls: ['./rendimentos-table.component.css'],
})
export class RendimentosTableComponent implements OnInit {
  dataSource: Array<{ date: string; stock: string } & Transacao> = [];
  displayedColumns: string[] = ['date', 'stock', 'quantity', 'cost', 'rendimentoFormatado', 'dateReceived'];

  dataInicial: Date = new Date('2019-01-17');
  dataFinal: Date = new Date('2020-01-22');

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.fetchData();
  }

  fetchData(): void {
    const formattedDataInicial = this.formatDate(this.dataInicial);
    const formattedDataFinal = this.formatDate(this.dataFinal);
    this.apiService.getRendimentos(formattedDataInicial, formattedDataFinal).subscribe(
      (data: ApiResponse) => {
        console.log(data);
        this.dataSource = this.transformData(data);
      },
      (error) => {
        console.error('Error fetching rendimentos:', error);
      }
    );
  }

  formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  transformData(data: ApiResponse): Array<{ date: string; stock: string } & Transacao> {
    const result: Array<{ date: string; stock: string } & Transacao> = [];
    
    
    if (Array.isArray(data)) {
      data.forEach(item => {
        for (const [date, transactions] of Object.entries(item)) {
       
          const diaDeTransacoes = transactions as DiaDeTransacoes;

    
          if (diaDeTransacoes && diaDeTransacoes.transacoes) {
            const transacoes: { [stock: string]: Transacao } = diaDeTransacoes.transacoes;

            for (const [stock, details] of Object.entries(transacoes)) {
              result.push({
                date,
                stock,
                ...details,
                dateReceived: date 
              });
            }
          } else {
            console.error(`Expected transactions to contain transacoes for date ${date}, but got:`, transactions);
          }
        }
      });
    } else {
      console.error('Expected data to be an array, but got:', data);
    }
    
    return result;
  }
}
