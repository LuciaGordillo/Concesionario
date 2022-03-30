import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICoche } from '../coche.model';
import { CocheService } from '../service/coche.service';

@Component({
  templateUrl: './coche-refresh-dialog.component.html',
})
export class CocheRefreshDialogComponent {
  

  constructor(protected cocheService: CocheService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  refrescar(): void {
   
      this.activeModal.close('confirmed');
  }
}
