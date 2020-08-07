import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { Validators, FormBuilder, FormGroup } from '@angular/forms';
import { ApiUserService } from 'src/app/shared/services/api-user.service';
import { JwtService } from 'src/app/shared/services/jwt.service';

@Component({
  selector: 'app-create-user-modal',
  templateUrl: './create-user-modal.component.html',
  styleUrls: ['./create-user-modal.component.css']
})
export class CreateUserModalComponent implements OnInit {
  public hide = true;
  public selectedRole: string;
  public roles = ["admin", "user"]
  public angForm: FormGroup;
  public disabledBtn: boolean = false;
  public submitted = false;
  public showProgress = false;
  constructor(
    public dialogRef: MatDialogRef<CreateUserModalComponent>,
    private frmBuilder: FormBuilder,
    private userService: ApiUserService,
    private jwt: JwtService,
    private _snackBar: MatSnackBar
    ) {}

  onNoClick(): void {
    this.dialogRef.close();
  }

  ngOnInit() {
    this.onValidate();
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
      password: ['', Validators.compose([Validators.required])],
      username: ['', Validators.compose([ Validators.required])],
      email : ['', Validators.compose([Validators.required, Validators.email])],
      role: ['', Validators.compose([ Validators.required])],
    });
  }

  getErrorUserName() {
    return this.angForm.get('username').hasError('required') ? 'Field is required': '';
  }

  getErrorPassword() {
    return this.angForm.get('password').hasError('required') ? 'Field is required': '';
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
        password: this.angForm.value.password,
        email: this.angForm.value.email,
        role: this.angForm.value.role,
        createdUser: this.jwt.getUsername()
      };
      this.userService.createUser(req).subscribe(res => {
        if (res) {
          this.disabledBtn = false;
          this.showProgress = false;
          if (res = "User registered successfully!") {
            this.onNoClick();
            this.openSnackBar("Create User", "Success")
          } else {
            this.onNoClick();
            this.openSnackBar("Create User", "Fail: " + res)
          }
        }
      });
    }
  }

}

