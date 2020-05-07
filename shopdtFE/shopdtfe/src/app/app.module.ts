import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LayoutModule } from './modules/layout/layout.module';
import { SharedModule } from './shared/shared/shared.module';
import { ApiService } from './shared/services/api.service';
import { HttpClientModule } from '@angular/common/http';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    RouterModule,
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    LayoutModule,
    SharedModule,
    HttpClientModule,
    NgbModule.forRoot()
  ],

  providers: [ApiService],
  bootstrap: [AppComponent]
})
export class AppModule { }
