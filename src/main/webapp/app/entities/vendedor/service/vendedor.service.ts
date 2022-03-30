import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVendedor, getVendedorIdentifier } from '../vendedor.model';

export type EntityResponseType = HttpResponse<IVendedor>;
export type EntityArrayResponseType = HttpResponse<IVendedor[]>;

@Injectable({ providedIn: 'root' })
export class VendedorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vendedors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vendedor: IVendedor): Observable<EntityResponseType> {
    return this.http.post<IVendedor>(this.resourceUrl, vendedor, { observe: 'response' });
  }

  update(vendedor: IVendedor): Observable<EntityResponseType> {
    return this.http.put<IVendedor>(`${this.resourceUrl}/${getVendedorIdentifier(vendedor) as number}`, vendedor, { observe: 'response' });
  }

  partialUpdate(vendedor: IVendedor): Observable<EntityResponseType> {
    return this.http.patch<IVendedor>(`${this.resourceUrl}/${getVendedorIdentifier(vendedor) as number}`, vendedor, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVendedor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVendedor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVendedorToCollectionIfMissing(vendedorCollection: IVendedor[], ...vendedorsToCheck: (IVendedor | null | undefined)[]): IVendedor[] {
    const vendedors: IVendedor[] = vendedorsToCheck.filter(isPresent);
    if (vendedors.length > 0) {
      const vendedorCollectionIdentifiers = vendedorCollection.map(vendedorItem => getVendedorIdentifier(vendedorItem)!);
      const vendedorsToAdd = vendedors.filter(vendedorItem => {
        const vendedorIdentifier = getVendedorIdentifier(vendedorItem);
        if (vendedorIdentifier == null || vendedorCollectionIdentifiers.includes(vendedorIdentifier)) {
          return false;
        }
        vendedorCollectionIdentifiers.push(vendedorIdentifier);
        return true;
      });
      return [...vendedorsToAdd, ...vendedorCollection];
    }
    return vendedorCollection;
  }
}
