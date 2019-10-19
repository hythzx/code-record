import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CodeRecordApplicationTestModule } from '../../../test.module';
import { SubmitRecordDeleteDialogComponent } from 'app/entities/submit-record/submit-record-delete-dialog.component';
import { SubmitRecordService } from 'app/entities/submit-record/submit-record.service';

describe('Component Tests', () => {
  describe('SubmitRecord Management Delete Component', () => {
    let comp: SubmitRecordDeleteDialogComponent;
    let fixture: ComponentFixture<SubmitRecordDeleteDialogComponent>;
    let service: SubmitRecordService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CodeRecordApplicationTestModule],
        declarations: [SubmitRecordDeleteDialogComponent]
      })
        .overrideTemplate(SubmitRecordDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SubmitRecordDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SubmitRecordService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
