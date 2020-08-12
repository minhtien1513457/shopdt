import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LayoutComponent } from './layout.component';


const routes: Routes = [
  {
    path: '', 
    component: LayoutComponent,
    children: [
      {path: '', redirectTo: 'dashboard', pathMatch: 'full'  },
      {path: 'dashboard', loadChildren: './dashboard/dashboard.module#DashboardModule'},
      {path: 'phone', loadChildren: './phone/phone.module#PhoneModule'},
      {path: 'laptop', loadChildren: './laptop/laptop.module#LaptopModule'},
      {path: 'tablet', loadChildren: './tablet/tablet.module#TabletModule'},
      {path: 'user', loadChildren: './users/users.module#UsersModule'},
      {path: 'type', loadChildren: './type/type.module#TypeModule'},
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LayoutRoutingModule { }
