import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVenta } from '../venta.model';
import { VentaService } from '../service/venta.service';

@Component({
  templateUrl: './venta-modal-vista.component.html',
})
export class VistaDialogComponent {
  venta?: IVenta;

  constructor(protected ventaService: VentaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ventaService.delete(id).subscribe(() => {
      this.activeModal.close('succes');
    });
  }
}
