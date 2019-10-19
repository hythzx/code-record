import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISubmitRecord } from 'app/shared/model/submit-record.model';

type EntityResponseType = HttpResponse<ISubmitRecord>;
type EntityArrayResponseType = HttpResponse<ISubmitRecord[]>;

@Injectable({ providedIn: 'root' })
export class SubmitRecordService {
  public resourceUrl = SERVER_API_URL + 'api/submit-records';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/submit-records';

  constructor(protected http: HttpClient) {}

  create(submitRecord: ISubmitRecord): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(submitRecord);
    return this.http
      .post<ISubmitRecord>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(submitRecord: ISubmitRecord): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(submitRecord);
    return this.http
      .put<ISubmitRecord>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISubmitRecord>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISubmitRecord[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISubmitRecord[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(submitRecord: ISubmitRecord): ISubmitRecord {
    const copy: ISubmitRecord = Object.assign({}, submitRecord, {
      recordDate: submitRecord.recordDate != null && submitRecord.recordDate.isValid() ? submitRecord.recordDate.format(DATE_FORMAT) : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.recordDate = res.body.recordDate != null ? moment(res.body.recordDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((submitRecord: ISubmitRecord) => {
        submitRecord.recordDate = submitRecord.recordDate != null ? moment(submitRecord.recordDate) : null;
      });
    }
    return res;
  }
}
