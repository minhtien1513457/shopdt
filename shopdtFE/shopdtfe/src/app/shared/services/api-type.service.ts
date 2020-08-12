import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { JwtService } from './jwt.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ApiTypeService {

  constructor(
    private jwt: JwtService,
    private api: ApiService
  ) { }

  //get all type
  getListType(page: number, size: number): Observable<any> {
    const url = `type/list?page=${page}&size=${size}`;
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

  /** Get info type by id */
  getInfoType(id: string): Observable<any> {
    const url = `type?id=${id}`;
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
