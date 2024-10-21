import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RendimentosTableComponent } from './components/rendimentos-table/rendimentos-table.component';  // Import the table component

const routes: Routes = [
  { path: '', component: RendimentosTableComponent },  // Default route for the root path
  // You can add more routes here if needed
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
