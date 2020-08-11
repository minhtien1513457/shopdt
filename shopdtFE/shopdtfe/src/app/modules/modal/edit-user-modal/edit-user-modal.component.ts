import { Component, OnInit, Inject, EventEmitter } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatSnackBar, ThemePalette } from '@angular/material';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ApiUserService } from 'src/app/shared/services/api-user.service';

@Component({
  selector: 'app-edit-user-modal',
  templateUrl: './edit-user-modal.component.html',
  styleUrls: ['./edit-user-modal.component.css']
})
export class EditUserModalComponent implements OnInit {
  public dataUser : any;
  public hide = true;
  public selectedRole: string;
  public roles = ["admin", "user"]
  public angForm: FormGroup;
  public disabledBtn: boolean = false;
  public submitted = false;
  public showProgress = false;
  public defaultRole: string;
  public active : boolean;
  onEdit = new EventEmitter()
  color: ThemePalette = 'warn';
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    public dialogRef: MatDialogRef<EditUserModalComponent>,
    private frmBuilder: FormBuilder,
    private userService: ApiUserService,
    private _snackBar: MatSnackBar,
  ) { }

  ngOnInit() {
    this.onValidate();
    this.userService.getInfoUser(this.data.id).subscribe(res => {
      if(res) {
        this.dataUser = res;
        this.angForm.patchValue({"username": res.username});
        this.angForm.patchValue({"email": res.email})
        this.angForm.patchValue({"status": res.status})
        if(res.role.name == "ROLE_ADMIN") {
          this.defaultRole = "admin";
          this.angForm.patchValue({"role": "admin"});
        }else {
          this.defaultRole = "user";
          this.angForm.patchValue({"role": "user"})
        }
        if(res.status == 0){
          this.active = false;
        }else {
          this.active = true;
        }
      }
    })
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  openSnackBar(action, status) {
    this._snackBar.open(action, status, {
      duration: 3000,
      horizontalPosition: "center",
      verticalPosition: "top",
    });
  }

   /**Pattern validate form */
   private onValidate(): void {
    this.angForm = this.frmBuilder.group({
      username: ['', Validators.compose([ Validators.required])],
      email : ['', Validators.compose([Validators.required, Validators.email])],
      role: ['', Validators.compose([ Validators.required])],
      status: ['', Validators.compose([Validators.required])],
    });
  }

  getErrorUserName() {
    return this.angForm.get('username').hasError('required') ? 'Field is required': '';
  }

  getErrorEmail() {
    if (this.angForm.get('email').hasError('required')) {
      return 'Field is required';
    }

    return this.angForm.get('email').hasError('email') ? 'Not a valid email' : '';
  }

  getErrorRole() {
    return this.angForm.get('role').hasError('required') ? 'Field is required': '';
  }

  /**Action when submit form */
  public onSubmit(): void {
    this.submitted = true;
    this.disabledBtn = true;
    if(!this.angForm.valid) {
      return;
    }
    if (this.angForm.valid) {
      this.showProgress = true;
      var req = {
        username: this.angForm.value.username,
        email: this.angForm.value.email,
        role: this.angForm.value.role,
        status: this.active==true ? 1: 0,
      };
      this.userService.editUser(this.data.id, req).subscribe(res => {
          this.disabledBtn = false;
          this.showProgress = false;
          if (!res) {
            this.onEdit.emit();
            this.onNoClick();
            this.openSnackBar("Edit User", "Success")
          } else {
            this.onNoClick();
            this.openSnackBar("Edit User", "Fail: " + res)
          }
      });
    }
  }
}
