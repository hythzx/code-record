import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CodeRecordApplicationTestModule } from '../../../test.module';
import { DeveloperDeleteDialogComponent } from 'app/entities/developer/developer-delete-dialog.component';
import { DeveloperService } from 'app/entities/developer/developer.service';

describe('Component Tests', () => {
  describe('Developer Management Delete Component', () => {
    let comp: DeveloperDeleteDialogComponent;
    let fixture: ComponentFixture<DeveloperDeleteDialogComponent>;
    let service: DeveloperService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CodeRecordApplicationTestModule],
        declarations: [DeveloperDeleteDialogComponent]
      })
        .overrideTemplate(DeveloperDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DeveloperDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DeveloperService);
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
