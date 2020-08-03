import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Validators } from '@angular/forms';

@Component({
  selector: 'app-create-user-modal',
  templateUrl: './create-user-modal.component.html',
  styleUrls: ['./create-user-modal.component.css']
})
export class CreateUserModalComponent implements OnInit {
  public hide = true;
  angForm: any;
  frmBuilder: any;
  constructor(
    public dialogRef: MatDialogRef<CreateUserModalComponent>) {}

  onNoClick(): void {
    this.dialogRef.close();
  }

  ngOnInit() {
  }

   /**Pattern validate form */
   private onValidate(): void {
    this.angForm = this.frmBuilder.group({
      password: ['', Validators.compose([Validators.required])],
      username: ['', Validators.compose([ Validators.required])]
    });
  }

  getErrorUserId() {
    return this.angForm.get('username').hasError('required') ? 'Field is required': '';
  }

  getErrorPassword() {
    return this.angForm.get('password').hasError('required') ? 'Field is required': '';
  }
}

