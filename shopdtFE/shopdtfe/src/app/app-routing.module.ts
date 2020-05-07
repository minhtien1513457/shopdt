import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SharedModule } from './shared/shared/shared.module';


const routes: Routes = [
  {
    path: '',
    loadChildren: './modules/layout/layout.module#LayoutModule',
    runGuardsAndResolvers: `always`,
  },
  { path: 'login', loadChildren: './modules/auth/auth.module#AuthModule' },
  { path: '**', loadChildren: './components/not-acces/not-acces.module#NotAccesModule' },
  { path: 'not-access', loadChildren: './components/not-acces/not-acces.module#NotAccesModule' },
];
@NgModule({
  imports: [
    RouterModule.forRoot(routes, { onSameUrlNavigation: `reload` }),
    SharedModule
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }

