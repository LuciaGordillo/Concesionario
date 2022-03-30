import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'coche',
        data: { pageTitle: 'concesionarioApp.coche.home.title' },
        loadChildren: () => import('./coche/coche.module').then(m => m.CocheModule),
      },
      {
        path: 'marca',
        data: { pageTitle: 'concesionarioApp.marca.home.title' },
        loadChildren: () => import('./marca/marca.module').then(m => m.MarcaModule),
      },
      {
        path: 'modelo',
        data: { pageTitle: 'concesionarioApp.modelo.home.title' },
        loadChildren: () => import('./modelo/modelo.module').then(m => m.ModeloModule),
      },
      {
        path: 'venta',
        data: { pageTitle: 'concesionarioApp.venta.home.title' },
        loadChildren: () => import('./venta/venta.module').then(m => m.VentaModule),
      },
      {
        path: 'cliente',
        data: { pageTitle: 'concesionarioApp.cliente.home.title' },
        loadChildren: () => import('./cliente/cliente.module').then(m => m.ClienteModule),
      },
      {
        path: 'vendedor',
        data: { pageTitle: 'concesionarioApp.vendedor.home.title' },
        loadChildren: () => import('./vendedor/vendedor.module').then(m => m.VendedorModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
