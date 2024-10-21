import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../models/rendimentos.model';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private baseUrl = 'http://localhost:8080/api/zg-invest';

  constructor(private http: HttpClient) {}

  getRendimentos(dataInicial: string, dataFinal: string): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(`${this.baseUrl}/calcularRendimentos?dataInicial=${dataInicial}&dataFinal=${dataFinal}`);
  }
}
