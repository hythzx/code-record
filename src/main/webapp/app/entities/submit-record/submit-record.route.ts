import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SubmitRecord } from 'app/shared/model/submit-record.model';
import { SubmitRecordService } from './submit-record.service';
import { SubmitRecordComponent } from './submit-record.component';
import { SubmitRecordDetailComponent } from './submit-record-detail.component';
import { SubmitRecordUpdateComponent } from './submit-record-update.component';
import { SubmitRecordDeletePopupComponent } from './submit-record-delete-dialog.component';
import { ISubmitRecord } from 'app/shared/model/submit-record.model';

@Injectable({ providedIn: 'root' })
export class SubmitRecordResolve implements Resolve<ISubmitRecord> {
  constructor(private service: SubmitRecordService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISubmitRecord> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<SubmitRecord>) => response.ok),
        map((submitRecord: HttpResponse<SubmitRecord>) => submitRecord.body)
      );
    }
    return of(new SubmitRecord());
  }
}

export const submitRecordRoute: Routes = [
  {
    path: '',
    component: SubmitRecordComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'codeRecordApplicationApp.submitRecord.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SubmitRecordDetailComponent,
    resolve: {
      submitRecord: SubmitRecordResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'codeRecordApplicationApp.submitRecord.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SubmitRecordUpdateComponent,
    resolve: {
      submitRecord: SubmitRecordResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'codeRecordApplicationApp.submitRecord.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SubmitRecordUpdateComponent,
    resolve: {
      submitRecord: SubmitRecordResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'codeRecordApplicationApp.submitRecord.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const submitRecordPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: SubmitRecordDeletePopupComponent,
    resolve: {
      submitRecord: SubmitRecordResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'codeRecordApplicationApp.submitRecord.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
