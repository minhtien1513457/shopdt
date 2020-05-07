import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';
import { catchError, map, timeout } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ApiConfigService {
  private _jsonURL = 'assets/config/config.dev.json';
  constructor(private http: HttpClient) { 
    var object;
    this.getJSON().subscribe(data => object=data, error => console.log(error));
  }

  public getJSON(): Observable<any> {
    return this.http.get(this._jsonURL).pipe(
      map((response:any) => {return response})
    )
  }
}
