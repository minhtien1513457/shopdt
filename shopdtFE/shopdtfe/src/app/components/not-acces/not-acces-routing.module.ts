import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { NotAccesComponent } from './not-acces.component';

const routes: Routes = [
  {
    path :'', component :NotAccesComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class NotAccesRoutingModule { }
