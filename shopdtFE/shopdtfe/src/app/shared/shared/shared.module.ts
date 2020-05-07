import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import {
   MatButtonModule,
   MatToolbarModule,
   MatIconModule,
   MatBadgeModule,
   MatSidenavModule,
   MatListModule,
   MatGridListModule,
   MatFormFieldModule,
   MatInputModule,
   MatSelectModule,
   MatRadioModule,
   MatDatepickerModule,
   MatNativeDateModule,
   MatChipsModule,
   MatTooltipModule,
   MatTableModule,
   MatPaginatorModule,
   MatCardModule
} from '@angular/material';
import { HeaderComponent } from 'src/app/components/header/header.component';
import { FooterComponent } from 'src/app/components/footer/footer.component';
import { HeaderSidebarComponent } from 'src/app/components/header-sidebar/header-sidebar.component';
import { RouterModule } from '@angular/router';



@NgModule({
   declarations: [
      HeaderSidebarComponent,
      HeaderComponent,
      FooterComponent
   ],

  imports: [
     RouterModule,
     CommonModule,
     MatButtonModule,
     MatToolbarModule,
     MatIconModule,
     MatSidenavModule,
     MatBadgeModule,
     MatListModule,
     MatGridListModule,
     MatFormFieldModule,
     MatInputModule,
     MatSelectModule,
     MatRadioModule,
     MatDatepickerModule,
     MatNativeDateModule,
     MatChipsModule,
     MatTooltipModule,
     MatTableModule,
     MatPaginatorModule,
     MatCardModule,
     FormsModule,
     ReactiveFormsModule
  ],
  exports: [
     //export module
     RouterModule,
     MatButtonModule,
     MatToolbarModule,
     MatIconModule,
     MatSidenavModule,
     MatBadgeModule,
     MatListModule,
     MatGridListModule,
     MatInputModule,
     MatFormFieldModule,
     MatSelectModule,
     MatRadioModule,
     MatDatepickerModule,
     MatChipsModule,
     MatTooltipModule,
     MatTableModule,
     MatPaginatorModule,
     MatCardModule,
     ReactiveFormsModule,
     FormsModule,
     //export component
     HeaderSidebarComponent,
     HeaderComponent,
     FooterComponent
  ],
  providers: [
     MatDatepickerModule,
  ]
})

export class SharedModule { }
