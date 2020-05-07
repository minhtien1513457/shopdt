import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LayoutRoutingModule } from './layout-routing.module';
import { LayoutComponent } from './layout.component';
import { SharedModule } from 'src/app/shared/shared/shared.module';

@NgModule({
  declarations: [
    LayoutComponent,
  ],
  imports: [
    SharedModule,
    CommonModule,
    LayoutRoutingModule
  ]
})
export class LayoutModule { }
