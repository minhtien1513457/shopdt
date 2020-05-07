import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TabletRoutingModule } from './tablet-routing.module';
import { SharedModule } from 'src/app/shared/shared/shared.module';
import { TabletComponent } from './tablet.component';


@NgModule({
  declarations: [TabletComponent],
  imports: [
    SharedModule,
    CommonModule,
    TabletRoutingModule
  ]
})
export class TabletModule { }
