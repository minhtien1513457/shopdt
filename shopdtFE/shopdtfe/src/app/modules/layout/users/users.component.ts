import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { ApiUserService } from 'src/app/shared/services/api-user.service';
import { MatPaginator, MatTableDataSource, PageEvent } from '@angular/material';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit, AfterViewInit {
  public displayedColumns: string[] = ['username', 'email', 'role', 'status', 'createdDate'];
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  dataSource = new MatTableDataSource<any>();

  public listUser: any;
  public pageInit: number = 0;
  public pagesizeInit: number = 5;
  public pageIndex: number = this.pageInit;
  public pageSize: number = this.pagesizeInit;
  public length: number = 0;
  public pageEvent: PageEvent;
  
  constructor(
    private userApi: ApiUserService
  ) { }

  ngOnInit() {
    this.userApi.getListUser(1, this.pagesizeInit).subscribe(res => {
      this.listUser = res.lstData;
      this.pageIndex = 0;
      this.pageSize = res.pageSize;
      this.length = res.total;
      this.dataSource = new MatTableDataSource<any>(this.listUser);
      this.dataSource.paginator = this.paginator;
    })
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
  }

  customStatus(e) {
    if (e == 1) {
      return "Active";
    } else {
      return "None-active";
    }
  }

  public getServerData(e?:PageEvent){
    console.log(JSON.stringify(e))
    this.userApi.getListUser(e.pageIndex+1, e.pageSize).subscribe(res => {
      this.listUser = res.lstData;
      this.pageIndex = res.pageNo-1;
      this.pageSize = res.pageSize;
      this.length = res.total;
      this.dataSource = new MatTableDataSource<any>(this.listUser);
      },
      error =>{
        // handle error
      }
    );
    return event;
  }
}
