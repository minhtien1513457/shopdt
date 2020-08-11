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
          totalPage: res.totalPage,
          lstData: res.lstData,
          pageNo: res.page,
          pageSize: res.pageSize,
          total: res.totalItem,
          pageNumber: Math.ceil(Number(res.totalItem / res.pageSize))
        }
      })
    )
  }

  /** Get info user by id */
  getInfoUser(id: string): Observable<any> {
    const url = `user?id=${id}`;
    return this.api.get({ path: url }).pipe(
      map(res => {
        return res.data;
      })
    )
  }

  /** Create user */
  public createUser(data): Observable<any> {
    const url = `auth/signup`;
    return this.api.post(url, data).pipe(
      map(data => {
        return data;
      }
      )
    );
  }

  /** Edit user */
  public editUser(id, data): Observable<any> {
    const url = `user/${id}`;
    return this.api.put(url, data).pipe(
      map(data => {
        return data;
      }
      )
    );
  }

    /** Edit user */
    public deleteUser(ids): Observable<any> {
      const url = `user/${ids}`;
      return this.api.delete({path: url}).pipe(
        map(data => {
          return data;
        }
        )
      );
    }
}
