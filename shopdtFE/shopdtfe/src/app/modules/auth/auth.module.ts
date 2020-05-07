import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthRoutingModule } from './auth-routing.module';
import { AuthComponent } from './auth.component';
import { SharedModule } from 'src/app/shared/shared/shared.module';

@NgModule({
  declarations: [AuthComponent],
  imports: [
    SharedModule,
    CommonModule,
    AuthRoutingModule,
  ]
})
export class AuthModule { }
