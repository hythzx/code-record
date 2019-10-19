import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Developer } from 'app/shared/model/developer.model';
import { DeveloperService } from './developer.service';
import { DeveloperComponent } from './developer.component';
import { DeveloperDetailComponent } from './developer-detail.component';
import { DeveloperUpdateComponent } from './developer-update.component';
import { DeveloperDeletePopupComponent } from './developer-delete-dialog.component';
import { IDeveloper } from 'app/shared/model/developer.model';

@Injectable({ providedIn: 'root' })
export class DeveloperResolve implements Resolve<IDeveloper> {
  constructor(private service: DeveloperService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDeveloper> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Developer>) => response.ok),
        map((developer: HttpResponse<Developer>) => developer.body)
      );
    }
    return of(new Developer());
  }
}

export const developerRoute: Routes = [
  {
    path: '',
    component: DeveloperComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'codeRecordApplicationApp.developer.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DeveloperDetailComponent,
    resolve: {
      developer: DeveloperResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'codeRecordApplicationApp.developer.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DeveloperUpdateComponent,
    resolve: {
      developer: DeveloperResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'codeRecordApplicationApp.developer.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DeveloperUpdateComponent,
    resolve: {
      developer: DeveloperResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'codeRecordApplicationApp.developer.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const developerPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: DeveloperDeletePopupComponent,
    resolve: {
      developer: DeveloperResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'codeRecordApplicationApp.developer.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
