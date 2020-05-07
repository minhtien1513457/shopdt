import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { JwtService } from './jwt.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ApiUserService {

  constructor(
    private jwt: JwtService,
    private api: ApiService
  ) { }

  //login
  login(user: any): Observable<any> {
    return this.api.post('auth/signin', user).pipe(
      map(res => {
        if(res) {
          let result = res;
          let role: string = result.role 
          console.log(JSON.stringify(result))
          this.jwt.saveTicket(result.token);
          this.jwt.saveRole(role.substring(1,(role.length)-1));
          this.jwt.saveExpired(result.expire)
          return res;
        }
      })
    )
  }
}
