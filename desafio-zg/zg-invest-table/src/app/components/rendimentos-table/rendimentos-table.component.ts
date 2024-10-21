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
  paginatedData: Array<{ date: string; stock: string } & Transacao> = [];
  displayedColumns: string[] = ['date', 'stock', 'quantity', 'cost', 'rendimentoFormatado', 'dateReceived'];

  dataInicial: Date = new Date('2019-01-17');
  dataFinal: Date = new Date('2020-01-22');

  totalElements: number = 0; 
  pageSize: number = 5; 
  currentPage: number = 0; 
  searchTerm: string = ''; 

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.fetchData();
  }

  fetchData(): void {
    const formattedDataInicial = this.formatDate(this.dataInicial);
    const formattedDataFinal = this.formatDate(this.dataFinal);
    this.apiService.getRendimentos(formattedDataInicial, formattedDataFinal).subscribe(
      (data: ApiResponse) => {
        console.log('Full API Response:', data); 
        this.dataSource = this.transformData(data);
        this.totalElements = this.dataSource.length; 
        this.updatePaginatedData(); 
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
            console.error(transactions);
          }
        }
      });
    } else {
      console.error( data);
    }

    return result;
  }

  updatePaginatedData(): void {
    const start = this.currentPage * this.pageSize;
    const end = start + this.pageSize;
    this.paginatedData = this.dataSource.slice(start, end);
  }

  onPageChange(event: any): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.updatePaginatedData(); 
  }


  
}
