export interface ICliente {
  id?: number;
  dni?: string;
  nombre?: string;
  direccion?: string | null;
  telefono?: number;
}

export class Cliente implements ICliente {
  constructor(
    public id?: number,
    public dni?: string,
    public nombre?: string,
    public direccion?: string | null,
    public telefono?: number
  ) {}
}

export function getClienteIdentifier(cliente: ICliente): number | undefined {
  return cliente.id;
}
