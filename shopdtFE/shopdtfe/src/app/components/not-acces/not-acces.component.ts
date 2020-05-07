import { Component, OnInit } from '@angular/core';
// import { JwtService } from '../../shared/service/jwt.service';
// import { SharedService } from 'src/app/shared/shared.service';

@Component({
  selector: 'app-not-acces',
  templateUrl: './not-acces.component.html',
  styleUrls: ['./not-acces.component.scss']
})
export class NotAccesComponent implements OnInit {
  isRole = '';
  // constructor( private jwt: JwtService) {
  //   if(SharedService.modalRef) SharedService.modalRef.hide();
  //  }

  ngOnInit() {
    // this.isRole = this.jwt.getRole();
  }

}
