
import { Component,OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import dayjs from 'dayjs/esm';


import { IVenta, Venta } from '../venta.model';
import { VentaService } from '../service/venta.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-venta-modal-update',
  templateUrl: './venta-modal-update.component.html',
})
export class VentaUpdateModalComponent implements OnInit {
  isSaving = false;
  venta?: IVenta;
  
  editForm = this.fb.group({
    id: [],
    unidades: [null, [Validators.required]],
    fecha: [dayjs(), [Validators.required]],
    descuento: [],
    precio_total: [null, [Validators.required]],
  });

  constructor(
    protected ventaService: VentaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected activeModal :NgbActiveModal,
  ){}
  cancel(): void {
    this.activeModal.dismiss();
  }

  ngOnInit(): void {
      this.updateForm(this.venta!);

  }


  save(): void {
    this.isSaving = true;
    const venta = this.createFromForm();
    if (venta.id != null) {
      this.subscribeToSaveResponse(this.ventaService.update(venta));
    } else {
      this.subscribeToSaveResponse(this.ventaService.create(venta));
    }
  }


  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVenta>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
      this.activeModal.close('success');
    
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(venta: IVenta): void {
    this.editForm.patchValue({
      id: venta.id,
      unidades: venta.unidades,
      fecha: venta.fecha,
      descuento: venta.descuento,
      precio_total: venta.precio_total,
    });

  }


  protected createFromForm(): IVenta {
    return {
      ...new Venta(),
      id: this.editForm.get(['id'])!.value,
      unidades: this.editForm.get(['unidades'])!.value,
      fecha: this.editForm.get(['fecha'])!.value,
      descuento: this.editForm.get(['descuento'])!.value,
      precio_total: this.editForm.get(['precio_total'])!.value,
    };
  }
}
