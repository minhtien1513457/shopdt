import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatSnackBar } from '@angular/material';
import { ApiUserService } from 'src/app/shared/services/api-user.service';

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
    private userService: ApiUserService,
  ) { }

  ngOnInit() {
    this.userService.getInfoUser(this.data.id).subscribe(res => {
      if(res) {
        this.dataUser = res;
      }
    })
  }

}
