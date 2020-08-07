import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatSnackBar } from '@angular/material';
import { FormBuilder } from '@angular/forms';
import { ApiUserService } from 'src/app/shared/services/api-user.service';
import { JwtService } from 'src/app/shared/services/jwt.service';

@Component({
  selector: 'app-detail-user-modal',
  templateUrl: './detail-user-modal.component.html',
  styleUrls: ['./detail-user-modal.component.css']
})
export class DetailUserModalComponent implements OnInit {
  public dataUser : any;
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    public dialogRef: MatDialogRef<DetailUserModalComponent>,
    private frmBuilder: FormBuilder,
    private userService: ApiUserService,
    private jwt: JwtService,
    private _snackBar: MatSnackBar
  ) { }

  ngOnInit() {
    this.userService.getInfoUser(this.data.id).subscribe(res => {
      if(res) {
        this.dataUser = res;
      }
    })
  }

}
