export interface IVendedor {
  id?: number;
  dni?: string;
  nombre?: string | null;
  direccion?: string | null;
  telefono?: number;
}

export class Vendedor implements IVendedor {
  constructor(
    public id?: number,
    public dni?: string,
    public nombre?: string | null,
    public direccion?: string | null,
    public telefono?: number
  ) {}
}

export function getVendedorIdentifier(vendedor: IVendedor): number | undefined {
  return vendedor.id;
}
