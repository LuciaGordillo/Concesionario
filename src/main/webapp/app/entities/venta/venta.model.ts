import dayjs from 'dayjs/esm';

export interface IVenta {
  id?: number;
  unidades?: number;
  fecha?: dayjs.Dayjs;
  descuento?: number | null;
  precio_total?: number;
}

export class Venta implements IVenta {
  constructor(
    public id?: number,
    public unidades?: number,
    public fecha?: dayjs.Dayjs,
    public descuento?: number | null,
    public precio_total?: number
  ) {}
}

export function getVentaIdentifier(venta: IVenta): number | undefined {
  return venta.id;
}
