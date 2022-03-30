import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IVendedor, Vendedor } from '../vendedor.model';
import { VendedorService } from '../service/vendedor.service';

@Component({
  selector: 'jhi-vendedor-update',
  templateUrl: './vendedor-update.component.html',
})
export class VendedorUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    dni: [null, [Validators.required]],
    nombre: [],
    direccion: [],
    telefono: [null, [Validators.required]],
  });

  constructor(protected vendedorService: VendedorService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vendedor }) => {
      this.updateForm(vendedor);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vendedor = this.createFromForm();
    if (vendedor.id !== undefined) {
      this.subscribeToSaveResponse(this.vendedorService.update(vendedor));
    } else {
      this.subscribeToSaveResponse(this.vendedorService.create(vendedor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVendedor>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(vendedor: IVendedor): void {
    this.editForm.patchValue({
      id: vendedor.id,
      dni: vendedor.dni,
      nombre: vendedor.nombre,
      direccion: vendedor.direccion,
      telefono: vendedor.telefono,
    });
  }

  protected createFromForm(): IVendedor {
    return {
      ...new Vendedor(),
      id: this.editForm.get(['id'])!.value,
      dni: this.editForm.get(['dni'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      direccion: this.editForm.get(['direccion'])!.value,
      telefono: this.editForm.get(['telefono'])!.value,
    };
  }
}
