import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-type',
  templateUrl: './type.component.html',
  styleUrls: ['./type.component.css']
})
export class TypeComponent implements OnInit, AfterViewInit {
  public displayedColumns: string[] = ['select', 'name', 'description', 'createdDate'];
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  public dataSource = new MatTableDataSource<any>();
  public initialSelection = [];
  public allowMultiSelect = true;
  public selection = new SelectionModel<any>(this.allowMultiSelect, this.initialSelection);

  public listType: any;
  public pageInit: number = 0;
  public pagesizeInit: number = 5;
  public pageIndex: number = this.pageInit;
  public totalPage: number = this.pageInit;
  public pageSize: number = this.pagesizeInit;
  public length: number = 0;
  public pageEvent: PageEvent;
  public selectedElement = [];

  constructor(
    private userApi: ApiUserService,
    public dialog: MatDialog,
    private _snackBar: MatSnackBar
  ) { }

  ngOnInit() {
  }

}
