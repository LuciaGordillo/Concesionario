/* import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VentaComponent } from './list/venta.component';
import { VentaDetailComponent } from './detail/venta-detail.component';
import { VentaUpdateComponent } from './update/venta-update.component';
import { VentaDeleteDialogComponent } from './delete/venta-delete-dialog.component';
import {VentaUpdateModalComponent} from './modal/venta-update-modal.component';
import { VentaRoutingModule } from './route/venta-routing.module';

@NgModule({
  imports: [SharedModule, VentaRoutingModule],
  declarations: [VentaComponent, VentaDetailComponent, VentaUpdateComponent, VentaDeleteDialogComponent, VentaUpdateModalComponent],
  entryComponents: [VentaUpdateModalComponent, VentaDeleteDialogComponent],
})
export class VentaModule {}/
 */
import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VentaComponent } from './list/venta.component';
import { VentaDetailComponent } from './detail/venta-detail.component';
import { VentaUpdateComponent } from './update/venta-update.component';
import { VentaUpdateModalComponent } from './venta-modal-update/venta-modal-update.component';
import { VistaDialogComponent } from './venta-modal-vista/venta-modal-vista.component';
import { VentaDeleteDialogComponent } from './delete/venta-delete-dialog.component';
import { VentaRoutingModule } from './route/venta-routing.module';

@NgModule({
  imports: [SharedModule, VentaRoutingModule],
  declarations: [VentaComponent, VentaDetailComponent, VentaUpdateComponent, VentaDeleteDialogComponent,VentaUpdateModalComponent,VistaDialogComponent],
  entryComponents: [VentaDeleteDialogComponent, VentaUpdateModalComponent,VistaDialogComponent],
})
export class VentaModule {}