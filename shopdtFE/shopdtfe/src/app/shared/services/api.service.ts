import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, throwError } from 'rxjs';
import { catchError, map, timeout } from 'rxjs/operators';
import { JwtService } from './jwt.service';
import * as data from '../../../assets/config/config.dev.json';

@Injectable()
export class ApiService {
  private timeOut = 1000 * 60 * 2;
  private _jsonURL = 'assets/config/config.dev.json';

  public urlServer;
  constructor(
    private httpClient: HttpClient,
    private jwtService: JwtService,
    private router: Router,
  ) {
      this.urlServer = data.apiServer.api_url;
  }

  /**Set header api */
  private setHeaders(): HttpHeaders {
    let header = new HttpHeaders(
      {
        'Content-Type': 'application/json',
        observe: 'response',
      }
    );

    if (this.jwtService.getTicket()) {
      header = header.append('Authorization', 'Bearer ' + this.jwtService.getTicket());
      header = header.append('username', this.jwtService.getUsername());
      header = header.append('language', this.jwtService.getLanguage());
    }
    return header;
  }

  /**Set header method get */
  private setHeadersGet(): HttpHeaders {
    let header = new HttpHeaders();
    if (this.jwtService.getTicket()) {
      header = header.append('Authorization', 'Bearer ' + this.jwtService.getTicket());
      header = header.append('username', this.jwtService.getUsername());
      header = header.append('language', this.jwtService.getLanguage());
    }
    return header;
  }

  /**Handle while api response error */
  private formatErrors(error: HttpErrorResponse) {
    if (error instanceof HttpErrorResponse) {
      if (error.error instanceof ErrorEvent) {
      } else {
        switch (error.status) {
          case 401:      //login
            this.navigateLogin();
            break;
          case 403:     //forbidden
            this.router.navigateByUrl("/not-access");
            // if(error.error && error.error.code) this.errorHandling(error.error.code, error.error.message);
            break;
          default:
            if(error.error && error.error.code) this.errorHandling(error.error.code, error.error.message);
        }
      }
    } else {

    }
    return throwError(error);
  }

  /** Handle error code*/
  errorHandling (errorCode, message = '') {

    // let msg = errorCode ? this.translateService.instant(errorCode.toString()) : null;
    // if(msg == errorCode){
    //   this.alertService.error(message);
    // }else if(msg != errorCode){
    //   this.alertService.error(msg);
    // }else{
    //   this.alertService.error(this.translateService.instant('lbl_serve_error')); 
    // }
  } 

  /**Redirect to page login */
  navigateLogin() {
    this.jwtService.clearStorage();
    this.router.navigate(['/login']);
  }

  /**Method get */
  get({ path, params }: { path: string; params?: HttpParams; }): Observable<any> {
    return this.httpClient.get(
      `${this.urlServer}${path}`,
      {
        headers: this.setHeadersGet(),
        params: params
      }
    ).pipe(
      timeout(this.timeOut),
      map((res: Response) => res),
      catchError(err => this.formatErrors(err))
    );
  }

  /**Method put */
  put(path: string, body: Object = {}): Observable<any> {
    return this.httpClient.put(
      `${this.urlServer}${path}`,
      JSON.stringify(body),
      { headers: this.setHeaders(), },
    )
      .pipe(
        timeout(this.timeOut),
        map((res: Response) => {
          if (res) {
            if (res.status === 201) {
                return [{ status: res.status, json: res }]
            }
            else if (res.status === 200) {
                return [{ status: res.status, json: res }]
            }
          }
        }),
        catchError(err => this.formatErrors(err))
      );
  }

/**Method post */
  post(path: string, body: Object = {}): Observable<any> {
    return this.httpClient.post(
      `${this.urlServer}${path}`,
      JSON.stringify(body),
      { headers: this.setHeaders() }
    ).pipe(
      timeout(this.timeOut),
      map((res: Response) => res),
      catchError(err => this.formatErrors(err))
    );
  }

/**Method delete */
  delete({ path, params }: { path: string; params?: HttpParams; }): Observable<any> {
    return this.httpClient.delete(
      `${this.urlServer}${path}`,
      {
        headers: this.setHeaders(),
        params: params
      },
    ).pipe(
      timeout(this.timeOut),
      map((res: Response) => {
        res
      }),
      catchError(err => this.formatErrors(err))
    );
  }
}
