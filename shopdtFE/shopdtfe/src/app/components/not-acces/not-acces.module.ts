import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NotAccesRoutingModule } from './not-acces-routing.module';
import { NotAccesComponent } from './not-acces.component';

@NgModule({
  declarations: [NotAccesComponent],
  imports: [
    CommonModule,
    NotAccesRoutingModule
  ]
})
export class NotAccesModule { }
