import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDeveloper } from 'app/shared/model/developer.model';

type EntityResponseType = HttpResponse<IDeveloper>;
type EntityArrayResponseType = HttpResponse<IDeveloper[]>;

@Injectable({ providedIn: 'root' })
export class DeveloperService {
  public resourceUrl = SERVER_API_URL + 'api/developers';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/developers';

  constructor(protected http: HttpClient) {}

  create(developer: IDeveloper): Observable<EntityResponseType> {
    return this.http.post<IDeveloper>(this.resourceUrl, developer, { observe: 'response' });
  }

  update(developer: IDeveloper): Observable<EntityResponseType> {
    return this.http.put<IDeveloper>(this.resourceUrl, developer, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDeveloper>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDeveloper[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDeveloper[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
