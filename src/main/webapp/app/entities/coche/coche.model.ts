import { IMarca } from 'app/entities/marca/marca.model';
import { IModelo } from 'app/entities/modelo/modelo.model';

export interface ICoche {
  id?: number;
  color?: string;
  matricula?: string;
  potencia?: number | null;
  precio?: number | null;
  puertas?: string | null;
  combustible?: string | null;
  vendido?: boolean;
  marca?: IMarca;
  modelo?: IModelo;
}

export class Coche implements ICoche {
  constructor(
    public id?: number,
    public color?: string,
    public matricula?: string,
    public potencia?: number | null,
    public precio?: number | null,
    public puertas?: string | null,
    public combustible?: string | null,
    public vendido?: boolean,
    public marca?: IMarca,
    public modelo?: IModelo
  ) {
    this.vendido = this.vendido ?? false;
  }
}

export function getCocheIdentifier(coche: ICoche): number | undefined {
  return coche.id;
}
