## Desafio ZG

**Dev:** João Vitor de Lima  
**Desenvolvedor Fullstack**


## Principais Tecnologias

- **Java 17:** versão 17.0.6-tem
- **Spring Boot 3:** 3.3.4
- **Spring Data JPA:** versão mais recente
- **Spring Data Postgresql:** versão mais recente
- **OpenAPI (Swagger):** 2.6.0

🚨 **Aviso:** Certificasse que as seguinter portas estão disponíveis: 8080, 4200, 5432

## Diagrama de Classes (Domínio da API)
```mermaid
classDiagram
    class TradeServiceImpl {
        +UserTradeRepository userTradeRepository
        +InstrumentQuoteRepository instrumentQuoteRepository
        +List<?> calculateReturnsOverRange(LocalDate startDate, LocalDate endDate)
    }

    class UserTrade {
        +LocalDate getData()
        +String getInstrument()
        +String getTipoOperacao()
        +int getQuantidade()
        +double getValorTotal()
    }

    class InstrumentQuote {
        +LocalDate getDate()
        +String getSimbol()
        +double getPrice()
    }

    class HoldingDTO {
        +Map<String, DailyReturnDTO> getTransacoes()
        +void setTransacoes(Map<String, DailyReturnDTO> transacoes)
    }

    class DailyReturnDTO {
        +double getQuantity()
        +void setQuantity(double quantity)
        +double getCost()
        +void setCost(double cost)
        +boolean isTransacao()
        +void setTransacao(boolean transacao)
        +boolean isVenda()
        +void setVenda(boolean venda)
        +double getValorTotalVendas()
        +void setValorTotalVendas(double valorTotalVendas)
        +double getSaldo()
        +void setSaldo(double saldo)
        +double getRendimento()
        +void setRendimento(double rendimento)
        +double getValorTotalComprado()
        +void setValorTotalComprado(double valorTotalComprado)
        +double getQuantidadeVendida()
        +void setQuantidadeVendida(double quantidadeVendida)
    }

    TradeServiceImpl --> UserTradeRepository
    TradeServiceImpl --> InstrumentQuoteRepository
    TradeServiceImpl --> List
    TradeServiceImpl --> HoldingDTO
    TradeServiceImpl --> DailyReturnDTO
    UserTrade --> LocalDate
    InstrumentQuote --> LocalDate

```


## API Endpoints
-------------

| Método | Endpoint                                   | Descrição                                   |
|--------|--------------------------------------------|---------------------------------------------|

| GET    | `/api/zg-invest/calcularRendimentos?dataInicial=2019-01-17&dataFinal=2020-01-22` 


## Como utilizar

```bash
   
    sudo docker-compose up --build
    sudo docker-compose exec postgres psql -U postgres -d bolsa -f /docker-entrypoint-initdb.d/bolsa.bkp
```


1. **SWAGGER DISPONÍVEL**
   - **URL:** http://localhost:8080/swagger-ui/index.html

1. **Front-end**
 - **URL:**  http://localhost:4200/


<p align="left">
  💌 Contatos: ⤵️
</p>

<p align="left">
  <a href="mailto:ozymandiasphp@gmail.com" title="Gmail">
  <img src="https://img.shields.io/badge/-Gmail-FF0000?style=flat-square&labelColor=FF0000&logo=gmail&logoColor=white&link=LINK-DO-SEU-GMAIL" alt="Gmail"/></a>
  <a href="https://www.linkedin.com/in/jo%C3%A3o-vitor-de-lima-74441b1b1/" title="LinkedIn">
  <img src="https://img.shields.io/badge/-Linkedin-0e76a8?style=flat-square&logo=Linkedin&logoColor=white&link=LINK-DO-SEU-LINKEDIN" alt="LinkedIn"/></a>
  <a href="https://wa.me/5581989553431" title="WhatsApp">
  <img src="https://img.shields.io/badge/-WhatsApp-25d366?style=flat-square&labelColor=25d366&logo=whatsapp&logoColor=white&link=API-DO-SEU-WHATSAPP" alt="WhatsApp"/></a>
</p>







