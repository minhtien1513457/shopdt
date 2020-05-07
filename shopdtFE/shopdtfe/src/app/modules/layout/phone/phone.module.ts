import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PhoneRoutingModule } from './phone-routing.module';
import { SharedModule } from 'src/app/shared/shared/shared.module';
import { PhoneComponent } from './phone.component';


@NgModule({
  declarations: [PhoneComponent],
  imports: [
    SharedModule,
    CommonModule,
    PhoneRoutingModule
  ]
})
export class PhoneModule { }
