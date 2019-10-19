import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISubmitRecord } from 'app/shared/model/submit-record.model';
import { SubmitRecordService } from './submit-record.service';

@Component({
  selector: 'jhi-submit-record-delete-dialog',
  templateUrl: './submit-record-delete-dialog.component.html'
})
export class SubmitRecordDeleteDialogComponent {
  submitRecord: ISubmitRecord;

  constructor(
    protected submitRecordService: SubmitRecordService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.submitRecordService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'submitRecordListModification',
        content: 'Deleted an submitRecord'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-submit-record-delete-popup',
  template: ''
})
export class SubmitRecordDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ submitRecord }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(SubmitRecordDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.submitRecord = submitRecord;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/submit-record', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/submit-record', { outlets: { popup: null } }]);
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
