import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TypeRoutingModule } from './type-routing.module';
import { SharedModule } from 'src/app/shared/shared/shared.module';
import { TypeComponent } from './type.component';



@NgModule({
  declarations: [TypeComponent],
  imports: [
    CommonModule,
    TypeRoutingModule,
    SharedModule
  ]
})
export class TypeModule { }
