import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CodeRecordApplicationTestModule } from '../../../test.module';
import { SubmitRecordUpdateComponent } from 'app/entities/submit-record/submit-record-update.component';
import { SubmitRecordService } from 'app/entities/submit-record/submit-record.service';
import { SubmitRecord } from 'app/shared/model/submit-record.model';

describe('Component Tests', () => {
  describe('SubmitRecord Management Update Component', () => {
    let comp: SubmitRecordUpdateComponent;
    let fixture: ComponentFixture<SubmitRecordUpdateComponent>;
    let service: SubmitRecordService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CodeRecordApplicationTestModule],
        declarations: [SubmitRecordUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(SubmitRecordUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SubmitRecordUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SubmitRecordService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new SubmitRecord(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new SubmitRecord();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
