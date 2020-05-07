import { Component, ViewChild, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { MatSidenav } from '@angular/material/sidenav';
import { PlatformLocation, Location } from '@angular/common';
import { JwtService } from './shared/services/jwt.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'shopdtfe';
  constructor(
    private jwt: JwtService,
    router: Router,
    location: PlatformLocation,
    local: Location,
    ) {
      location.onPopState(() => {
        if (location.pathname === "/login") {
          this.jwt.clearStorage();
        }
      });
    
      if (jwt.getTicket() && local.path() === "/login") {
        router.navigate([`/layout/dashboard`]);
      }
    
  }
 
}
