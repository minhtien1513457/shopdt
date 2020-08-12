import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Route, Routes, RouterModule } from '@angular/router';
import { TypeComponent } from './type.component';

const routes: Routes = [
  {path: '', component: TypeComponent}
]

@NgModule({
  imports: [
    RouterModule.forChild(routes),
  ],
  exports: [RouterModule]
})
export class TypeRoutingModule { }
