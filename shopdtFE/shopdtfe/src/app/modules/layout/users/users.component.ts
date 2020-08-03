import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { ApiUserService } from 'src/app/shared/services/api-user.service';
import { MatPaginator, MatTableDataSource, PageEvent, MatSort, MatButtonModule, MatDialog } from '@angular/material';
import { SelectionModel } from '@angular/cdk/collections';
import { CreateUserModalComponent } from '../../modal/create-user-modal/create-user-modal.component';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit, AfterViewInit {
  public displayedColumns: string[] = ['select', 'username', 'email', 'role', 'status', 'createdDate'];
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource = new MatTableDataSource<any>();
  public initialSelection = [];
  public allowMultiSelect = true;
  public selection = new SelectionModel<any>(this.allowMultiSelect, this.initialSelection);

  public listUser: any;
  public pageInit: number = 0;
  public pagesizeInit: number = 5;
  public pageIndex: number = this.pageInit;
  public totalPage: number = this.pageInit;
  public pageSize: number = this.pagesizeInit;
  public length: number = 0;
  public pageEvent: PageEvent;
  public selectedElement = [];

  name = "Tâm";
  animal = "tiger";

  constructor(
    private userApi: ApiUserService,
    public dialog: MatDialog
  ) { }

  ngOnInit() {
    this.userApi.getListUser(1, this.pagesizeInit).subscribe(res => {
      this.listUser = res.lstData;
      this.pageIndex = 0;
      this.pageSize = res.pageSize;
      this.length = res.total;
      this.totalPage = res.totalPage;
      this.dataSource = new MatTableDataSource<any>(this.listUser);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    })
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  ngAfterViewChecked() {
    const list = document.getElementsByClassName('mat-paginator-range-label');
    list[0].innerHTML = 'Page: ' + this.pageIndex + '/' + (this.totalPage - 1);
  }

  /** Whether the number of selected elements matches the total number of rows. */
  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle($event) {
    if ($event.checked) {
      this.onCompleteRow(this.dataSource);
    } else {
      this.onCompleteRow(null);
    }
    this.isAllSelected() ?
      this.selection.clear() :
      this.dataSource.data.forEach(row => this.selection.select(row));
  }

  private selectRow($event, e) {
    if ($event.checked) {
      this.checkPushObInArray(e,this.selectedElement);
    }else {
      this.checkRemoveObInArray(e, this.selectedElement)
    }
    console.log(this.selectedElement)
  }

  onCompleteRow(dataSource) {
    if (dataSource != null) {
      this.selectedElement = [];
      dataSource.data.forEach(element => {
        this.selectedElement.push(element);
      });
    } 
    console.log(this.selectedElement)
    return;
  }

  checkRemoveObInArray(e, arr) {
    // get index of object with id:37
    var removeIndex = arr.map(function (item) { return item.id; }).indexOf(e.id);

    // remove object
    if(removeIndex != -1) {
      arr.splice(removeIndex, 1);
    }
  }

  checkPushObInArray(e, arr) {
    // get index of object with id:37
    var addIndex = arr.map(function (item) { return item.id; }).indexOf(e.id);

    // push object
    if(addIndex == -1) {
      arr.push(e);
    }
  }

  customStatus(e) {
    if (e == 1) {
      return "Active";
    } else {
      return "None-active";
    }
  }

  public getServerData(e?: PageEvent) {
    this.userApi.getListUser(e.pageIndex + 1, e.pageSize).subscribe(res => {
      this.listUser = res.lstData;
      this.pageIndex = res.pageNo - 1;
      this.pageSize = res.pageSize;
      this.length = res.total;
      this.totalPage = res.totalPage;
      this.dataSource = new MatTableDataSource<any>(this.listUser);
      this.dataSource.sort = this.sort;
    },
      error => {
        // handle error
      }
    );
    return event;
  }

  openCreateModal(): void {
    const dialogRef = this.dialog.open(CreateUserModalComponent, {
      data: {name: this.name, animal: this.animal}
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      this.animal = result;
    });
  }
}
