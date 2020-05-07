import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LaptopRoutingModule } from './laptop-routing.module';
import { SharedModule } from 'src/app/shared/shared/shared.module';
import { LaptopComponent } from './laptop.component';


@NgModule({
  declarations: [LaptopComponent],
  imports: [
    SharedModule,
    CommonModule,
    LaptopRoutingModule
  ]
})
export class LaptopModule { }
