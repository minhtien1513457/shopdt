import { Component, OnInit  } from '@angular/core';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { ApiUserService } from 'src/app/shared/services/api-user.service';
import { JwtService } from 'src/app/shared/services/jwt.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  hide = true;
  angForm: FormGroup;
  mesuser: boolean = false;
  mespass: boolean = false;
  resFalse: boolean = false;
  disabledBtn: boolean = false;
  submitted = false;
  isRole = '';
  constructor(
   
    private userService: ApiUserService,
    private frmBuilder: FormBuilder,
    private router: Router,
    private jwt: JwtService,
    private route: ActivatedRoute,
    
  ) {
  }
  
  /**Implement OnInit's `ngOnInit` method
   ** validate form
  */
  ngOnInit() {
    this.route.parent.url.subscribe();
    this.isRole = this.jwt.getRole();
    this.onValidate();
  }
  
  /**Action when submit form */
  public onSubmit(): void {
    this.submitted = true;
    this.disabledBtn = true;
    this.resFalse = false;
    if (this.angForm.valid) {
      var reqLogin = {
        username: this.angForm.value.userID,
        password: this.angForm.value.password
      };
      this.userService.login(reqLogin).subscribe(res => {
        if (res) {
          this.disabledBtn = false
          if (res.success) {
              this.router.navigate([`/dashboard`]);                      
          } else {
            this.jwt.clearStorage();
          }
        }
      });
    } else {
    }
  }

  /**Pattern validate form */
  private onValidate(): void {
    this.angForm = this.frmBuilder.group({
      password: ['', Validators.compose([Validators.required])],
      userID: ['', Validators.compose([ Validators.required])]
    });
  }

  getErrorUserId() {
    return this.angForm.get('userID').hasError('required') ? 'Field is required': '';
  }

  getErrorPassword() {
    return this.angForm.get('password').hasError('required') ? 'Field is required': '';
  }
}
