import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDeveloper } from 'app/shared/model/developer.model';
import { DeveloperService } from './developer.service';

@Component({
  selector: 'jhi-developer-delete-dialog',
  templateUrl: './developer-delete-dialog.component.html'
})
export class DeveloperDeleteDialogComponent {
  developer: IDeveloper;

  constructor(protected developerService: DeveloperService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.developerService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'developerListModification',
        content: 'Deleted an developer'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-developer-delete-popup',
  template: ''
})
export class DeveloperDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ developer }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(DeveloperDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.developer = developer;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/developer', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/developer', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
