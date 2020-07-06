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
        if (res) {
          let result = res;
          let role: string = result.role
          this.jwt.saveTicket(result.token);
          this.jwt.saveRole(role.substring(1, (role.length) - 1));
          this.jwt.saveExpired(result.expire);
          this.jwt.saveUsername(res.username)
          this.jwt.saveLanguage('en');
          return res;
        }
      })
    )
  }

  //get all user
  getListUser(page: number, size: number): Observable<any> {
    const url = `user/list?page=${page}&size=${size}`;
    return this.api.get({ path: url }).pipe(
      map(res => {
        return {
          lstData: res.lstData,
          pageNo: res.page,
          pageSize: res.pageSize,
          total: res.totalItem,
          pageNumber: Math.ceil(Number(res.totalItem / res.pageSize))
        }
      })
    )
  }

  /** Get info group by id */
  getInfoUser(id: string): Observable<any> {
    const url = `user?id=${id}`;
    return this.api.get({ path: url }).pipe(
      map(res => {
        return res.data;
      })
    )
  }
}