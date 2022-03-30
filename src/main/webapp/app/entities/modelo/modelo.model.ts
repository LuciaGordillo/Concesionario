import { IMarca } from 'app/entities/marca/marca.model';

export interface IModelo {
  id?: number;
  nombre?: string;
  marca?: IMarca;
}

export class Modelo implements IModelo {
  constructor(public id?: number, public nombre?: string, public marca?: IMarca) {}
}

export function getModeloIdentifier(modelo: IModelo): number | undefined {
  return modelo.id;
}
