export interface ICliente {
  id?: number;
  dni?: string;
  nombre?: string;
  direccion?: string | null;
  telefono?: number;
  empresa?: boolean | null;
}

export class Cliente implements ICliente {
  constructor(
    public id?: number,
    public dni?: string,
    public nombre?: string,
    public direccion?: string | null,
    public telefono?: number,
    public empresa?: boolean |null
  ) {}
}

export function getClienteIdentifier(cliente: ICliente): number | undefined {
  return cliente.id;
}
