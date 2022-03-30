
import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVenta } from '../venta.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { VentaService } from '../service/venta.service';
import { VentaDeleteDialogComponent } from '../delete/venta-delete-dialog.component';
import { VentaUpdateModalComponent } from '../venta-modal-update/venta-modal-update.component';
import { VistaDialogComponent } from '../venta-modal-vista/venta-modal-vista.component';

@Component({
  selector: 'jhi-venta',
  templateUrl: './venta.component.html',
})
export class VentaComponent implements OnInit {
  ventas?: IVenta[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected ventaService: VentaService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.ventaService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IVenta[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        error: () => {
          this.isLoading = false;
          this.onError();
        },
      });
  }

  ngOnInit(): void {
    this.handleNavigation();
  }

  trackId(index: number, item: IVenta): number {
    return item.id!;
  }

  delete(venta: IVenta): void {
    const modalRef = this.modalService.open(VentaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.venta = venta;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  openModal(venta?: IVenta): void {
    const modalRef = this.modalService.open(VentaUpdateModalComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.venta = venta;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'success') {
        this.loadPage();
      }
    });
  }
  openVista(venta?: IVenta): void {
    const modalRef = this.modalService.open(VistaDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.venta = venta;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'success') {
        this.loadPage();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = +(page ?? 1);
      const sort = (params.get(SORT) ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === ASC;
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IVenta[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/venta'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.ventas = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}